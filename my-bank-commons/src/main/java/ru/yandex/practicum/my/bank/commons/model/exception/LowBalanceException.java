package ru.yandex.practicum.my.bank.commons.model.exception;

public class LowBalanceException extends RuntimeException {

    public LowBalanceException() {
        super("Недостаточно средств");
    }
}
