package ru.yandex.practicum.my.bank.transfer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferDto;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferResultDto;
import ru.yandex.practicum.my.bank.transfer.service.client.TransferClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferClient transferClient;
    private final NotificationService notificationService;

    public Mono<TransferResultDto> transfer(String fromUsername, TransferDto request) {
        return transferClient.transfer(fromUsername, request)
                .doOnSuccess(notificationService::notifyTransfer);
    }
}
