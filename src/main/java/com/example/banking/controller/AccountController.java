package com.example.banking.controller;

import com.example.banking.dto.AccountResponse;
import com.example.banking.dto.AccountRequest;
import com.example.banking.dto.FundTransferResponse;
import com.example.banking.entity.Account;
import com.example.banking.service.AccountService;
import com.example.banking.repository.AccountRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    public AccountController(AccountService accountService, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    @PostMapping("/accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@Valid @RequestBody AccountRequest request) {
        Account account = Account.builder()
                .accountNumber(request.getAccountNumber())
                .accountType(request.getAccountType())
                .currency(request.getCurrency())
                .balance(request.getBalance())
                .availableBalance(request.getAvailableBalance())
                .status(request.getStatus())
                .userId(request.getUserId())
                .createdAt(java.time.LocalDateTime.now())
                .build();
        return AccountResponse.from(accountRepository.save(account));
    }

    @GetMapping("/accounts/user/{userId}")
    public List<AccountResponse> getAccountsByUser(@PathVariable Long userId) {
        return accountService.getAccountsForUser(userId);
    }

    @GetMapping("/accounts/{accountId}")
    public AccountResponse getAccount(@PathVariable Long accountId) {
        return accountService.getAccount(accountId);
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public List<FundTransferResponse> getRecentTransactionsForAccount(@PathVariable Long accountId) {
        return accountService.getRecentTransactionsForAccount(accountId);
    }
}
