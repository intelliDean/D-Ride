package dean.project.Dride.services.user_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dean.project.Dride.config.security.users.AuthenticatedUser;
import dean.project.Dride.config.security.util.JwtUtil;
import dean.project.Dride.data.models.User;
import dean.project.Dride.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CurrentUserService {
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
}