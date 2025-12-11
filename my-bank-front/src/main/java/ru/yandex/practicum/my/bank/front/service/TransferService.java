package ru.yandex.practicum.my.bank.front.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferDto;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferResultDto;
import ru.yandex.practicum.my.bank.front.service.client.TransferClient;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferClient transferClient;

    public Mono<TransferResultDto> transfer(TransferDto transferDto) {
        return transferClient.transfer(transferDto);
    }
}
