package dean.project.Dride.config.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import dean.project.Dride.config.security.filters.DrideAuthenticationFilter;
import dean.project.Dride.config.security.filters.DrideAuthorizationFilter;
import dean.project.Dride.config.security.utilities.JwtUtil;
import dean.project.Dride.data.dto.request.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

import static dean.project.Dride.utilities.Constants.*;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    private final String[] AUTHENTICATION_WHITE_LIST = {DRIVER_REGISTER, PASSENGER_REGISTER,
            ADMIN_BASE_URL, LOGIN_URL, VERIFY_USER, ADMIN_DETAILS};
    private final String[] SWAGGERS = {"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, AUTHENTICATION_WHITE_LIST).permitAll()
                        .requestMatchers(SWAGGERS).permitAll()
                        .anyRequest().authenticated())
                .addFilterAt(login(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authorization(), DrideAuthenticationFilter.class)
                .build();
    }

    private UsernamePasswordAuthenticationFilter login() {
        UsernamePasswordAuthenticationFilter authenticationFilter =
                new DrideAuthenticationFilter(authenticationManager, jwtUtil, objectMapper);
        authenticationFilter.setFilterProcessesUrl(LOGIN_URL);
        return authenticationFilter;
    }

    private DrideAuthorizationFilter authorization() {
        return new DrideAuthorizationFilter(userDetailsService, jwtUtil);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}