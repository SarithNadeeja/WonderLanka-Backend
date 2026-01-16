package com.wanderlanka.admin.licenseverification;

import com.wanderlanka.rider.licenseverification.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import com.wanderlanka.rider.licenseverification.LicenseVerificationFile;

import org.springframework.transaction.annotation.Transactional;


@RestController
@RequestMapping("/api/admin/license-verifications")
public class AdminLicenseVerificationController {

    private final LicenseVerificationRepository verificationRepository;
    private final LicenseVerificationFileRepository fileRepository;

    public AdminLicenseVerificationController(
            LicenseVerificationRepository verificationRepository,
            LicenseVerificationFileRepository fileRepository
    ) {
        this.verificationRepository = verificationRepository;
        this.fileRepository = fileRepository;
    }

    @GetMapping("/pending")
    public List<AdminLicenseVerificationDTO> getPending() {
        System.out.println("🔥 ADMIN PENDING ENDPOINT HIT 🔥");

        return verificationRepository
                .findByStatus(LicenseVerificationStatus.PENDING)
                .stream()
                .map(verification -> {

                    // ✅ FIRST: fetch files from DB
                    List<LicenseVerificationFile> dbFiles =
                            fileRepository.findByVerificationId(verification.getId());

                    // 🔍 DEBUG: print what DB actually returns
                    System.out.println("=== VERIFICATION ID: " + verification.getId() + " ===");
                    for (LicenseVerificationFile f : dbFiles) {
                        System.out.println("FILE ID   : " + f.getId());
                        System.out.println("FILE SIDE : " + f.getFileSide());
                        System.out.println("FILE PATH : " + f.getFilePath());
                    }
                    System.out.println("===================================");

                    // ✅ THEN: map to DTOs
                    List<AdminLicenseVerificationDTO.FileDTO> files =
                            dbFiles.stream()
                                    .map(f -> new AdminLicenseVerificationDTO.FileDTO(
                                            f.getId(),
                                            f.getFileSide().name(),
                                            f.getFilePath()
                                    ))
                                    .collect(Collectors.toList());

                    return new AdminLicenseVerificationDTO(
                            verification.getId(),
                            verification.getUserId(),
                            verification.getStatus().name(),
                            files
                    );
                })
                .collect(Collectors.toList());
    }
    @Transactional
    @PutMapping("/{verificationId}/approve")
    public void approveVerification(
            @PathVariable Long verificationId,
            @RequestHeader("Authorization") String authHeader
    ) {
        LicenseVerification verification =
                verificationRepository.findById(verificationId)
                        .orElseThrow(() -> new RuntimeException("Verification not found"));

        verification.setStatus(LicenseVerificationStatus.APPROVED);
        verification.setReviewedAt(java.time.LocalDateTime.now());

        // TODO: extract adminId from JWT later
        verification.setReviewedByAdminId(1L);

        verificationRepository.save(verification);
    }
    @Transactional
    @PutMapping("/{verificationId}/reject")
    public void rejectVerification(
            @PathVariable Long verificationId,
            @RequestBody RejectRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        LicenseVerification verification =
                verificationRepository.findById(verificationId)
                        .orElseThrow(() -> new RuntimeException("Verification not found"));

        verification.setStatus(LicenseVerificationStatus.REJECTED);
        verification.setRejectionReason(request.getReason());
        verification.setReviewedAt(java.time.LocalDateTime.now());

        // TODO: extract adminId from JWT later
        verification.setReviewedByAdminId(1L);

        verificationRepository.save(verification);
    }



}
