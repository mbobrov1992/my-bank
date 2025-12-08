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
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferDto;
import ru.yandex.practicum.my.bank.front.service.TransferService;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@WebFluxTest(TransferController.class)
public class TransferControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private TransferService transferService;

    @Test
    void transferAndRedirect() {
        TransferDto expectedDto = new TransferDto("Получатель", new BigDecimal("100.00"));

        when(transferService.transfer(expectedDto)).thenReturn(Mono.empty());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("value", "100.00");
        formData.add("toAccount", "Получатель");

        webTestClient.post()
                .uri("/transfer")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/account");
    }
}
