package dean.project.Dride.services.user_service.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import dean.project.Dride.config.security.users.AuthenticatedUser;
import dean.project.Dride.config.security.utilities.JwtUtil;
import dean.project.Dride.data.dto.request.CreateUser;
import dean.project.Dride.data.models.User;
import dean.project.Dride.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class UtilityUserImpl implements UtilityUser {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User currentUser() {
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

    @Override
    public User createUser(CreateUser createUser) {
        return User.builder()
                .name(createUser.getName())
                .email(createUser.getEmail())
                .password(passwordEncoder.encode(createUser.getPassword()))
                .createdAt(LocalDateTime.now().toString())
                .roles(new HashSet<>())
                .build();
    }

    @Override
    public int calculateAge(String dateOfBirth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthDate = LocalDate.parse(dateOfBirth, formatter);
        LocalDate currentDate = LocalDate.now();

        Period period = Period.between(birthDate, currentDate);
        return period.getYears();
    }
}