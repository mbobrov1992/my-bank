package ru.yandex.practicum.my.bank.accounts.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.my.bank.accounts.model.entity.AccountEnt;

import java.util.UUID;

@Repository
public interface AccountRepository extends R2dbcRepository<AccountEnt, UUID> {
}
