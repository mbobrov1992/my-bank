package ru.yandex.practicum.my.bank.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferDto;
import ru.yandex.practicum.my.bank.front.controller.dto.TransferForm;
import ru.yandex.practicum.my.bank.front.service.TransferService;

import static ru.yandex.practicum.my.bank.front.util.SessionUtils.putSessionKey;

@Controller
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/transfer")
    public Mono<Rendering> transfer(
            ServerWebExchange exchange,
            @ModelAttribute TransferForm transfer
    ) {
        return transfer.validate()
                .then(transferService.transfer(new TransferDto(transfer.toAccount(), transfer.value())))
                .then(putSessionKey(exchange, "info", String.format("Выполнен перевод на сумму: %s руб.", transfer.value())))
                .then(Mono.just(Rendering.redirectTo("/account").build()));
    }
}
