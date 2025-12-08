package ru.yandex.practicum.my.bank.accounts.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.yandex.practicum.my.bank.accounts.config.TestcontainersConfig;
import ru.yandex.practicum.my.bank.accounts.mapper.AccountMapper;
import ru.yandex.practicum.my.bank.accounts.model.entity.AccountEnt;
import ru.yandex.practicum.my.bank.accounts.repository.AccountRepository;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountShortDto;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountUpdateDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@DataR2dbcTest
@Import({TestcontainersConfig.class, AccountMapper.class, AccountService.class})
class AccountServiceTest {

    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AccountService accountService;

    @MockitoBean
    private NotificationService notificationService;

    private UUID testUserId;

    @BeforeEach
    void setup() {
        testUserId = UUID.randomUUID();
    }

    @AfterEach
    void tearDown() {
        accountRepo.deleteAll().block();
    }

    @Test
    void getAccounts_shouldReturnAllExceptCurrent_ifExcludeCurrentTrue() {
        AccountEnt acc1 = new AccountEnt(testUserId);
        AccountEnt acc2 = new AccountEnt(UUID.randomUUID());
        accountRepo.saveAll(Flux.just(acc1, acc2)).blockLast();

        Flux<AccountShortDto> result = accountService.getAccounts(testUserId.toString(), true);

        StepVerifier.create(result)
                .expectNextMatches(dto -> !dto.id().equals(testUserId))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void getAccounts_shouldReturnAll_ifExcludeCurrentFalse() {
        AccountEnt acc1 = new AccountEnt(testUserId);
        AccountEnt acc2 = new AccountEnt(UUID.randomUUID());
        accountRepo.saveAll(Flux.just(acc1, acc2)).blockLast();

        Flux<AccountShortDto> result = accountService.getAccounts(testUserId.toString(), false);

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void getAccount_shouldReturnExistingAccount() {
        AccountEnt saved = accountRepo.save(new AccountEnt(testUserId)).block();

        StepVerifier.create(accountService.getAccount(testUserId.toString()))
                .expectNextMatches(dto -> dto.id().equals(testUserId))
                .verifyComplete();
    }

    @Test
    void getAccount_shouldCreateIfNotExist() {
        StepVerifier.create(accountService.getAccount(testUserId.toString()))
                .expectNextMatches(dto -> dto.id().equals(testUserId) &&
                        dto.balance().equals(BigDecimal.ZERO))
                .verifyComplete();
    }

    @Test
    void editAccount_shouldUpdateAndNotify() {
        AccountEnt original = new AccountEnt(testUserId);
        accountRepo.save(original).block();

        AccountUpdateDto updateDto = new AccountUpdateDto(
                "Иванов", "Иван", LocalDate.of(1980, 1, 1));

        Mockito.doNothing().when(notificationService)
                .notifyAccountUpsert(Mockito.any());

        StepVerifier.create(accountService.editAccount(testUserId.toString(), updateDto))
                .expectNextMatches(dto ->
                        dto.firstName().equals("Иван") &&
                                dto.lastName().equals("Иванов") &&
                                dto.birthDate().equals(LocalDate.of(1980, 1, 1))
                )
                .verifyComplete();

        Mockito.verify(notificationService)
                .notifyAccountUpsert(Mockito.any());
    }
}
