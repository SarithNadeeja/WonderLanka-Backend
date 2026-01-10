package com.wanderlanka.rider.insuranceverification;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InsuranceVerificationFileRepository
        extends JpaRepository<InsuranceVerificationFile, Long> {

    Optional<InsuranceVerificationFile> findByVerificationAndFileSide(
            InsuranceVerification verification,
            InsuranceFileSide fileSide
    );
}
