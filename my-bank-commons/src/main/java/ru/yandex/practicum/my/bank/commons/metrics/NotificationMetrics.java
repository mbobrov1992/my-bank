package ru.yandex.practicum.my.bank.commons.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationMetrics {

    private static final String NOTIFICATION_SUCCESS = "notification_success";
    private static final String NOTIFICATION_FAILURE = "notification_failure";

    private final MeterRegistry meterRegistry;

    public void notificationSuccess() {
        meterRegistry.counter(NOTIFICATION_SUCCESS).increment();
    }

    public void notificationFailure() {
        meterRegistry.counter(NOTIFICATION_FAILURE).increment();
    }
}
