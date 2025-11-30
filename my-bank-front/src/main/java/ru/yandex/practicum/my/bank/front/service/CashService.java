package ru.yandex.practicum.my.bank.front.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashResultDto;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashUpdateDto;
import ru.yandex.practicum.my.bank.front.service.client.CashClient;

@Service
@RequiredArgsConstructor
public class CashService {

    private final CashClient cashClient;

    public Mono<CashResultDto> editCash(CashUpdateDto cashUpdateDto) {
        return cashClient.editCash(cashUpdateDto);
    }
}
