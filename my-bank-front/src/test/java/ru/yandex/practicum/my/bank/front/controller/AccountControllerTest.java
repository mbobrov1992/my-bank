package ru.yandex.practicum.my.bank.front.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountDto;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountUpdateDto;
import ru.yandex.practicum.my.bank.front.model.AccountViewModel;
import ru.yandex.practicum.my.bank.front.service.AccountService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private AccountService accountService;

    @Test
    void getAccount_success() {
        AccountDto accountDto = new AccountDto(
                UUID.randomUUID(),
                "Иванов",
                "Иван",
                LocalDate.now(),
                BigDecimal.ONE);

        AccountViewModel mockModel = new AccountViewModel();
        mockModel.setCurrentAccount(accountDto);
        mockModel.setOtherAccounts(List.of());

        Mockito.when(accountService.getAccount()).thenReturn(Mono.just(mockModel));

        webTestClient.get()
                .uri("/account")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    byte[] bodyBytes = response.getResponseBody();
                    assertThat(bodyBytes).isNotNull();

                    String body = new String(bodyBytes);
                    assertThat(body).contains(accountDto.firstName());
                });
    }

    @Test
    void editAccount_success() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("lastName", "Петров");
        formData.add("firstName", "Петр");
        formData.add("birthdate", "2000-10-10");

        AccountDto accountDto = new AccountDto(
                UUID.randomUUID(),
                "Петр",
                "Петров",
                LocalDate.now(),
                BigDecimal.ONE);

        AccountViewModel resultModel = new AccountViewModel();
        resultModel.setCurrentAccount(accountDto);
        resultModel.setOtherAccounts(List.of());

        Mockito.when(accountService.editAccount(Mockito.any(AccountUpdateDto.class))).thenReturn(Mono.just(resultModel));

        webTestClient.post()
                .uri("/account")
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    byte[] bodyBytes = response.getResponseBody();
                    assertThat(bodyBytes).isNotNull();

                    String body = new String(bodyBytes);
                    assertThat(body).contains(accountDto.firstName());
                });
    }
}
