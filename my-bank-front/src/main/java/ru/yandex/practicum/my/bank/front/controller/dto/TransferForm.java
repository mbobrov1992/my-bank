package ru.yandex.practicum.my.bank.front.controller.dto;

import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.front.exception.TransferValidationException;

import java.math.BigDecimal;

public record TransferForm(
        BigDecimal value,
        String toAccount
) {
    public Mono<Void> validate() {
        if (value.signum() <= 0) {
            return Mono.error(new TransferValidationException("Сумма должна быть положительной"));
        }
        return Mono.empty();
    }
}
