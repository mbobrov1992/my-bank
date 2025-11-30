package ru.yandex.practicum.my.bank.front.exception;

public class AccountValidationException extends FormValidationException {

    public AccountValidationException(String message) {
        super(message);
    }
}
