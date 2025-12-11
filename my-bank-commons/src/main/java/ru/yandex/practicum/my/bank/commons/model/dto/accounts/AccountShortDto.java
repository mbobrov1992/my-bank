package ru.yandex.practicum.my.bank.commons.model.dto.accounts;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record AccountShortDto(
        UUID id,
        String firstName,
        String lastName
) {

    public String getFullName() {
        String fullName = Stream.of(lastName, firstName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));

        return fullName.isEmpty() ? String.valueOf(id) : fullName;
    }
}
