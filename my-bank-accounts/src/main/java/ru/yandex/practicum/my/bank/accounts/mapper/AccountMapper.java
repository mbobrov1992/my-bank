package ru.yandex.practicum.my.bank.accounts.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.my.bank.accounts.model.entity.AccountEnt;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountDto;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountShortDto;

@Component
public class AccountMapper {

    public AccountDto toDto(AccountEnt entity) {
        return new AccountDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getBirthDate(),
                entity.getBalance()
        );
    }

    public AccountShortDto toShortDto(AccountEnt entity) {
        return new AccountShortDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName()
        );
    }
}
