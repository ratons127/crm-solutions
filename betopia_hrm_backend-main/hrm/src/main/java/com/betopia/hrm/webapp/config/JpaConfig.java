package com.betopia.hrm.webapp.config;

import com.betopia.hrm.domain.users.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;


@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfig {

    private final UserRepository userRepository;

    public JpaConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public AuditorAware<Long> auditor() {
//        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
//                .map(SecurityContext::getAuthentication)
//                .filter(Authentication::isAuthenticated)
//                .map(Authentication::getPrincipal)
//                .map(UserDetails.class::cast)
//                .map(u -> new Username(u.getUsername()));

        return () -> Optional.of(1l); // Default auditor for simplicity
    }

    /*@Bean
    public AuditorAware<Long> auditor() {
        return () -> {
            try {
                String username = AuthUtils.getCurrentUsername();
                if (username == null)
                    return Optional.empty();

                User user = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                        .stream()
                        .filter(p -> username.equals(p.getEmail()) || username.equals(p.getPhone()))
                        .findFirst()
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                return Optional.ofNullable(user.getId());

            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }*/
}
