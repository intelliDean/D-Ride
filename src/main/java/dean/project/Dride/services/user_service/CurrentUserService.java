package dean.project.Dride.services.user_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dean.project.Dride.config.security.filters.DrideAuthenticationFilter;
import dean.project.Dride.config.security.users.AuthenticatedUser;
import dean.project.Dride.config.security.util.JwtUtil;
import dean.project.Dride.data.dto.request.LoginRequest;
import dean.project.Dride.data.models.User;
import dean.project.Dride.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import java.util.Map;

import static dean.project.Dride.utilities.Constants.LOGIN_URL;

@Service
@AllArgsConstructor
public class CurrentUserService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    public User currentUser () {
        try {
            AuthenticatedUser authenticatedUser =
                    (AuthenticatedUser) SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getPrincipal();
            return authenticatedUser.getUser();
        } catch (Exception e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }

     public void login(LoginRequest request) {
        UsernamePasswordAuthenticationFilter authenticationFilter =
                new DrideAuthenticationFilter(authenticationManager, jwtUtil, objectMapper);
        authenticationFilter.setFilterProcessesUrl("/api/v1/login");
    }
}