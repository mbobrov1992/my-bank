package ru.yandex.practicum.my.bank.front.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferDto;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferResultDto;

@Service
@RequiredArgsConstructor
public class TransferClient {

    private static final String TRANSFER_V1 = "/transfer/v1/transfer";

    private final WebClient apiGatewayClient;

    public Mono<TransferResultDto> transfer(TransferDto dto) {
        return apiGatewayClient.post()
                .uri(TRANSFER_V1)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(TransferResultDto.class);
    }
}
