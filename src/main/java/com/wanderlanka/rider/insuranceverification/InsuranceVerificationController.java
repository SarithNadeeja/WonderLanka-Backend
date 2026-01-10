package com.wanderlanka.rider.insuranceverification;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/rider/insurance-verification")
public class InsuranceVerificationController {

    private final InsuranceVerificationService service;

    public InsuranceVerificationController(InsuranceVerificationService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadInsurance(
            Authentication authentication,
            @RequestParam("front") MultipartFile front,
            @RequestParam("back") MultipartFile back
    ) {
        try {
            Long userId = Long.parseLong(authentication.getName());

            service.uploadInsuranceImages(userId, front, back);

            return ResponseEntity.ok("Insurance uploaded successfully");

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<String> getInsuranceStatus(Authentication authentication) {

        Long userId = Long.parseLong(authentication.getName());

        String status = service.getInsuranceStatus(userId);

        return ResponseEntity.ok(status);
    }
}
