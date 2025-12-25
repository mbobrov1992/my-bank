package ru.yandex.practicum.my.bank.transfer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        String[] permitAntPatterns = new String[]{
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/actuator/health"
        };

        return http
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(permitAntPatterns).permitAll()
                        .pathMatchers("/v1/transfer/**").hasAuthority("SCOPE_transfer-access")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(customizer -> customizer
                        .jwt(Customizer.withDefaults())
                )
                .build();
    }
}
