package com.bank.transferapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaction_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLog {

    @Id
    private UUID transactionId;

    private UUID accountId;

    private String transactionType;

    private BigDecimal amount;

    private String status;

    private LocalDateTime createdAt;
}