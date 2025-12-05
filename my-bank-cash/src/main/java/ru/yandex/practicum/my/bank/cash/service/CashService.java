package ru.yandex.practicum.my.bank.cash.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.cash.service.client.AccountClient;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashResultDto;
import ru.yandex.practicum.my.bank.commons.model.dto.cash.CashUpdateDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class CashService {

    private final AccountClient accountClient;

    public Mono<CashResultDto> editCash(String username, CashUpdateDto request) {
        return accountClient.editCash(username, request);
    }
}
