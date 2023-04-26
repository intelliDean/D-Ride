package dean.project.Dride.config.security.filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import dean.project.Dride.config.security.util.JwtUtil;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static dean.project.Dride.utilities.AdminUrls.ADMIN_BASE_URL;
import static dean.project.Dride.utilities.Constants.AUTHENTICATION_FAILED;
import static dean.project.Dride.utilities.Constants.LOGIN_URL;
import static dean.project.Dride.utilities.SecurityUrls.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@AllArgsConstructor
public class DrideAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        String authHeader = request.getHeader(AUTHORIZATION);

        UserDetails userDetails = (UserDetails) request.getUserPrincipal();

        boolean toLogin = request.getServletPath().equals(LOGIN_URL);
        boolean toRegisterDriver = request.getServletPath().equals(DRIVER_REGISTER);
        boolean toRegisterPassenger = request.getServletPath().equals(PASSENGER_REGISTER);
        boolean toInviteAdmin = request.getServletPath().equals(ADMIN_BASE_URL);
        boolean toVerifyUser = request.getServletPath().equals(VERIFY_USER);
        boolean adminDetails = request.getServletPath().equals(ADMIN_DETAILS);

        if (toLogin || toRegisterDriver || toRegisterPassenger ||
                toInviteAdmin || toVerifyUser || adminDetails) {
            filterChain.doFilter(request, response);
        } else {
            if (StringUtils.hasText(authHeader) && StringUtils.startsWithIgnoreCase(authHeader, BEARER)) {
                String token = request.getHeader(AUTHORIZATION);
                String jwt = token.substring(BEARER.length());   //"Bearer ".length()
                boolean tokenIsSigned = Jwts.parserBuilder()
                        .setSigningKey(jwtUtil.getJwtSecret())
                        .build()
                        .isSigned(jwt);

                if (tokenIsSigned) {
                    savePrincipalInSecurityContextHolder(userDetails, jwt);
                    filterChain.doFilter(request, response);

                } else {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    mapper.writeValue(response.getOutputStream(),
                            GlobalApiResponse.builder()
                                    .message(AUTHENTICATION_FAILED)
                                    .build());
                }
            }
        }
    }

    private void savePrincipalInSecurityContextHolder(UserDetails userDetails, String jwt) {
        List<String> roles = new ArrayList<>();
        var jwtMap = Jwts.parserBuilder()
                .setSigningKey(jwtUtil.getJwtSecret())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        jwtMap.forEach((k, v) -> roles.add(v.toString()));

        List<SimpleGrantedAuthority> roleLists = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, roleLists);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}