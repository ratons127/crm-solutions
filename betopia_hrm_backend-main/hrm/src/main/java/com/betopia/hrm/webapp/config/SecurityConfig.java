package com.betopia.hrm.webapp.config;


import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.domain.users.repository.TokenRepository;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.auth.JwtService;
import com.betopia.hrm.webapp.filters.ByPassAuthorizationFilter;
import com.betopia.hrm.webapp.filters.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig {

    @Value("${app.disable.security}")
    private boolean disableSecurity;

    private UserRepository userRepository;

    private JwtService jwtService;

    private TokenRepository tokenRepository;

    public SecurityConfig(UserRepository userRepository, JwtService jwtService, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository users) {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return users
                        .findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/v1/auth/**",
                                "/v1/public/**",
                                "/images/**",
                                "/swagger-ui/**",       // Allow access to Swagger UI resources
                                "/v3/api-docs/**",      // Allow access to API docs (OpenAPI specification)
                                "/swagger-ui.html",     // Swagger UI main page
                                "/swagger-ui/index.html" // Swagger UI HTML page
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(
                       (disableSecurity ? new ByPassAuthorizationFilter() : new JwtAuthenticationFilter(jwtService, userDetailsService(userRepository), tokenRepository)), BasicAuthenticationFilter.class
                );

        return http.build();
    }
}
