package ru.yandex.practicum.my.bank.commons.model.dto.transfer;

import java.math.BigDecimal;

public record TransferDto(
        String toUsername,
        BigDecimal amount
) {
}
