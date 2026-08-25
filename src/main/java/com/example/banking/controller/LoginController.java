package com.example.banking.controller;

import com.example.banking.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        loginService.login(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Login successful.");

        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        result.put("data", data);

        return ResponseEntity.ok(result);
    }
}
