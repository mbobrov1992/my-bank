package ru.yandex.practicum.my.bank.accounts.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CashEditEvent extends ApplicationEvent {

    private final String username;
    private final boolean success;

    public CashEditEvent(Object source, String username, boolean success) {
        super(source);
        this.username = username;
        this.success = success;
    }
}
