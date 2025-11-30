package ru.yandex.practicum.my.bank.front.controller.dto;

import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.enums.cash.CashAction;
import ru.yandex.practicum.my.bank.front.exception.CashValidationException;

import java.math.BigDecimal;

public record CashForm(
        BigDecimal value,
        CashAction action
) {
    public Mono<Void> validate() {
        if (value.signum() <= 0) {
            return Mono.error(new CashValidationException("Сумма должна быть положительной"));
        }
        return Mono.empty();
    }
}
