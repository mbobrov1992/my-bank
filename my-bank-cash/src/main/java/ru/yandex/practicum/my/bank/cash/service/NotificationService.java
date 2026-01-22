package ru.yandex.practicum.my.bank.cash.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashResultDto;
import ru.yandex.practicum.my.bank.commons.model.dto.notifications.NotificationDto;
import ru.yandex.practicum.my.bank.commons.service.NotificationProducer;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Import(NotificationProducer.class)
public class NotificationService {

    private final NotificationProducer notificationProducer;

    public void notifyCashEdit(CashResultDto cashResult) {
        Mono.just(new NotificationDto(
                        cashResult.username(),
                        "Изменен баланс пользователя: " + cashResult.username(),
                        ZonedDateTime.now()
                ))
                .flatMap(notificationProducer::notify)
                .doOnSuccess(ignore -> log.debug(
                        "Отправлено уведомление об изменении баланса пользователя: {}", cashResult.username()))
                .subscribe();
    }
}
