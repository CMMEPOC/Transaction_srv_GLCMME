package com.bank.transferapi.service;

import com.bank.transferapi.dto.BalanceResponse;
import com.bank.transferapi.dto.DepositRequest;
import com.bank.transferapi.dto.WithdrawRequest;

public interface AccountService {

    BalanceResponse checkBalance(String accountNumber);

    String deposit(DepositRequest request);

    String withdraw(WithdrawRequest request);
}