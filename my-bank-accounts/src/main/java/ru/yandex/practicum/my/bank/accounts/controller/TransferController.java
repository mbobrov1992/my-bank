package ru.yandex.practicum.my.bank.accounts.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.accounts.service.TransferService;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferDto;
import ru.yandex.practicum.my.bank.commons.model.dto.transfer.TransferResultDto;

@RestController
@RequestMapping("/v1/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/{fromUsername}")
    public Mono<TransferResultDto> transfer(
            @PathVariable String fromUsername,
            @RequestBody TransferDto transferDto
    ) {
        return transferService.transfer(fromUsername, transferDto);
    }
}
