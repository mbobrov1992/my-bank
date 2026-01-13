package ru.yandex.practicum.my.bank.transfer.service.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.notifications.NotificationDto;

@Slf4j
@Service
public class NotificationProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String notificationTopic;

    public NotificationProducer(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${kafka.notifications.topic.name}") String notificationTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.notificationTopic = notificationTopic;
    }

    public Mono<Void> notify(NotificationDto notification) {
        return Mono.fromFuture(kafkaTemplate.send(notificationTopic, notification.username(), notification))
                .doOnSuccess(result ->
                        log.debug("Уведомление отправлено: топик {}, ключ {}",
                                notificationTopic, notification.username())
                )
                .doOnError(e -> log.error("Ошибка отправки уведомления: {}", e.getMessage()))
                .then();
    }
}
