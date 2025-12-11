package ru.yandex.practicum.my.bank.front.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(MainController.class)
class MainControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testRedirectRoot() {
        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/account");
    }
}
