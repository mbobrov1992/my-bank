package ru.yandex.practicum.my.bank.accounts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.accounts.model.entity.AccountEnt;
import ru.yandex.practicum.my.bank.accounts.repository.AccountRepository;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashResultDto;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashUpdateDto;
import ru.yandex.practicum.my.bank.commons.model.enums.cash.CashAction;
import ru.yandex.practicum.my.bank.commons.model.exception.AccountNotFoundException;
import ru.yandex.practicum.my.bank.commons.model.exception.LowBalanceException;

import java.math.BigDecimal;
import java.util.UUID;

import static ru.yandex.practicum.my.bank.commons.model.enums.cash.CashAction.GET;

@Slf4j
@Service
@RequiredArgsConstructor
public class CashService {

    private final AccountRepository accountRepo;

    @Transactional
    public Mono<CashResultDto> editCash(String username, CashUpdateDto request) {
        return accountRepo.findById(UUID.fromString(username))
                .switchIfEmpty(Mono.error(new AccountNotFoundException(username)))
                .flatMap(account -> validateBalanceAndUpdate(account, request))
                .map(this::buildResult)
                .doOnSuccess(result -> log.info("Обновлен баланс пользователя: {}", username))
                .doOnError(ex -> log.error("При обновлении баланса пользователя: {} произошла ошибка: {}", username, ex.getMessage()));
    }

    private Mono<AccountEnt> validateBalanceAndUpdate(AccountEnt account, CashUpdateDto request) {
        if (request.cashAction() == GET && account.getBalance().compareTo(request.amount()) < 0) {
            return Mono.error(new LowBalanceException());
        }
        BigDecimal delta = resolveBalanceDelta(request.cashAction(), request.amount());
        return accountRepo.updateBalance(account.getId(), delta);
    }

    private CashResultDto buildResult(AccountEnt account) {
        return new CashResultDto(UUID.randomUUID(), String.valueOf(account.getId()), account.getBalance());
    }

    private BigDecimal resolveBalanceDelta(CashAction action, BigDecimal amount) {
        return action == GET ? amount.negate() : amount;
    }
}
