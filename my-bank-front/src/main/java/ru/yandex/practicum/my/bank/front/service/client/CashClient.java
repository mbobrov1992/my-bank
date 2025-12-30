package ru.yandex.practicum.my.bank.front.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashResultDto;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashUpdateDto;

@Service
@RequiredArgsConstructor
public class CashClient {

    private static final String CASH_V1 = "/v1/cash";

    private final WebClient cashWebClient;

    public Mono<CashResultDto> editCash(CashUpdateDto dto) {
        return cashWebClient.post()
                .uri(CASH_V1)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(CashResultDto.class);
    }
}
