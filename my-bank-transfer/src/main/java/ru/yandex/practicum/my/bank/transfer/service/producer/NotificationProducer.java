package ru.yandex.practicum.my.bank.transfer.service.producer;

import io.micrometer.core.instrument.MeterRegistry;
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
    private final MeterRegistry meterRegistry;

    public NotificationProducer(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${kafka.notifications.topic.name}") String notificationTopic,
            MeterRegistry meterRegistry
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.notificationTopic = notificationTopic;
        this.meterRegistry = meterRegistry;
    }

    public Mono<Void> notify(NotificationDto notification) {
        return Mono.fromFuture(kafkaTemplate.send(notificationTopic, notification.username(), notification))
                .doOnSuccess(result -> {
                            meterRegistry.counter("transfer_notification_success",
                                    "username", notification.username()).increment();
                            log.debug("Уведомление отправлено: топик {}, ключ {}",
                                    notificationTopic, notification.username());
                        }
                )
                .doOnError(e -> {
                    meterRegistry.counter("transfer_notification_failure",
                            "username", notification.username()).increment();
                    log.error("Ошибка отправки уведомления: {}", e.getMessage());
                })
                .then();
    }
}
