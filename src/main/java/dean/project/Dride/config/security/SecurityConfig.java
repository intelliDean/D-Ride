package dean.project.Dride.config.security;


import dean.project.Dride.config.security.filters.DrideAuthenticationFilter;
import dean.project.Dride.config.security.filters.DrideAuthorizationFilter;
import dean.project.Dride.config.security.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static dean.project.Dride.utilities.AdminUrls.ADMIN_BASE_URL;
import static dean.project.Dride.utilities.Constants.LOGIN_URL;
import static dean.project.Dride.utilities.SecurityUrls.*;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final String[] AUTHENTICATION_WHITE_LIST = {DRIVER_REGISTER, PASSENGER_REGISTER,
            ADMIN_BASE_URL, LOGIN_URL, VERIFY_USER, ADMIN_DETAILS};
    private final String[] SWAGGERS={SWAGGER_HTML, SWAGGER_UI, SWAGGER_API_DOCS, SWAGGER_API_DOCS2};
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        UsernamePasswordAuthenticationFilter authenticationFilter =
                new DrideAuthenticationFilter(authenticationManager, jwtUtil, userDetailsService);
        authenticationFilter.setFilterProcessesUrl(LOGIN_URL);
        return http.csrf().disable().cors()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new DrideAuthorizationFilter(jwtUtil), DrideAuthenticationFilter.class)
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, AUTHENTICATION_WHITE_LIST).permitAll()
                .requestMatchers(SWAGGERS).permitAll()
                .anyRequest().authenticated()
                .and()
                .build();
    }
}
