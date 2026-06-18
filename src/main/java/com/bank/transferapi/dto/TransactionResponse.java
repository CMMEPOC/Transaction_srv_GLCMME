package com.bank.transferapi.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionResponse {

    private UUID transactionId;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private String transactionType;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String status;
    private String referenceId;
    private String remarks;
    private LocalDateTime createdAt;
}
