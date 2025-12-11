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
import ru.yandex.practicum.my.bank.accounts.service.TransferService;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferDto;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferResultDto;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;

@WebFluxTest(TransferController.class)
@Import(TestSecurityConfig.class)
public class TransferControllerTest {

    private static final String USER = "user";
    private static final String SCOPE_ACCOUNTS_TRANSFER_ACCESS = "SCOPE_accounts-transfer-access";
    private static final String SCOPE_UNKNOWN = "SCOPE_unknown";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private TransferService transferService;

    @WithMockUser(username = USER, authorities = SCOPE_ACCOUNTS_TRANSFER_ACCESS)
    @Test
    void transfer() {
        String toUsername = "to_username";

        TransferDto transferDto = new TransferDto(toUsername, BigDecimal.ONE);

        TransferResultDto transferResultDto = new TransferResultDto(
                UUID.randomUUID(), USER, toUsername);

        when(transferService.transfer(USER, transferDto))
                .thenReturn(Mono.just(transferResultDto));

        webTestClient.post()
                .uri("/v1/transfer/" + USER)
                .bodyValue(transferDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(TransferResultDto.class)
                .isEqualTo(transferResultDto);
    }

    @WithMockUser(username = USER, authorities = SCOPE_UNKNOWN)
    @Test
    void transferForbidden() {
        webTestClient.post()
                .uri("/v1/transfer/" + USER)
                .exchange()
                .expectStatus().isForbidden();
    }
}
