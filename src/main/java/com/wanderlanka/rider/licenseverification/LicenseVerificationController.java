package com.wanderlanka.rider.licenseverification;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/rider/license-verification")
public class LicenseVerificationController {

    private final LicenseVerificationService service;

    public LicenseVerificationController(LicenseVerificationService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadLicense(
            Authentication authentication,
            @RequestParam("front") MultipartFile front,
            @RequestParam("back") MultipartFile back
    ) {
        try {
            // ⚠️ Adjust this based on how you store user ID in JWT
            Long userId = Long.parseLong(authentication.getName());

            service.uploadLicenseImages(userId, front, back);

            return ResponseEntity.ok("License uploaded successfully");

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<String> getLicenseStatus(Authentication authentication) {

        Long userId = Long.parseLong(authentication.getName());

        String status = service.getLicenseStatus(userId);

        return ResponseEntity.ok(status);
    }

}
