package ru.yandex.practicum.my.bank.accounts.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final String NOTIFICATIONS_URL = "http://my-bank-notifications";

    @Bean
    public WebClient notificationWebClient(ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Filter) {
        return webClientBuilder()
                .baseUrl(NOTIFICATIONS_URL)
                .filter(oauth2Filter)
                .build();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
