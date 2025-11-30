package ru.yandex.practicum.my.bank.front.exception;

public class FormValidationException extends RuntimeException {

    public FormValidationException(String message) {
        super(message);
    }
}
