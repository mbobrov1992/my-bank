package ru.yandex.practicum.my.bank.commons.model.dto.cash;

import ru.yandex.practicum.my.bank.commons.model.enums.cash.CashAction;

import java.math.BigDecimal;

public record CashUpdateDto(
        CashAction cashAction,
        BigDecimal amount
) {
}
