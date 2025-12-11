package ru.yandex.practicum.my.bank.transfer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferDto;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferResultDto;
import ru.yandex.practicum.my.bank.transfer.service.TransferService;

import java.security.Principal;

@RestController
@RequestMapping("/v1/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public Mono<TransferResultDto> transfer(
            Mono<Principal> principal,
            @RequestBody TransferDto request
    ) {
        return principal.map(Principal::getName)
                .flatMap(fromUsername -> transferService.transfer(fromUsername, request));
    }
}
