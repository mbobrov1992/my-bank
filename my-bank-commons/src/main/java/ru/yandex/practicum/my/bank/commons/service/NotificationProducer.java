package ru.yandex.practicum.my.bank.commons.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.metrics.NotificationMetrics;
import ru.yandex.practicum.my.bank.commons.model.dto.notifications.NotificationDto;

@Service
@Import(NotificationMetrics.class)
public class NotificationProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String notificationTopic;
    private final NotificationMetrics notificationMetrics;

    public NotificationProducer(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${kafka.notifications.topic.name}") String notificationTopic,
            NotificationMetrics notificationMetrics
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.notificationTopic = notificationTopic;
        this.notificationMetrics = notificationMetrics;
    }

    public Mono<Void> notify(NotificationDto notification) {
        return Mono.fromFuture(kafkaTemplate.send(notificationTopic, notification.username(), notification))
                .doOnSuccess(result -> notificationMetrics.notificationSuccess())
                .doOnError(e -> notificationMetrics.notificationFailure())
                .then();
    }
}
