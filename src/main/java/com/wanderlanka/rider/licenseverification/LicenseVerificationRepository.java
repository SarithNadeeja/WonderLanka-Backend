package com.wanderlanka.rider.licenseverification;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LicenseVerificationRepository extends JpaRepository<LicenseVerification, Long> {
    Optional<LicenseVerification> findByUserId(Long userId);
}
