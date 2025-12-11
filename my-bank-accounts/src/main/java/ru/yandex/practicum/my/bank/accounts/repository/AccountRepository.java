package ru.yandex.practicum.my.bank.accounts.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.my.bank.accounts.model.entity.AccountEnt;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface AccountRepository extends R2dbcRepository<AccountEnt, UUID> {

    @Transactional
    @Query("""
            UPDATE account
            SET balance = balance + :delta,
            updated_at = CURRENT_TIMESTAMP
            WHERE id = :id
            RETURNING *
            """)
    Mono<AccountEnt> updateBalance(
            @Param("id") UUID id,
            @Param("delta") BigDecimal delta
    );
}
