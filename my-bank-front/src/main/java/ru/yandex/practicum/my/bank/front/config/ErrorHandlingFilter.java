package ru.yandex.practicum.my.bank.front.config;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.ErrorResponse;
import ru.yandex.practicum.my.bank.commons.model.exception.RemoteServiceException;

@Component
public class ErrorHandlingFilter implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(request)
                .flatMap(response -> {
                    if (response.statusCode().is4xxClientError()) {
                        return response.bodyToMono(ErrorResponse.class)
                                .flatMap(error ->
                                        Mono.error(new RemoteServiceException(error.errorMessage())));
                    }
                    return Mono.just(response);
                });
    }
}
