package ru.yandex.practicum.my.bank.accounts.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.accounts.service.CashService;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashResultDto;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashUpdateDto;

@RestController
@RequestMapping("/v1/cash")
@RequiredArgsConstructor
public class CashController {

    private final CashService cashService;

    @PostMapping("/{username}")
    public Mono<CashResultDto> editCash(
            @PathVariable String username,
            @RequestBody CashUpdateDto cashUpdateDto
    ) {
        return cashService.editCash(username, cashUpdateDto);
    }
}
