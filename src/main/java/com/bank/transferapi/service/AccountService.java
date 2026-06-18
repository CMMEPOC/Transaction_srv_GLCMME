package com.bank.transferapi.service;

import com.bank.transferapi.dto.BalanceResponse;
import com.bank.transferapi.dto.DepositRequest;
import com.bank.transferapi.dto.TransferRequest;
import com.bank.transferapi.dto.WithdrawRequest;
import com.bank.transferapi.dto.TransactionResponse;
import java.util.UUID;

public interface AccountService {

    BalanceResponse checkBalance(String accountNumber);

    String deposit(DepositRequest request);

    String withdraw(WithdrawRequest request);

    String transfer(TransferRequest request);

    TransactionResponse getTransactionById(UUID transactionId);
}
