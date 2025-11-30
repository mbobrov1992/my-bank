package ru.yandex.practicum.my.bank.commons.model.dto.accounts;

import java.time.LocalDate;

public record AccountUpdateDto(
        String lastName,
        String firstName,
        LocalDate birthDate
) {
}
