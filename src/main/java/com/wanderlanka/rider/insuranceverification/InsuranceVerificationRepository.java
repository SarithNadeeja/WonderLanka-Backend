package com.wanderlanka.rider.insuranceverification;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InsuranceVerificationRepository extends JpaRepository<InsuranceVerification, Long> {
    Optional<InsuranceVerification> findByUserId(Long userId);
}
