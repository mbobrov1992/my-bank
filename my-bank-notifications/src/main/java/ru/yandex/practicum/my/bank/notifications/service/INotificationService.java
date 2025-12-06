package ru.yandex.practicum.my.bank.notifications.service;

import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.notifications.NotificationDto;

public interface INotificationService {

    Mono<Void> notify(NotificationDto notification);
}
