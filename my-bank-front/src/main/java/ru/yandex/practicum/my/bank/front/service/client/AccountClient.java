package ru.yandex.practicum.my.bank.front.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountDto;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountShortDto;
import ru.yandex.practicum.my.bank.commons.model.dto.accounts.AccountUpdateDto;

@Service
@RequiredArgsConstructor
public class AccountClient {

    private static final String ACCOUNTS_V1 = "/accounts/v1/accounts";

    private final WebClient apiGatewayClient;

    public Flux<AccountShortDto> getAccounts() {
        return apiGatewayClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ACCOUNTS_V1)
                        .queryParam("excludeCurrent", true)
                        .build())
                .retrieve()
                .bodyToFlux(AccountShortDto.class);
    }

    public Mono<AccountDto> getAccount() {
        return apiGatewayClient.get()
                .uri(ACCOUNTS_V1 + "/current")
                .retrieve()
                .bodyToMono(AccountDto.class);
    }

    public Mono<AccountDto> editAccount(AccountUpdateDto dto) {
        return apiGatewayClient.post()
                .uri(ACCOUNTS_V1 + "/current")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(AccountDto.class);
    }
}
