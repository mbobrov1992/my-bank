package ru.yandex.practicum.my.bank.cash.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.notifications.NotificationDto;

@Service
@RequiredArgsConstructor
public class NotificationClient {

    private static final String V1_NOTIFICATION = "/v1/notification";

    private final WebClient notificationWebClient;

    public Mono<Void> notify(NotificationDto notification) {
        return notificationWebClient.post()
                .uri(V1_NOTIFICATION)
                .bodyValue(notification)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
