package ru.yandex.practicum.my.bank.front.exception;

public class CashValidationException extends FormValidationException {

    public CashValidationException(String message) {
        super(message);
    }
}
