package ru.yandex.practicum.my.bank.notifications.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.notifications.NotificationDto;
import ru.yandex.practicum.my.bank.notifications.service.INotificationService;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final INotificationService notificationService;

    @KafkaListener(topics = "${kafka.notifications.topic.name}")
    public Mono<Void> listen(NotificationDto notification) {
        return notificationService.notify(notification);
    }
}
