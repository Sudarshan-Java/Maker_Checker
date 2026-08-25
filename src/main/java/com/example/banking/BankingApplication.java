package com.example.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.example.audit.repository.AuditRecordRepository;
import com.example.audit.service.AuditService;
import com.example.rbac.repository.RbacRuleRepository;
import com.example.rbac.repository.RbacUserRoleRepository;
import com.example.rbac.service.RbacService;

@SpringBootApplication
@EntityScan(basePackages = {
    "com.example.banking.entity",
    "com.example.audit.entity",
    "com.example.rbac.entity"
})
@EnableJpaRepositories(basePackages = "com.example.banking.repository")
public class BankingApplication {

    @Bean
    RbacService rbacService(RbacUserRoleRepository userRoleRepository,
                            RbacRuleRepository ruleRepository) {
        return new RbacService(userRoleRepository, ruleRepository);
    }

    @Bean
    AuditService auditService(AuditRecordRepository auditRecordRepository) {
        return new AuditService(auditRecordRepository);
    }

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }
}
