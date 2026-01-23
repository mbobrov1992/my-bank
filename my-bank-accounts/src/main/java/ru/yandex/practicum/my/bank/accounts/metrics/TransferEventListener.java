package ru.yandex.practicum.my.bank.accounts.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.my.bank.accounts.event.TransferEvent;

@Component
@RequiredArgsConstructor
public class TransferEventListener {

    private static final String ACCOUNTS_TRANSFER_CASH_SUCCESS = "accounts_transfer_cash_success";
    private static final String ACCOUNTS_TRANSFER_CASH_FAILURE = "accounts_transfer_cash_failure";

    private final MeterRegistry meterRegistry;

    @EventListener(TransferEvent.class)
    public void handle(TransferEvent event) {
        meterRegistry.counter(event.isSuccess() ? ACCOUNTS_TRANSFER_CASH_SUCCESS : ACCOUNTS_TRANSFER_CASH_FAILURE)
                .increment();
    }
}
