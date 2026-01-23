package ru.yandex.practicum.my.bank.accounts.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TransferEvent extends ApplicationEvent {

    private final String fromUsername;
    private final String toUsername;
    private final boolean success;

    public TransferEvent(Object source, String fromUsername, String toUsername, boolean success) {
        super(source);
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.success = success;
    }
}
