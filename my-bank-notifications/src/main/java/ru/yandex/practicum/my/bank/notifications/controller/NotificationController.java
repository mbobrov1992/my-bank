package ru.yandex.practicum.my.bank.notifications.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.notifications.NotificationDto;
import ru.yandex.practicum.my.bank.notifications.service.INotificationService;

@RestController
@RequestMapping("/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final INotificationService notificationService;

    @PostMapping
    public Mono<Void> notify(@RequestBody NotificationDto notification) {
        return notificationService.notify(notification);
    }
}
