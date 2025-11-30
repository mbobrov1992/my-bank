package ru.yandex.practicum.my.bank.accounts.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Table("account")
public class AccountEnt implements Persistable<UUID> {

    @Id
    @Column("id")
    private UUID id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("birth_date")
    private LocalDate birthDate;

    @Column("balance")
    private BigDecimal balance = BigDecimal.ZERO;

    @Transient
    private boolean isNew = true;

    public AccountEnt(UUID id) {
        this.id = id;
    }
}
