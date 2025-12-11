package ru.yandex.practicum.my.bank.accounts.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.accounts.service.AccountService;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountDto;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountShortDto;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountUpdateDto;

import java.security.Principal;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public Flux<AccountShortDto> getAccounts(
            Mono<Principal> principal,
            @RequestParam boolean excludeCurrent
    ) {
        return principal.map(Principal::getName)
                .flatMapMany(username -> accountService.getAccounts(username, excludeCurrent));
    }

    @GetMapping("/current")
    public Mono<AccountDto> getAccount(Mono<Principal> principal) {
        return principal.map(Principal::getName)
                .flatMap(accountService::getAccount);
    }

    @PostMapping("/current")
    public Mono<AccountDto> editAccount(
            Mono<Principal> principal,
            @RequestBody AccountUpdateDto accountUpdateDto
    ) {
        return principal.map(Principal::getName)
                .flatMap(username -> accountService.editAccount(username, accountUpdateDto));
    }
}
