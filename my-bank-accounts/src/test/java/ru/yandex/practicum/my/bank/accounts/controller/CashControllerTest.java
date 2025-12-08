package ru.yandex.practicum.my.bank.accounts.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.accounts.config.TestSecurityConfig;
import ru.yandex.practicum.my.bank.accounts.service.CashService;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashResultDto;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashUpdateDto;
import ru.yandex.practicum.my.bank.commons.model.enums.cash.CashAction;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;

@WebFluxTest(CashController.class)
@Import(TestSecurityConfig.class)
public class CashControllerTest {

    private static final String USER = "user";
    private static final String SCOPE_ACCOUNTS_CASH_ACCESS = "SCOPE_accounts-cash-access";
    private static final String SCOPE_UNKNOWN = "SCOPE_unknown";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CashService cashService;

    @WithMockUser(username = USER, authorities = SCOPE_ACCOUNTS_CASH_ACCESS)
    @Test
    void editCash() {
        CashUpdateDto updateDto = new CashUpdateDto(CashAction.GET, BigDecimal.ONE);

        CashResultDto cashResultDto = new CashResultDto(
                UUID.randomUUID(), USER, BigDecimal.ONE);

        when(cashService.editCash(USER, updateDto))
                .thenReturn(Mono.just(cashResultDto));

        webTestClient.post()
                .uri("/v1/cash/" + USER)
                .bodyValue(updateDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CashResultDto.class)
                .isEqualTo(cashResultDto);
    }

    @WithMockUser(username = USER, authorities = SCOPE_UNKNOWN)
    @Test
    void editCashForbidden() {
        webTestClient.post()
                .uri("/v1/cash/" + USER)
                .exchange()
                .expectStatus().isForbidden();
    }
}
