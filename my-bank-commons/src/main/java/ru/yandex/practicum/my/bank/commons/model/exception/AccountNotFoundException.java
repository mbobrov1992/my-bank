package ru.yandex.practicum.my.bank.commons.model.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String username) {
        super("Не найден аккаунт с id: " + username);
    }
}
