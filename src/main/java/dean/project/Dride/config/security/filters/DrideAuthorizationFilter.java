package dean.project.Dride.config.security.filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import dean.project.Dride.config.security.utilities.JwtUtil;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION);
        String servletPath = request.getServletPath();

        //This is doing the same as the WHITE_LIST in the SecurityConfiguration (grant free access)
        if (grantFreeAccessTo(servletPath)) filterChain.doFilter(request, response);
        else if (StringUtils.hasText(authHeader) && StringUtils.startsWithIgnoreCase(authHeader, BEARER)) {
            String token = authHeader.substring(BEARER.length());
            if (jwtUtil.isTokenValid(token)) {
                String email = jwtUtil.extractUsernameFromToken(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                saveToContext(request, userDetails);
            } else {
                failedAuthentication(response);
            }
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private static void saveToContext(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private static void failedAuthentication(@NotNull HttpServletResponse response) throws IOException {
        GlobalApiResponse failedResponse = GlobalApiResponse.builder().message(AUTHENTICATION_FAILED).build();
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getOutputStream(), failedResponse);
    }

    private boolean grantFreeAccessTo(String servletPath) {
        List<String> allowedPaths = List.of(
                LOGIN_URL, DRIVER_REGISTER, PASSENGER_REGISTER,
                ADMIN_BASE_URL, VERIFY_USER, ADMIN_DETAILS);
        return allowedPaths.contains(servletPath);
    }
}