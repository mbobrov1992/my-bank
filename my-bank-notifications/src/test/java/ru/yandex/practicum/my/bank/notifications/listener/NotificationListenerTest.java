package ru.yandex.practicum.my.bank.notifications.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import ru.yandex.practicum.my.bank.commons.model.dto.notifications.NotificationDto;
import ru.yandex.practicum.my.bank.notifications.service.INotificationService;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestPropertySource(properties = {
        "kafka.notifications.topic.name=notifications",
        "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
        "spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer"
})
@EmbeddedKafka(
        topics = "${kafka.notifications.topic.name}",
        partitions = 1
)
public class NotificationListenerTest {

    @Value("${kafka.notifications.topic.name}")
    private String topic;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaRegistry;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockitoSpyBean
    private INotificationService notificationService;

    @BeforeEach
    void setUp() {
        kafkaRegistry.getListenerContainers().forEach(container ->
                ContainerTestUtils.waitForAssignment(container, 1));
    }

    @Test
    void writeNotificationToTopic_shouldBeProcessed() {
        String username = UUID.randomUUID().toString();
        String message = UUID.randomUUID().toString();
        ZonedDateTime timestamp = ZonedDateTime.now();

        NotificationDto notification = new NotificationDto(username, message, timestamp);

        kafkaTemplate.send(topic, username, notification)
                .toCompletableFuture().join();

        ArgumentCaptor<NotificationDto> captor = ArgumentCaptor.forClass(NotificationDto.class);
        verify(notificationService, timeout(5000L).times(1))
                .notify(captor.capture());

        NotificationDto actual = captor.getValue();
        assertThat(actual.username()).isEqualTo(username);
        assertThat(actual.message()).isEqualTo(message);
        assertThat(actual.timestamp().toInstant()).isEqualTo(timestamp.toInstant());
    }
}
