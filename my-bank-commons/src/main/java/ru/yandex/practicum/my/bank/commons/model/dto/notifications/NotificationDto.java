package ru.yandex.practicum.my.bank.commons.model.dto.notifications;

import java.time.ZonedDateTime;

public record NotificationDto(
        String username,
        String message,
        ZonedDateTime timestamp
) {
}
