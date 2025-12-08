package ru.yandex.practicum.my.bank.accounts.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;
import ru.yandex.practicum.my.bank.accounts.config.TestcontainersConfig;
import ru.yandex.practicum.my.bank.accounts.model.entity.AccountEnt;
import ru.yandex.practicum.my.bank.accounts.repository.AccountRepository;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashUpdateDto;
import ru.yandex.practicum.my.bank.commons.model.enums.cash.CashAction;
import ru.yandex.practicum.my.bank.commons.model.exception.AccountNotFoundException;
import ru.yandex.practicum.my.bank.commons.model.exception.LowBalanceException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@Import({TestcontainersConfig.class, CashService.class})
class CashServiceTest {

    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private CashService cashService;

    @AfterEach
    void tearDown() {
        accountRepo.deleteAll().block();
    }

    @Test
    void editCash_shouldUpdateBalance_whenPutAction() {
        UUID userId = UUID.randomUUID();
        AccountEnt acc = new AccountEnt(userId);
        acc.setBalance(BigDecimal.valueOf(100));
        accountRepo.save(acc).block();

        CashUpdateDto request = new CashUpdateDto(CashAction.PUT, BigDecimal.valueOf(50));

        StepVerifier.create(cashService.editCash(userId.toString(), request))
                .assertNext(result -> {
                    assertThat(userId.toString()).isEqualTo(result.username());
                    assertThat(BigDecimal.valueOf(150).doubleValue()).isEqualTo(result.newBalance().doubleValue());
                })
                .verifyComplete();
    }

    @Test
    void editCash_shouldFail_whenAccountNotFound() {
        UUID userId = UUID.randomUUID();
        CashUpdateDto request = new CashUpdateDto(CashAction.GET, BigDecimal.valueOf(50));

        StepVerifier.create(cashService.editCash(userId.toString(), request))
                .expectError(AccountNotFoundException.class)
                .verify();
    }

    @Test
    void editCash_shouldFail_whenLowBalanceForGet() {
        UUID userId = UUID.randomUUID();
        AccountEnt acc = new AccountEnt(userId);
        acc.setBalance(BigDecimal.valueOf(30));
        accountRepo.save(acc).block();

        CashUpdateDto request = new CashUpdateDto(CashAction.GET, BigDecimal.valueOf(50));

        StepVerifier.create(cashService.editCash(userId.toString(), request))
                .expectError(LowBalanceException.class)
                .verify();
    }
}
