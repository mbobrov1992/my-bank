package ru.yandex.practicum.my.bank.transfer.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferDto;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferResultDto;

@Service
@RequiredArgsConstructor
public class TransferClient {

    private static final String V1_TRANSFER = "/v1/transfer";

    private final WebClient accountWebClient;

    public Mono<TransferResultDto> transfer(String fromUsername, TransferDto request) {
        return accountWebClient.post()
                .uri(uriBuilder -> uriBuilder.path(V1_TRANSFER + "/{fromUsername}")
                        .build(fromUsername))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TransferResultDto.class);
    }
}
