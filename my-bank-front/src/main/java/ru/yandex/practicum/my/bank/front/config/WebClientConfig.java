package ru.yandex.practicum.my.bank.front.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private final String apiGatewayUrl;

    public WebClientConfig(@Value("${service.gateway.url}") String apiGatewayUrl) {
        this.apiGatewayUrl = apiGatewayUrl;
    }

    @Bean
    public WebClient apiGatewayClient(
            ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Filter,
            ErrorHandlingFilter errorHandlingFilter
    ) {
        return WebClient.builder()
                .baseUrl(apiGatewayUrl)
                .filter(oauth2Filter)
                .filter(errorHandlingFilter)
                .build();
    }
}
