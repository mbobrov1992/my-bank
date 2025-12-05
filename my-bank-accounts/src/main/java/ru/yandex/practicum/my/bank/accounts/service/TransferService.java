package ru.yandex.practicum.my.bank.accounts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.accounts.model.entity.AccountEnt;
import ru.yandex.practicum.my.bank.accounts.repository.AccountRepository;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferDto;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferResultDto;
import ru.yandex.practicum.my.bank.commons.model.exception.AccountNotFoundException;
import ru.yandex.practicum.my.bank.commons.model.exception.LowBalanceException;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepo;

    @Transactional
    public Mono<TransferResultDto> transfer(String fromUsername, TransferDto request) {
        return Mono.zip(getOrThrow(fromUsername),
                        getOrThrow(request.toUsername()))
                .flatMap(tuple ->
                        processTransfer(tuple.getT1(), tuple.getT2(), request.amount()));
    }

    private Mono<AccountEnt> getOrThrow(String username) {
        return accountRepo.findById(UUID.fromString(username))
                .switchIfEmpty(Mono.error(new AccountNotFoundException(username)));
    }

    private Mono<TransferResultDto> processTransfer(AccountEnt fromAccount, AccountEnt toAccount, BigDecimal amount) {
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            return Mono.error(new LowBalanceException());
        }

        String fromUsername = String.valueOf(fromAccount.getId());
        String toUsername = String.valueOf(toAccount.getId());

        return Mono.zip(accountRepo.updateBalance(fromAccount.getId(), amount.negate()),
                        accountRepo.updateBalance(toAccount.getId(), amount))
                .thenReturn(new TransferResultDto(UUID.randomUUID(), fromUsername, toUsername))
                .doOnSuccess(result -> log.info(
                        "Выполнен перевод '{}' -> '{}' на сумму: {}", fromUsername, toUsername, amount))
                .doOnError(ex -> log.error(
                        "Ошибка перевода '{}' -> '{}': {}", fromUsername, toUsername, ex.getMessage()));
    }
}
