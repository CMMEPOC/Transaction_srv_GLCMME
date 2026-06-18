package com.bank.transferapi.service.Impl;

import com.bank.transferapi.dto.*;
import com.bank.transferapi.entity.Account;
import com.bank.transferapi.entity.TransactionLog;
import com.bank.transferapi.exception.*;
import com.bank.transferapi.repository.*;
import com.bank.transferapi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
                account.getCurrentBalance()
        );
    }

    @Override
    public String deposit(DepositRequest request) {
        Account account = accountRepository.findByAccountNumber(
                request.getAccountNumber()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Account not found"));
        account.setCurrentBalance(
                account.getCurrentBalance().add(request.getAmount())
        );
        accountRepository.save(account);
        saveTransaction(account, request.getAmount(), "CREDIT");
        return "Amount deposited successfully";
    }

    @Override
    public String withdraw(WithdrawRequest request) {
        Account account = accountRepository.findByAccountNumber(
                request.getAccountNumber()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Account not found"));
        if (account.getCurrentBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }
        account.setCurrentBalance(
                account.getCurrentBalance().subtract(request.getAmount())
        );
        accountRepository.save(account);
        saveTransaction(account, request.getAmount(), "DEBIT");
        return "Amount withdrawn successfully";
    }

    private void saveTransaction(
            Account account,
            java.math.BigDecimal amount,
            String type
    ) {
        TransactionLog transaction = new TransactionLog();
        transaction.setTransactionId(UUID.randomUUID());
        transaction.setDestinationAccountNumber(String.valueOf(account.getAccountId()));
        transaction.setSourceAccountNumber(String.valueOf(account.getAccountId()));
        transaction.setTransactionType(type);
        transaction.setAmount(amount);
        transaction.setStatus("SUCCESS");
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setReferenceId("RF1234567");
        transaction.setRemarks("credited/debited");
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public String transfer(TransferRequest request) {
        if (transactionRepository.existsByReferenceId(
                request.getReferenceId())) {
            return "Duplicate request";
        }
        if (request.getAmount().signum() <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
        if (request.getSourceAccountNumber()
                .equals(request.getDestinationAccountNumber())) {
            throw new IllegalArgumentException(
                    "Source and destination accounts cannot be same");
        }
        Account source = accountRepository
                .findByAccountNumber(request.getSourceAccountNumber())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Source account not found"));
        Account destination = accountRepository
                .findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Destination account not found"));
        if (!"ACTIVE".equalsIgnoreCase(source.getStatus())) {
            throw new IllegalArgumentException(
                    "Source account is inactive");
        }
        if (!"ACTIVE".equalsIgnoreCase(destination.getStatus())) {
            throw new IllegalArgumentException(
                    "Destination account is inactive");
        }
        if (source.getCurrentBalance()
                .compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }
        source.setCurrentBalance(
                source.getCurrentBalance()
                        .subtract(request.getAmount()));
        destination.setCurrentBalance(
                destination.getCurrentBalance()
                        .add(request.getAmount()));
        accountRepository.save(source);
        accountRepository.save(destination);
        TransactionLog transaction = new TransactionLog();
        transaction.setTransactionId(UUID.randomUUID());
        transaction.setSourceAccountNumber(
                request.getSourceAccountNumber());
        transaction.setDestinationAccountNumber(
                request.getDestinationAccountNumber());
        transaction.setTransactionType("DEBIT");
        transaction.setAmount(request.getAmount());
        transaction.setBalanceAfter(source.getCurrentBalance());
        transaction.setStatus("SUCCESS");
        transaction.setReferenceId(request.getReferenceId());
        transaction.setRemarks(request.getRemarks());
        transaction.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
        return "Transfer successful";
    }

        @Override
    public TransactionResponse getTransactionById(UUID transactionId) {

        TransactionLog transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new RuntimeException("Transaction not found"));

        return mapToResponse(transaction);
    }

    private TransactionResponse mapToResponse(TransactionLog transaction) {

        TransactionResponse response = new TransactionResponse();

        response.setTransactionId(transaction.getTransactionId());
        response.setSourceAccountNumber(transaction.getSourceAccountNumber());
        response.setDestinationAccountNumber(transaction.getDestinationAccountNumber());
        response.setTransactionType(transaction.getTransactionType());
        response.setAmount(transaction.getAmount());
        response.setBalanceAfter(transaction.getBalanceAfter());
        response.setStatus(transaction.getStatus());
        response.setReferenceId(transaction.getReferenceId());
        response.setRemarks(transaction.getRemarks());
        response.setCreatedAt(transaction.getCreatedAt());
        return response;
    }
}
