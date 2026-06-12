package com.bank.transferapi.service.impl;

import com.bank.transferapi.dto.*;
import com.bank.transferapi.entity.Account;
import com.bank.transferapi.entity.TransactionLog;
import com.bank.transferapi.exception.*;
import com.bank.transferapi.repository.*;
import com.bank.transferapi.service.AccountService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public BalanceResponse checkBalance(String accountNumber) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found"));

        return new BalanceResponse(
                account.getAccountNumber(),
                account.getBalance()
        );
    }

    @Override
    public String deposit(DepositRequest request) {

        Account account = accountRepository.findByAccountNumber(
                request.getAccountNumber()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Account not found"));

        account.setBalance(
                account.getBalance().add(request.getAmount())
        );

        accountRepository.save(account);

        saveTransaction(account, request.getAmount(), "DEPOSIT");

        return "Amount deposited successfully";
    }

    @Override
    public String withdraw(WithdrawRequest request) {

        Account account = accountRepository.findByAccountNumber(
                request.getAccountNumber()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Account not found"));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }

        account.setBalance(
                account.getBalance().subtract(request.getAmount())
        );

        accountRepository.save(account);

        saveTransaction(account, request.getAmount(), "WITHDRAW");

        return "Amount withdrawn successfully";
    }

    private void saveTransaction(
            Account account,
            java.math.BigDecimal amount,
            String type
    ) {

        TransactionLog transaction = new TransactionLog();

        transaction.setTransactionId(UUID.randomUUID());
        transaction.setAccountId(account.getAccountId());
        transaction.setTransactionType(type);
        transaction.setAmount(amount);
        transaction.setStatus("SUCCESS");
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);
    }
}