package com.example.banking.controller;

import com.example.rbac.dto.AssignRoleRequest;
import com.example.rbac.dto.CreateRoleRequest;
import com.example.rbac.dto.CreateRuleRequest;
import com.example.rbac.entity.RbacRole;
import com.example.rbac.entity.RbacRule;
import com.example.rbac.entity.RbacUserRole;
import com.example.rbac.repository.RbacRoleRepository;
import com.example.rbac.repository.RbacRuleRepository;
import com.example.rbac.repository.RbacUserRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rbac")
public class RbacController {

    private final RbacRoleRepository roleRepository;
    private final RbacUserRoleRepository userRoleRepository;
    private final RbacRuleRepository ruleRepository;

    public RbacController(RbacRoleRepository roleRepository,
                          RbacUserRoleRepository userRoleRepository,
                          RbacRuleRepository ruleRepository) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.ruleRepository = ruleRepository;
    }

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public RbacRole createRole(@RequestBody CreateRoleRequest request) {
        return roleRepository.findByRoleName(request.getRoleName())
                .orElseGet(() -> {
                    RbacRole role = new RbacRole();
                    role.setRoleName(request.getRoleName());
                    return roleRepository.save(role);
                });
    }

    @PostMapping("/user-roles")
    @ResponseStatus(HttpStatus.CREATED)
    public RbacUserRole assignRole(@RequestBody AssignRoleRequest request) {
        RbacUserRole userRole = new RbacUserRole();
        userRole.setUserId(request.getUserId());
        userRole.setRoleName(request.getRoleName());
        return userRoleRepository.save(userRole);
    }

    @GetMapping("/user-roles/{userId}")
    public List<RbacUserRole> getUserRoles(@PathVariable Long userId) {
        return userRoleRepository.findByUserId(userId);
    }

    @GetMapping("/rules")
    public List<RbacRule> getRules() {
        return ruleRepository.findAllByOrderByIdAsc();
    }

    @PostMapping("/rules")
    @ResponseStatus(HttpStatus.CREATED)
    public RbacRule createRule(@RequestBody CreateRuleRequest request) {
        RbacRule rule = new RbacRule();
        rule.setRoleName(request.getRoleName());
        rule.setPermission(request.getPermission());
        rule.setOperator(request.getOperator());
        rule.setLimitAmount(request.getLimitAmount());
        rule.setActive(true);
        return ruleRepository.save(rule);
    }
}