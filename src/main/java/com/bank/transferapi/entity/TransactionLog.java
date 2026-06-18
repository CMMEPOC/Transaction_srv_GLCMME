package com.bank.transferapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLog {

    @Id
    @Column(name = "transaction_id")
    private UUID transactionId;

    @Column(name = "source_account_number")
    private String sourceAccountNumber;

    @Column(name = "destination_account_number")
    private String destinationAccountNumber;

    @Column(name = "transaction_type")
    private String transactionType;

    private BigDecimal amount;

    @Column(name = "balance_after")
    private BigDecimal balanceAfter;

    private String status;

    @Column(name = "reference_id")
    private String referenceId;

    private String remarks;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
