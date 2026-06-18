package com.bank.transferapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    private String status;

    private LocalDateTime createdAt;
}
