package dean.project.Dride.config.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import dean.project.Dride.config.security.filters.DrideAuthenticationFilter;
import dean.project.Dride.config.security.filters.DrideAuthorizationFilter;
import dean.project.Dride.config.security.util.JwtUtil;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
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

import static dean.project.Dride.utilities.Constants.*;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final GlobalApiResponse.GlobalApiResponseBuilder globalApiResponse;
    private final JwtUtil jwtUtil;
    private final String[] AUTHENTICATION_WHITE_LIST = {DRIVER_REGISTER, PASSENGER_REGISTER,
            ADMIN_BASE_URL, LOGIN_URL, VERIFY_USER, ADMIN_DETAILS};
    private final String[] SWAGGERS = {"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //Login end point
        UsernamePasswordAuthenticationFilter authenticationFilter =
                new DrideAuthenticationFilter(authenticationManager, jwtUtil, objectMapper);
        authenticationFilter.setFilterProcessesUrl(LOGIN_URL);

        return http.csrf().disable().cors()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS)
                .and()
                .addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new DrideAuthorizationFilter(
                                userDetailsService, globalApiResponse, jwtUtil, objectMapper),
                        DrideAuthenticationFilter.class
                )
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, AUTHENTICATION_WHITE_LIST).permitAll()
                .requestMatchers(SWAGGERS).permitAll()
                .anyRequest().authenticated()
                .and()
                .build();
    }
}
