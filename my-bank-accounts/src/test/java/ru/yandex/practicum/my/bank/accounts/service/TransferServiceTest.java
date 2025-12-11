package ru.yandex.practicum.my.bank.accounts.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;
import ru.yandex.practicum.my.bank.accounts.config.TestcontainersConfig;
import ru.yandex.practicum.my.bank.accounts.model.entity.AccountEnt;
import ru.yandex.practicum.my.bank.accounts.repository.AccountRepository;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferDto;
import ru.yandex.practicum.my.bank.commons.model.exception.LowBalanceException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@Import({TestcontainersConfig.class, TransferService.class})
public class TransferServiceTest {

    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private TransferService transferService;

    private AccountEnt fromAccount;
    private AccountEnt toAccount;

    @BeforeEach
    void setUp() {
        fromAccount = new AccountEnt(UUID.randomUUID());
        fromAccount.setBalance(BigDecimal.valueOf(200));

        toAccount = new AccountEnt(UUID.randomUUID());
        toAccount.setBalance(BigDecimal.valueOf(100));

        accountRepo.saveAll(List.of(fromAccount, toAccount)).collectList().block();
    }

    @AfterEach
    void tearDown() {
        accountRepo.deleteAll().block();
    }

    @Test
    void transfer_shouldSucceed_andUpdateBalances() {
        TransferDto request = new TransferDto(toAccount.getId().toString(), BigDecimal.valueOf(50));

        StepVerifier.create(transferService.transfer(fromAccount.getId().toString(), request))
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.fromUsername()).isEqualTo(fromAccount.getId().toString());
                    assertThat(result.toUsername()).isEqualTo(toAccount.getId().toString());
                    assertThat(result.transactionId()).isNotNull();
                })
                .verifyComplete();

        AccountEnt updatedFrom = accountRepo.findById(fromAccount.getId()).block();
        assertThat(updatedFrom).isNotNull();
        assertThat(updatedFrom.getBalance()).isEqualByComparingTo("150");

        AccountEnt updatedTo = accountRepo.findById(toAccount.getId()).block();
        assertThat(updatedTo).isNotNull();
        assertThat(updatedTo.getBalance()).isEqualByComparingTo("150");
    }

    @Test
    void transfer_shouldFail_whenInsufficientBalance() {
        TransferDto request = new TransferDto(toAccount.getId().toString(), BigDecimal.valueOf(500));

        StepVerifier.create(transferService.transfer(fromAccount.getId().toString(), request))
                .expectErrorSatisfies(ex -> assertThat(ex).isInstanceOf(LowBalanceException.class))
                .verify();

        AccountEnt updatedFrom = accountRepo.findById(fromAccount.getId()).block();
        assertThat(updatedFrom).isNotNull();
        assertThat(updatedFrom.getBalance()).isEqualByComparingTo("200");

        AccountEnt updatedTo = accountRepo.findById(toAccount.getId()).block();
        assertThat(updatedTo).isNotNull();
        assertThat(updatedTo.getBalance()).isEqualByComparingTo("100");
    }
}
