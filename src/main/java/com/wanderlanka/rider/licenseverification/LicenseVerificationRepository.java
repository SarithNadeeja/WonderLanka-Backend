package com.wanderlanka.rider.licenseverification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LicenseVerificationRepository extends JpaRepository<LicenseVerification, Long> {
    Optional<LicenseVerification> findByUserId(Long userId);
    List<LicenseVerification> findByStatus(LicenseVerificationStatus status);
    Optional<LicenseVerification> findTopByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<LicenseVerification>
    findTopByUserIdOrderByAttemptNumberDesc(Long userId);

    int countByUserId(Long userId);

    Optional<LicenseVerification> findTopByUserIdOrderByIdDesc(Long userId);


}
