package ru.yandex.practicum.my.bank.cash.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashResultDto;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashUpdateDto;

@Service
@RequiredArgsConstructor
public class AccountClient {

    private static final String V1_CASH = "/v1/cash";

    private final WebClient accountWebClient;

    public Mono<CashResultDto> editCash(String username, CashUpdateDto request) {
        return accountWebClient.post()
                .uri(uriBuilder -> uriBuilder.path(V1_CASH + "/{username}")
                        .build(username))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CashResultDto.class);
    }
}
