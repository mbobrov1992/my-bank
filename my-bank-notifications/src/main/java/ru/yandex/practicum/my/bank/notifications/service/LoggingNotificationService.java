package ru.yandex.practicum.my.bank.notifications.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.notifications.NotificationDto;

@Slf4j
@Service
public class LoggingNotificationService implements INotificationService {

    @Override
    public Mono<Void> notify(NotificationDto notification) {
        return Mono.just(notification)
                .doOnNext(dto -> log.info("Получено уведомление: {}", dto.message()))
                .then();
    }
}
