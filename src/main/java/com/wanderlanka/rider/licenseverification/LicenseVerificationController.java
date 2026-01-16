package com.wanderlanka.rider.licenseverification;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/rider/license-verification")
public class LicenseVerificationController {

    private final LicenseVerificationService service;
    private final LicenseVerificationRepository verificationRepository;

    public LicenseVerificationController(
            LicenseVerificationService service,
            LicenseVerificationRepository verificationRepository
    ) {
        this.service = service;
        this.verificationRepository = verificationRepository;
    }

    /**
     * Upload driving license images
     * Always creates a NEW verification attempt
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadLicense(
            Authentication authentication,
            @RequestParam("front") MultipartFile front,
            @RequestParam("back") MultipartFile back
    ) {
        try {
            // ⚠️ Assumes authentication.getName() = userId
            Long userId = Long.parseLong(authentication.getName());

            service.uploadLicenseImages(userId, front, back);

            return ResponseEntity.ok("License uploaded successfully");

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Upload failed: " + e.getMessage());
        }
    }

    /**
     * Get LATEST license verification status for rider
     */
    @GetMapping("/status")
    public ResponseEntity<String> getLicenseStatus(Authentication authentication) {

        Long userId = Long.parseLong(authentication.getName());

        return verificationRepository
                .findTopByUserIdOrderByCreatedAtDesc(userId)
                .map(v -> ResponseEntity.ok(v.getStatus().name()))
                .orElse(ResponseEntity.ok("NOT_SUBMITTED"));
    }
}
