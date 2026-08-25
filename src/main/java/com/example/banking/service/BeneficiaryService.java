package com.example.banking.service;

import com.example.banking.dto.BeneficiaryRequest;
import com.example.banking.dto.BeneficiaryResponse;
import com.example.banking.entity.Beneficiary;
import com.example.banking.enums.BeneficiaryStatus;
import com.example.banking.exception.BusinessException;
import com.example.banking.exception.ResourceNotFoundException;
import com.example.banking.repository.BeneficiaryRepository;
import com.example.rbac.service.RbacService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final RbacService rbacService;

    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository, RbacService rbacService) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.rbacService = rbacService;
    }

    public BeneficiaryResponse createBeneficiary(BeneficiaryRequest request) {
        rbacService.check(request.getCreatedBy(), "CREATE_BENEFICIARY", null);

        Beneficiary beneficiary = Beneficiary.builder()
                .beneficiaryName(request.getBeneficiaryName())
                .accountNumber(request.getAccountNumber())
                .bankName(request.getBankName())
                .ifscCode(request.getIfscCode())
                .createdBy(request.getCreatedBy())
                .status(BeneficiaryStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        return BeneficiaryResponse.from(beneficiaryRepository.save(beneficiary));
    }

    public List<BeneficiaryResponse> getBeneficiariesForUser(Long userId) {
        return beneficiaryRepository.findByCreatedBy(userId)
                .stream()
                .map(BeneficiaryResponse::from)
                .collect(Collectors.toList());
    }

    public BeneficiaryResponse getBeneficiary(Long beneficiaryId) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found"));
        return BeneficiaryResponse.from(beneficiary);
    }

    public BeneficiaryResponse updateBeneficiary(Long beneficiaryId, BeneficiaryRequest request) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found"));

        beneficiary.setBeneficiaryName(request.getBeneficiaryName());
        beneficiary.setAccountNumber(request.getAccountNumber());
        beneficiary.setBankName(request.getBankName());
        beneficiary.setIfscCode(request.getIfscCode());
        beneficiary.setUpdatedAt(LocalDateTime.now());

        return BeneficiaryResponse.from(beneficiaryRepository.save(beneficiary));
    }

    public BeneficiaryResponse activateBeneficiary(Long beneficiaryId) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found"));
        beneficiary.setStatus(BeneficiaryStatus.ACTIVE);
        beneficiary.setUpdatedAt(LocalDateTime.now());
        return BeneficiaryResponse.from(beneficiaryRepository.save(beneficiary));
    }

    public BeneficiaryResponse deactivateBeneficiary(Long beneficiaryId) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found"));
        beneficiary.setStatus(BeneficiaryStatus.INACTIVE);
        beneficiary.setUpdatedAt(LocalDateTime.now());
        return BeneficiaryResponse.from(beneficiaryRepository.save(beneficiary));
    }

    public Beneficiary validateActiveBeneficiary(Long beneficiaryId) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BusinessException("Beneficiary does not exist"));
        if (beneficiary.getStatus() != BeneficiaryStatus.ACTIVE) {
            throw new BusinessException("Beneficiary is inactive");
        }
        return beneficiary;
    }
}
