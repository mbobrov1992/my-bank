package ru.yandex.practicum.my.bank.accounts.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.my.bank.accounts.event.CashEditEvent;

@Component
@RequiredArgsConstructor
public class CashEventListener {

    private static final String ACCOUNTS_EDIT_CASH_SUCCESS = "accounts_edit_cash_success";
    private static final String ACCOUNTS_EDIT_CASH_FAILURE = "accounts_edit_cash_failure";

    private final MeterRegistry meterRegistry;

    @EventListener(CashEditEvent.class)
    public void handle(CashEditEvent event) {
        meterRegistry.counter(event.isSuccess() ? ACCOUNTS_EDIT_CASH_SUCCESS : ACCOUNTS_EDIT_CASH_FAILURE)
                .increment();
    }
}
