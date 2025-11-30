package ru.yandex.practicum.my.bank.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.ErrorResponse;
import ru.yandex.practicum.my.bank.commons.model.enums.ErrorCode;
import ru.yandex.practicum.my.bank.commons.model.exception.AccountNotFoundException;
import ru.yandex.practicum.my.bank.commons.model.exception.LowBalanceException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LowBalanceException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInsufficientBalance(LowBalanceException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ErrorCode.LOW_BALANCE, ex.getMessage());

    }

    @ExceptionHandler(AccountNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleAccountNotFound(AccountNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ErrorCode.ACCOUNT_NOT_FOUND, ex.getMessage());
    }

    private Mono<ResponseEntity<ErrorResponse>> buildResponse(
            HttpStatus status, ErrorCode errorCode, String message
    ) {
        return Mono.just(ResponseEntity.status(status)
                .body(new ErrorResponse(
                        status.value(),
                        errorCode,
                        message)));
    }
}
