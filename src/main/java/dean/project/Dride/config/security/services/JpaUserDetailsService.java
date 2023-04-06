package dean.project.Dride.config.security.services;

import dean.project.Dride.config.security.users.SecureUser;
import dean.project.Dride.data.models.Users;
import dean.project.Dride.services.user_services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class JpaUserDetailsService implements UserDetailsService {
    private final UserService appUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = appUserService.getByEmail(username);
        return new SecureUser(user);
    }
}
