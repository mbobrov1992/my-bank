package ru.yandex.practicum.my.bank.commons.model.exception;

public class RemoteServiceException extends RuntimeException {

    public RemoteServiceException(String message) {
        super(message);
    }
}
