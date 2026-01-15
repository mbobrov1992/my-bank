package ru.yandex.practicum.my.bank.accounts.service;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.mock.MockProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.yandex.practicum.my.bank.accounts.model.entity.AccountEnt;
import ru.yandex.practicum.my.bank.accounts.service.producer.NotificationProducer;
import ru.yandex.practicum.my.bank.commons.model.dto.notifications.NotificationDto;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationServiceTest {

    private static final String TOPIC = UUID.randomUUID().toString();

    private MockProducer<String, Object> mockProducer;
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        mockProducer = new MockProducer<>(true, new StringSerializer(), new JsonSerializer<>());

        ProducerFactory<String, Object> producerFactory = new MockProducerFactory<>(() -> mockProducer);
        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        NotificationProducer notificationProducer = new NotificationProducer(kafkaTemplate, TOPIC, new SimpleMeterRegistry());

        notificationService = new NotificationService(notificationProducer);
    }

    @Test
    void notifyAccountUpsert_shouldSendNotification() {
        AccountEnt account = new AccountEnt(UUID.randomUUID());

        notificationService.notifyAccountUpsert(account);

        List<ProducerRecord<String, Object>> history = mockProducer.history();
        assertThat(history).hasSize(1);

        ProducerRecord<String, Object> message = history.getFirst();
        assertThat(message.key()).isEqualTo(String.valueOf(account.getId()));

        NotificationDto notification = (NotificationDto) message.value();
        assertThat(notification.username()).isEqualTo(String.valueOf(account.getId()));
        assertThat(notification.message()).isNotBlank();
        assertThat(notification.timestamp()).isNotNull();
    }
}
