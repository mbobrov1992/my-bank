package ru.yandex.practicum.my.bank.commons.model.dto;

import ru.yandex.practicum.my.bank.commons.model.enums.ErrorCode;

public record ErrorResponse(
        int status,
        ErrorCode errorCode,
        String errorMessage
) {
}
