package ru.yandex.practicum.my.bank.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountDto;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountUpdateDto;
import ru.yandex.practicum.my.bank.front.controller.dto.AccountForm;
import ru.yandex.practicum.my.bank.front.model.AccountViewModel;
import ru.yandex.practicum.my.bank.front.service.AccountService;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static ru.yandex.practicum.my.bank.front.util.SessionUtils.getSessionKey;
import static ru.yandex.practicum.my.bank.front.util.SessionUtils.putSessionKey;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/account")
    public Mono<Rendering> getAccount(ServerWebExchange exchange) {
        return accountService.getAccount()
                .flatMap(model -> enrichModel(exchange, model))
                .map(this::renderModel)
                .onErrorResume(ex -> Mono.just(renderFallbackModel(ex)));
    }

    @PostMapping("/account")
    public Mono<Rendering> editAccount(
            ServerWebExchange exchange,
            @ModelAttribute AccountForm account
    ) {
        return account.validate()
                .then(accountService.editAccount(new AccountUpdateDto(
                        account.lastName(),
                        account.firstName(),
                        account.birthdate()
                )))
                .flatMap(model -> putSessionKey(exchange, "info", "Данные аккаунта изменены")
                        .then(enrichModel(exchange, model)))
                .map(this::renderModel);
    }

    private Mono<AccountViewModel> enrichModel(ServerWebExchange exchange, AccountViewModel model) {
        return getSessionKey(exchange, "info")
                .doOnNext(model::setInfo)
                .then(getSessionKey(exchange, "error"))
                .doOnNext(model::setError)
                .thenReturn(model);
    }

    private Rendering renderModel(AccountViewModel model) {
        AccountDto currentAccount = model.getCurrentAccount();

        LocalDate birthDate = currentAccount.birthDate();
        String formattedBirthDate = birthDate == null ? null : birthDate.format(ISO_DATE);

        return Rendering.view("main")
                .modelAttribute("lastName", currentAccount.lastName())
                .modelAttribute("firstName", currentAccount.firstName())
                .modelAttribute("birthdate", formattedBirthDate)
                .modelAttribute("sum", currentAccount.balance())
                .modelAttribute("accounts", model.getOtherAccounts())
                .modelAttribute("info", model.getInfo())
                .modelAttribute("error", model.getError())
                .build();
    }

    private Rendering renderFallbackModel(Throwable ex) {
        return Rendering.view("main")
                .modelAttribute("error", ex.getMessage())
                .build();
    }
}
