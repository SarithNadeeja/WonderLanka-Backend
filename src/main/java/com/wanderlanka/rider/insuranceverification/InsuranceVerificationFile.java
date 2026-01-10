package com.wanderlanka.rider.insuranceverification;

import com.wanderlanka.rider.licenseverification.LicenseFileSide;
import com.wanderlanka.rider.licenseverification.LicenseVerification;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "insurance_verification_files")
public class InsuranceVerificationFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verification_id", nullable = false)
    private InsuranceVerification verification;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_side", nullable = false)
    private InsuranceFileSide fileSide;

    @Column(nullable = false)
    private String filePath;

    private LocalDateTime uploadedAt;

    // ==============================
    // JPA LIFECYCLE
    // ==============================
    @PrePersist
    public void onCreate() {
        this.uploadedAt = LocalDateTime.now();
    }

    // ==============================
    // GETTERS & SETTERS
    // ==============================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InsuranceVerification getVerification() {
        return verification;
    }

    public void setVerification(InsuranceVerification verification) {
        this.verification = verification;
    }

    public InsuranceFileSide getFileSide() {
        return fileSide;
    }

    public void setFileSide(InsuranceFileSide fileSide) {
        this.fileSide = fileSide;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
