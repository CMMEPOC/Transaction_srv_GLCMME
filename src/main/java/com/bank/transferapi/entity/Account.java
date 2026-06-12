package com.bank.transferapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    private UUID accountId;

    @Column(unique = true)
    private String accountNumber;

    private String accountHolderName;

    private BigDecimal balance;

    private String status;

    private LocalDateTime createdAt;
}