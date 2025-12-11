package ru.yandex.practicum.my.bank.front.model;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountDto;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountShortDto;

import java.util.List;

@Getter
@Setter
public class AccountViewModel {

    private AccountDto currentAccount;
    private List<AccountShortDto> otherAccounts;
    private String info;
    private String error;
}
