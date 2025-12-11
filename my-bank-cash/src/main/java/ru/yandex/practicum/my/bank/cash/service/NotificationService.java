package ru.yandex.practicum.my.bank.cash.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.cash.service.client.NotificationClient;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashResultDto;
import ru.yandex.practicum.my.bank.commons.model.dto.notifications.NotificationDto;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationClient notificationClient;

    public void notifyCashEdit(CashResultDto cashResult) {
        Mono.just(new NotificationDto(
                        cashResult.username(),
                        "Изменен баланс пользователя: " + cashResult.username(),
                        ZonedDateTime.now()
                ))
                .flatMap(notificationClient::notify)
                .doOnSuccess(ignore -> log.debug(
                        "Отправлено уведомление об изменении баланса пользователя: {}", cashResult.username()))
                .subscribe();
    }
}
