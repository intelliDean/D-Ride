package dean.project.Dride.config.security.filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import dean.project.Dride.config.security.util.JwtUtil;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static dean.project.Dride.utilities.Constants.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@AllArgsConstructor
public class DrideAuthorizationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final GlobalApiResponse.GlobalApiResponseBuilder globalResponse;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION);
        String servletPath = request.getServletPath();

        if (StringUtils.hasText(authHeader) && StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
            String authToken = authHeader.substring("Bearer ".length());
            if (jwtUtil.isTokenValid(authToken)) {
                String email = jwtUtil.extractUsernameFromToken(authToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                savedToContext(request, userDetails);
            } else {
                handleAuthenticationFailure(response);
            }
        }
        filterChain.doFilter(request, response);
    }

    private static void savedToContext(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        //Note: After authentication, do not save the  user password in security context holder.
        authenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void handleAuthenticationFailure(HttpServletResponse response) throws IOException {
        GlobalApiResponse failureMessage = globalResponse.message(AUTHENTICATION_FAILED).build();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), failureMessage);
    }
}