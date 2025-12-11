package ru.yandex.practicum.my.bank.commons.model.dto.transfer;

import java.util.UUID;

public record TransferResultDto(
        UUID transactionId,
        String fromUsername,
        String toUsername
) {
}
