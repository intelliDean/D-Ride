package dean.project.Dride.config.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import dean.project.Dride.config.security.util.JwtUtil;
import dean.project.Dride.data.models.User;
import dean.project.Dride.exceptions.DrideException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static dean.project.Dride.utilities.Constants.AUTHENTICATION_FAILED;
import static dean.project.Dride.utilities.Constants.ISSUER;
import static dean.project.Dride.utilities.SecurityUrls.*;

@AllArgsConstructor
public class DrideAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    //private final Key key;


    /*todo
     *  to authenticate
     * 1. the request comes in and it's intercepted by the UsernamePasswordAuthenticationFilter through the method attemptAuthentication()
     * 2. the attemptAuthentication method passes it to the AuthenticationManager and handled by method authenticate()
     * 3. the method authenticate passes it to the AuthenticationProvide and it's finally authenticated by method authenticate()
     * 4. after authentication, it's passed to successfulAuthentication in UsernamePasswordAuthenticationFilter which then generate jwt for it*/


    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        ObjectMapper mapper = new ObjectMapper();
        // 1. Create an authentication object that contains authentication credentials,
        //    but is not yet authenticated.
        User user;
        try {
            user = mapper.readValue(request.getInputStream(), User.class);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
            // 2. Delegate authentication responsibility for authentication object in 1 to the manager
            // 3. Get back the now authenticated authentication object from the manager
            Authentication authenticationResult =
                    authenticationManager.authenticate(authentication);
            // 4. store authenticated authentication object in the security context
            if (authenticationResult != null) return getAuthentication(authenticationResult);
        } catch (IOException e) {
            throw new DrideException(e.getMessage());
        }
        throw new DrideException(AUTHENTICATION_FAILED);
    }

    private static Authentication getAuthentication(Authentication authenticationResult) {
        SecurityContextHolder.getContext().setAuthentication(authenticationResult);
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> claims = authResult.getAuthorities().stream()
                .collect(Collectors.toMap(k -> CLAIM, v -> v));

        UserDetails user = userDetailsService.loadUserByUsername((String) authResult.getPrincipal());
        String accessToken = generateAccessToken(claims, user);

        String refreshToken = generateRefreshToken();

        Map<String, String> tokens = new HashMap<>();
        tokens.put(ACCESS_TOKEN, accessToken);
        tokens.put(REFRESH_TOKEN, refreshToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getOutputStream(), tokens);
    }

    private String generateRefreshToken() {
        Date refreshExpiration = Date.from(Instant.now()
                .plusSeconds(BigInteger.valueOf(3600).longValue() *
                        BigInteger.valueOf(24).intValue()));

        return Jwts.builder()
                .setIssuer(ISSUER)
                .setExpiration(refreshExpiration)
                .signWith(SignatureAlgorithm.HS512, jwtUtil.getJwtSecret())
                .compact();
    }

    private String generateAccessToken(Map<String, Object> claims, UserDetails user) {
        Date accessExpiration = Date.from(Instant.now()
                .plusSeconds(BigInteger.valueOf(60).longValue() *
                        BigInteger.valueOf(60).intValue()));
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setExpiration(accessExpiration)
                .signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.decode(jwtUtil.getJwtSecret()))
                .compact();
    }
}
