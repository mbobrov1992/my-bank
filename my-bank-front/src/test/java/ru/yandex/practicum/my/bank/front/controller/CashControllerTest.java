package ru.yandex.practicum.my.bank.front.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashUpdateDto;
import ru.yandex.practicum.my.bank.commons.model.enums.cash.CashAction;
import ru.yandex.practicum.my.bank.front.service.CashService;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@WebFluxTest(CashController.class)
public class CashControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CashService cashService;

    @Test
    void editCashAndRedirect() {
        CashUpdateDto expectedDto = new CashUpdateDto(CashAction.PUT, new BigDecimal("100.00"));

        when(cashService.editCash(expectedDto)).thenReturn(Mono.empty());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("action", "PUT");
        formData.add("value", "100.00");

        webTestClient.post()
                .uri("/cash")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/account");
    }
}
