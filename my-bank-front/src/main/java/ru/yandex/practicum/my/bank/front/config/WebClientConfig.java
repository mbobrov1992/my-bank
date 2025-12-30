package ru.yandex.practicum.my.bank.front.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient accountsWebClient(
            WebClient.Builder webClientBuilder,
            @Value("${service.accounts.url}") String accountsUrl
    ) {
        return webClientBuilder
                .baseUrl(accountsUrl)
                .build();
    }

    @Bean
    public WebClient cashWebClient(
            WebClient.Builder webClientBuilder,
            @Value("${service.cash.url}") String cashUrl
    ) {
        return webClientBuilder
                .baseUrl(cashUrl)
                .build();
    }

    @Bean
    public WebClient transferWebClient(
            WebClient.Builder webClientBuilder,
            @Value("${service.transfer.url}") String transferUrl
    ) {
        return webClientBuilder
                .baseUrl(transferUrl)
                .build();
    }

    @Bean
    public WebClient.Builder webClientBuilder(
            ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Filter,
            ErrorHandlingFilter errorHandlingFilter
    ) {
        return WebClient.builder()
                .filter(oauth2Filter)
                .filter(errorHandlingFilter);
    }
}
