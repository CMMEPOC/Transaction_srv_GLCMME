package com.bank.transferapi.controller;

import com.bank.transferapi.dto.*;
import com.bank.transferapi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/balance/{accountNumber}")
    public BalanceResponse checkBalance(
            @PathVariable String accountNumber) {
        return accountService.checkBalance(accountNumber);
    }

    @PostMapping("/deposit")
    public String deposit(
            @RequestBody DepositRequest request) {
        return accountService.deposit(request);
    }

    @PostMapping("/withdraw")
    public String withdraw(
            @RequestBody WithdrawRequest request) {
        return accountService.withdraw(request);
    }
    
    @PostMapping("/transfer")
    public String transfer(
            @RequestBody TransferRequest request) {
        return accountService.transfer(request);
    }

    @GetMapping("/transactions/{transactionId}")
    public TransactionResponse getTransactionById(
            @PathVariable UUID transactionId) {
        return accountService.getTransactionById(transactionId);
    }
}
