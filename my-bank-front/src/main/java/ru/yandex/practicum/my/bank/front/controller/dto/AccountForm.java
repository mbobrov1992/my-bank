package ru.yandex.practicum.my.bank.front.controller.dto;

import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.front.exception.AccountValidationException;

import java.time.LocalDate;
import java.time.Period;

public record AccountForm(
        String lastName,
        String firstName,
        LocalDate birthdate
) {
    public Mono<Void> validate() {
        if (lastName == null || lastName.isEmpty() || lastName.isBlank()) {
            return Mono.error(new AccountValidationException("Некорректное значение в поле: Фамилия"));
        }
        if (firstName == null || firstName.isEmpty() || firstName.isBlank()) {
            return Mono.error(new AccountValidationException("Некорректное значение в поле: Имя"));
        }
        if (birthdate == null) {
            return Mono.error(new AccountValidationException("Некорректное значение в поле: Дата рождения"));
        }
        if (Period.between(birthdate, LocalDate.now()).getYears() < 18) {
            return Mono.error(new AccountValidationException("Возраст менее 18 лет"));
        }
        return Mono.empty();
    }
}
