package com.example.banking.service;

import com.example.banking.dto.AccountResponse;
import com.example.banking.dto.FundTransferResponse;
import com.example.banking.entity.Account;
import com.example.banking.entity.FundTransfer;
import com.example.banking.exception.ResourceNotFoundException;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.FundTransferRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final FundTransferRepository fundTransferRepository;

    public AccountService(AccountRepository accountRepository, FundTransferRepository fundTransferRepository) {
        this.accountRepository = accountRepository;
        this.fundTransferRepository = fundTransferRepository;
    }

    public List<AccountResponse> getAccountsForUser(Long userId) {
        return accountRepository.findByUserId(userId)
                .stream()
                .map(AccountResponse::from)
                .collect(Collectors.toList());
    }

    public AccountResponse getAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        return AccountResponse.from(account);
    }

    public List<FundTransferResponse> getRecentTransactionsForAccount(Long accountId) {
        List<FundTransfer> transfers = fundTransferRepository.findByDebitAccountIdOrderByCreatedAtDesc(accountId);
        return transfers.stream().map(FundTransferResponse::from).collect(Collectors.toList());
    }
}
