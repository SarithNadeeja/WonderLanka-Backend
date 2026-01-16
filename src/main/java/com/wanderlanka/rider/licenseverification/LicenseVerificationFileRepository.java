package com.wanderlanka.rider.licenseverification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LicenseVerificationFileRepository extends JpaRepository<LicenseVerificationFile, Long> {

    Optional<LicenseVerificationFile> findByVerificationAndFileSide(
            LicenseVerification verification,
            LicenseFileSide fileSide
    );
    List<LicenseVerificationFile> findByVerificationId(Long verificationId);
}
