package ru.yandex.practicum.my.bank.transfer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.notifications.NotificationDto;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferResultDto;
import ru.yandex.practicum.my.bank.commons.service.NotificationProducer;

import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Import(NotificationProducer.class)
public class NotificationService {

    private final NotificationProducer notificationProducer;

    public void notifyTransfer(TransferResultDto transfer) {
        String from = transfer.fromUsername();
        String to = transfer.toUsername();

        Mono.just(new NotificationDto(
                        from,
                        String.format("Выполнен перевод '%s' -> '%s'", from, to),
                        ZonedDateTime.now()
                ))
                .flatMap(notificationProducer::notify)
                .doOnSuccess(ignore -> log.debug(
                        "Отправлено уведомление о переводе: {} -> {}", from, to))
                .subscribe();
    }
}
