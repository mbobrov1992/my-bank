package ru.yandex.practicum.my.bank.accounts.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

@TestConfiguration
@Import(SecurityConfig.class)
@ImportAutoConfiguration(exclude = ReactiveOAuth2ClientAutoConfiguration.class)
public class TestSecurityConfig {

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        return token -> Mono.just(
                Jwt.withTokenValue(token).build()
        );
    }
}
