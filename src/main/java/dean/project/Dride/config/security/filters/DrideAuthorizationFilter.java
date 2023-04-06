package dean.project.Dride.config.security.filters;


import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class DrideAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/v1/login")||
        request.getServletPath().equals("/api/v1/driver/register")){
            filterChain.doFilter(request, response);
        }
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwt = token.substring("Bearer ".length());

        Jwts.parser().parseClaimsJwt("roles");


    }
}
