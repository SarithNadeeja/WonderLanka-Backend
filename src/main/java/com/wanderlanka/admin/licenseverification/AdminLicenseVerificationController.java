package com.wanderlanka.admin.licenseverification;

import com.wanderlanka.rider.licenseverification.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import com.wanderlanka.rider.licenseverification.LicenseVerificationFile;

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

}
