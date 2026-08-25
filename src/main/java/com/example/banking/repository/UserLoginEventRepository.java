package com.example.banking.repository;

import com.example.banking.entity.UserLoginEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLoginEventRepository extends JpaRepository<UserLoginEvent, Long> {
    Optional<UserLoginEvent> findByUserIdAndLoginEventId(Long userId, String loginEventId);
}
