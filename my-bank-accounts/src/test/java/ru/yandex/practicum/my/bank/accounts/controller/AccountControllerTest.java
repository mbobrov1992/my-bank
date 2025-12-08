package ru.yandex.practicum.my.bank.accounts.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.accounts.config.TestSecurityConfig;
import ru.yandex.practicum.my.bank.accounts.service.AccountService;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountDto;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountShortDto;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountUpdateDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@WebFluxTest(AccountController.class)
@Import(TestSecurityConfig.class)
class AccountControllerTest {

    private static final String USER = "user";
    private static final String SCOPE_ACCOUNTS_ACCESS = "SCOPE_accounts-access";
    private static final String SCOPE_UNKNOWN = "SCOPE_unknown";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AccountService accountService;

    @WithMockUser(username = USER, authorities = SCOPE_ACCOUNTS_ACCESS)
    @Test
    void getAccounts() {
        List<AccountShortDto> accounts = List.of(
                new AccountShortDto(UUID.randomUUID(), "Иван", "Иванов")
        );

        when(accountService.getAccounts(USER, false))
                .thenReturn(Flux.fromIterable(accounts));

        webTestClient.get()
                .uri("/v1/accounts?excludeCurrent=false")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(AccountShortDto.class)
                .hasSize(1)
                .contains(accounts.getFirst());
    }

    @WithMockUser(username = USER, authorities = SCOPE_UNKNOWN)
    @Test
    void getAccountsForbidden() {
        webTestClient.get()
                .uri("/v1/accounts?excludeCurrent=false")
                .exchange()
                .expectStatus().isForbidden();
    }

    @WithMockUser(username = USER, authorities = SCOPE_ACCOUNTS_ACCESS)
    @Test
    void getAccount() {
        AccountDto account = new AccountDto(
                UUID.randomUUID(), "Иван", "Иванов", LocalDate.now(), BigDecimal.ONE);

        when(accountService.getAccount(USER))
                .thenReturn(Mono.just(account));

        webTestClient.get()
                .uri("/v1/accounts/current")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(AccountDto.class)
                .isEqualTo(account);
    }

    @WithMockUser(username = USER, authorities = SCOPE_UNKNOWN)
    @Test
    void getAccountForbidden() {
        webTestClient.get()
                .uri("/v1/accounts/current")
                .exchange()
                .expectStatus().isForbidden();
    }

    @WithMockUser(username = USER, authorities = SCOPE_ACCOUNTS_ACCESS)
    @Test
    void editAccount() {
        AccountUpdateDto updateDto = new AccountUpdateDto("Иванов", "Иван", LocalDate.now());

        AccountDto account = new AccountDto(
                UUID.randomUUID(), "Иван", "Иванов", LocalDate.now(), BigDecimal.ONE);

        when(accountService.editAccount(USER, updateDto))
                .thenReturn(Mono.just(account));

        webTestClient.post()
                .uri("/v1/accounts/current")
                .bodyValue(updateDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(AccountDto.class)
                .isEqualTo(account);
    }

    @WithMockUser(username = USER, authorities = SCOPE_UNKNOWN)
    @Test
    void editAccountForbidden() {
        webTestClient.post()
                .uri("/v1/accounts/current")
                .exchange()
                .expectStatus().isForbidden();
    }
}
