package ru.yandex.practicum.my.bank.cash.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient accountWebClient(
            ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Filter,
            @Value("${service.accounts.url}") String accountsUrl
    ) {
        return webClientBuilder()
                .baseUrl(accountsUrl)
                .filter(oauth2Filter)
                .build();
    }

    @Bean
    public WebClient notificationWebClient(
            ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Filter,
            @Value("${service.notifications.url}") String notificationsUrl
    ) {
        return webClientBuilder()
                .baseUrl(notificationsUrl)
                .filter(oauth2Filter)
                .build();
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
