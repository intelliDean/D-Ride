package dean.project.Dride.config.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import dean.project.Dride.config.security.utilities.JwtUtil;
import dean.project.Dride.config.security.utilities.LoginResponse;
import dean.project.Dride.data.models.User;
import dean.project.Dride.exceptions.DrideException;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static dean.project.Dride.utilities.Constants.AUTHENTICATION_FAILED;

@AllArgsConstructor
public class DrideAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper;

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

        try {
            User user = mapper.readValue(request.getInputStream(), User.class);
            // 1. Create an authentication object that contains authentication credentials,
            //    but is not yet authenticated.
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
            //Note: Only use username and password to authenticate, do not add authorities until after authentication
            // 2. Delegate authentication responsibility for authentication object in 1 to the manager
            // 3. Get back the now authenticated authentication object from the manager
            Authentication authenticationResult = authenticationManager.authenticate(authentication);

            if (authenticationResult != null) {
                // 4. store authenticated authentication object in the security context
                return getAuthentication(authenticationResult);
            }
        } catch (IOException e) {
            throw new DrideException("Authentication not successful");
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

        Map<String, Object> claims = new HashMap<>();

        String email = authResult.getPrincipal().toString();
        authResult.getAuthorities().forEach(role -> claims.put("claim", role));


        String accessToken = jwtUtil.generateAccessToken(claims, email);
        String refreshToken = jwtUtil.generateRefreshToken(email);

        LoginResponse loginResponse = LoginResponse.builder()
                .message("Login successful")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getOutputStream(), loginResponse);
    }
}
