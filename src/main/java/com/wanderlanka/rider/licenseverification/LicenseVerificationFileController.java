package com.wanderlanka.rider.licenseverification;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/license-verification/file")
public class LicenseVerificationFileController {

    private final LicenseVerificationFileRepository fileRepository;

    public LicenseVerificationFileController(
            LicenseVerificationFileRepository fileRepository
    ) {
        this.fileRepository = fileRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Long id) {

        LicenseVerificationFile file =
                fileRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("License file not found")
                        );

        try {
            // 🔥 Resolve absolute filesystem path
            Path filePath = Paths.get(file.getFilePath()).toAbsolutePath();

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File not readable: " + filePath);
            }

            // 🔍 Detect content type dynamically
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + filePath.getFileName() + "\""
                    )
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }
}
