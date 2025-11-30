package ru.yandex.practicum.my.bank.commons.model.dto.accounts;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record AccountDto(
        UUID id,
        String firstName,
        String lastName,
        LocalDate birthDate,
        BigDecimal balance
) {
}
