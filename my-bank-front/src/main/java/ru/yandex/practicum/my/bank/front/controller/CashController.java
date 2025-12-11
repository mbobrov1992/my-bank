package ru.yandex.practicum.my.bank.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashUpdateDto;
import ru.yandex.practicum.my.bank.commons.model.enums.cash.CashAction;
import ru.yandex.practicum.my.bank.front.controller.dto.CashForm;
import ru.yandex.practicum.my.bank.front.service.CashService;

import java.math.BigDecimal;

import static ru.yandex.practicum.my.bank.front.util.SessionUtils.putSessionKey;

@Controller
@RequiredArgsConstructor
public class CashController {

    private final CashService cashService;

    @PostMapping("/cash")
    public Mono<Rendering> editCash(
            ServerWebExchange exchange,
            @ModelAttribute CashForm cash
    ) {
        return cash.validate()
                .then(cashService.editCash(new CashUpdateDto(cash.action(), cash.value())))
                .then(putSessionKey(exchange, "info", createAlertMessage(cash.action(), cash.value())))
                .then(Mono.just(Rendering.redirectTo("/account").build()));
    }

    private String createAlertMessage(CashAction action, BigDecimal value) {
        if (action == CashAction.PUT) {
            return String.format("Баланс пополнен на сумму: %s руб.", value);
        } else {
            return String.format("Снятие со счета: %s руб.", value);
        }
    }
}
