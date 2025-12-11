package ru.yandex.practicum.my.bank.front.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountDto;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountShortDto;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountUpdateDto;
import ru.yandex.practicum.my.bank.front.model.AccountViewModel;
import ru.yandex.practicum.my.bank.front.service.client.AccountClient;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountClient accountClient;

    public Mono<AccountViewModel> getAccount() {
        return createModel(
                accountClient.getAccount(),
                accountClient.getAccounts()
        );
    }

    public Mono<AccountViewModel> editAccount(AccountUpdateDto updateDto) {
        return createModel(
                accountClient.editAccount(updateDto),
                accountClient.getAccounts()
        );
    }

    private Mono<AccountViewModel> createModel(
            Mono<AccountDto> currentAccount,
            Flux<AccountShortDto> otherAccounts
    ) {
        return Mono.zip(
                currentAccount,
                otherAccounts.collectList()
        ).map(tuple -> {
                    AccountViewModel model = new AccountViewModel();
                    model.setCurrentAccount(tuple.getT1());
                    model.setOtherAccounts(tuple.getT2());
                    return model;
                }
        );
    }
}
