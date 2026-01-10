package com.wanderlanka.rider.licenseverification;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "license_verification_files")
public class LicenseVerificationFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verification_id", nullable = false)
    private LicenseVerification verification;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_side", nullable = false)
    private LicenseFileSide fileSide;

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

    public LicenseVerification getVerification() {
        return verification;
    }

    public void setVerification(LicenseVerification verification) {
        this.verification = verification;
    }

    public LicenseFileSide getFileSide() {
        return fileSide;
    }

    public void setFileSide(LicenseFileSide fileSide) {
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
