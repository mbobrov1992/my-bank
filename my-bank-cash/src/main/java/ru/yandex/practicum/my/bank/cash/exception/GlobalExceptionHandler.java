package ru.yandex.practicum.my.bank.cash.exception;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebClientResponseException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleWebClientResponseException(WebClientResponseException ex) {
        return Mono.just(ResponseEntity.status(ex.getStatusCode().value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ex.getResponseBodyAs(ErrorResponse.class)));
    }
}
