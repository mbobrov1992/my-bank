package ru.yandex.practicum.my.bank.commons.model.dto.cash;

import java.math.BigDecimal;
import java.util.UUID;

public record CashResultDto(
        UUID transactionId,
        String username,
        BigDecimal newBalance
) {
}
