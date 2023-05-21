package dean.project.Dride.config.security.providers;

import com.twilio.jwt.accesstoken.Grant;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static dean.project.Dride.utilities.SecurityUrls.INCORRECT_CREDENTIALS;

@Component
@AllArgsConstructor
public class DrideAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String incomingEmail = authentication.getPrincipal().toString();
        String incomingPassword = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailsService.loadUserByUsername(incomingEmail);
        String userEmail = userDetails.getUsername();
        String userPassword = userDetails.getPassword();
        Collection<? extends GrantedAuthority> userAuthorities = userDetails.getAuthorities();

        if (passwordEncoder.matches(incomingPassword, userPassword)) {
            return new UsernamePasswordAuthenticationToken(userEmail, userPassword, userAuthorities);
        }
        throw new BadCredentialsException(INCORRECT_CREDENTIALS);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
