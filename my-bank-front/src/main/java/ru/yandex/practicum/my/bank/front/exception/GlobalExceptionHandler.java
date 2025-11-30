package ru.yandex.practicum.my.bank.front.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.exception.RemoteServiceException;

import static ru.yandex.practicum.my.bank.front.util.SessionUtils.putSessionKey;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FormValidationException.class)
    public Mono<Rendering> handle(ServerWebExchange exchange, FormValidationException ex) {
        return putSessionKeyAndRedirect(exchange, ex.getMessage());
    }

    @ExceptionHandler(RemoteServiceException.class)
    public Mono<Rendering> handle(ServerWebExchange exchange, RemoteServiceException ex) {
        return putSessionKeyAndRedirect(exchange, ex.getMessage());
    }

    @ExceptionHandler(WebClientException.class)
    public Mono<Rendering> handle(ServerWebExchange exchange, WebClientException ex) {
        return putSessionKeyAndRedirect(exchange, ex.getMessage());
    }

    private Mono<Rendering> putSessionKeyAndRedirect(ServerWebExchange exchange, String error) {
        return putSessionKey(exchange, "error", error)
                .thenReturn(Rendering.redirectTo("/account").build());
    }
}
