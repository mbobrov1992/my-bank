package ru.yandex.practicum.my.bank.accounts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.accounts.model.entity.AccountEnt;
import ru.yandex.practicum.my.bank.accounts.service.client.NotificationClient;
import ru.yandex.practicum.my.bank.commons.model.dto.notifications.NotificationDto;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationClient notificationClient;

    public void notifyAccountUpsert(AccountEnt account) {
        Mono.just(new NotificationDto(
                        String.valueOf(account.getId()),
                        "Создан/изменен аккаунт пользователя: " + account.getId(),
                        ZonedDateTime.now()
                ))
                .flatMap(notificationClient::notify)
                .doOnSuccess(ignore -> log.debug(
                        "Отправлено уведомление о создании/изменении аккаунта: {}", account.getId()))
                .subscribe();
    }
}
