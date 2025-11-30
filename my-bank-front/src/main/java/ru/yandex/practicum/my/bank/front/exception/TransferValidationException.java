package ru.yandex.practicum.my.bank.front.exception;

public class TransferValidationException extends FormValidationException {

    public TransferValidationException(String message) {
        super(message);
    }
}
