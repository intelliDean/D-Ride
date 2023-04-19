package dean.project.Dride.config.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import dean.project.Dride.config.security.util.JwtUtil;
import dean.project.Dride.data.models.User;
import dean.project.Dride.exceptions.DrideException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

        //TODO: 1. Create an authentication object that contains authentication credentials,
        //TODO:    but is not yet authenticated.
        User user;
        try {
            user = mapper.readValue(request.getInputStream(), User.class);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
            //TODO: 2. Delegate authentication responsibility for authentication object in 1 to the manager
            //TODO: 3. Get back the now authenticated authentication object from the manager
            Authentication authenticationResult =
                    authenticationManager.authenticate(authentication);
            //TODO: 4. store authenticated authentication object in the security context
            if (authenticationResult != null) return getAuthentication(authenticationResult);
        } catch (IOException e) {
            throw new DrideException(e.getMessage());
        }
        throw new DrideException("oops!");
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
                .collect(Collectors.toMap(k -> "claim", v -> v));

        UserDetails user =   userDetailsService.loadUserByUsername((String)authResult.getPrincipal());//todo this might cause problems

        String accessToken = generateAccessToken(claims, user);

        String refreshToken = generateRefreshToken();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getOutputStream(), tokens);
    }

    private String generateRefreshToken() {
        Date refreshExpiration = Date.from(Instant.now()
                .plusSeconds(BigInteger.valueOf(3600).longValue() *
                        BigInteger.valueOf(24).intValue()));

        return Jwts.builder()
                .setIssuer("dride")
                .setExpiration(refreshExpiration)
                .signWith(SignatureAlgorithm.HS512, jwtUtil.getJwtSecret())
                .compact();
    }

    private String generateAccessToken(Map<String, Object> claims, UserDetails user) {
        Date accessExpiration = Date.from(Instant.now()
                .plusSeconds(BigInteger.valueOf(60).longValue() *
                        BigInteger.valueOf(60).intValue()));
        return Jwts.builder()
                .setIssuer("dride")
                .setIssuedAt(new Date())
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setExpiration(accessExpiration)
                .signWith(SignatureAlgorithm.HS512, jwtUtil.getJwtSecret())
                .compact();
    }
}
