package com.example.banking.service;

import com.example.banking.entity.UserLoginEvent;
import com.example.banking.exception.BusinessException;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.UserLoginEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LoginService {

    private final AccountRepository accountRepository;
    private final UserLoginEventRepository userLoginEventRepository;
    public LoginService(AccountRepository accountRepository,
                        UserLoginEventRepository userLoginEventRepository) {
        this.accountRepository = accountRepository;
        this.userLoginEventRepository = userLoginEventRepository;
    }

    public void login(Long userId) {
        if (accountRepository.findByUserId(userId).isEmpty()) {
            throw new BusinessException("User does not exist");
        }

        String eventId = "LOGIN-" + userId + "-" + LocalDateTime.now().toLocalDate() + "-" + UUID.randomUUID();
        UserLoginEvent event = UserLoginEvent.builder()
                .userId(userId)
                .loginEventId(eventId)
                .loginAt(LocalDateTime.now())
                .build();
        userLoginEventRepository.save(event);

    }
}
