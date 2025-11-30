package ru.yandex.practicum.my.bank.accounts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.accounts.mapper.AccountMapper;
import ru.yandex.practicum.my.bank.accounts.model.entity.AccountEnt;
import ru.yandex.practicum.my.bank.accounts.repository.AccountRepository;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountDto;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountShortDto;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountUpdateDto;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepo;
    private final AccountMapper accountMapper;

    public Flux<AccountShortDto> getAccounts(String username, boolean excludeCurrent) {
        return accountRepo.findAll()
                .filter(account -> !excludeCurrent || !UUID.fromString(username).equals(account.getId()))
                .map(accountMapper::toShortDto)
                .doOnComplete(() -> log.debug("Получен список аккаунтов пользователем: {}", username));
    }

    public Mono<AccountDto> getAccount(String username) {
        return findAccount(username)
                .switchIfEmpty(createAccount(username))
                .map(accountMapper::toDto);
    }

    private Mono<AccountEnt> createAccount(String username) {
        return Mono.just(username)
                .map(UUID::fromString)
                .map(AccountEnt::new)
                .flatMap(accountRepo::save)
                .doOnNext(accountEnt -> log.info("Создан аккаунт пользователя: {}", username))
                .doOnError(throwable -> log.error("Ошибка создания аккаунта пользователя: {}", username));
    }

    public Mono<AccountDto> editAccount(String username, AccountUpdateDto dto) {
        return findAccount(username)
                .map(account -> {
                    account.setFirstName(dto.firstName());
                    account.setLastName(dto.lastName());
                    account.setBirthDate(dto.birthDate());
                    account.setNew(false);
                    return account;
                })
                .flatMap(accountRepo::save)
                .map(accountMapper::toDto);
    }

    private Mono<AccountEnt> findAccount(String username) {
        return accountRepo.findById(UUID.fromString(username))
                .doOnNext(account -> log.debug("Найден аккаунт пользователя: {}", username));
    }
}
