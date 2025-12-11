package ru.yandex.practicum.my.bank.cash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.cash.service.CashService;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashResultDto;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashUpdateDto;

import java.security.Principal;

@RestController
@RequestMapping("/v1/cash")
@RequiredArgsConstructor
public class CashController {

    private final CashService cashService;

    @PostMapping
    public Mono<CashResultDto> editCash(
            Mono<Principal> principal,
            @RequestBody CashUpdateDto request
    ) {
        return principal.map(Principal::getName)
                .flatMap(username -> cashService.editCash(username, request));
    }
}
