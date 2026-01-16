package com.wanderlanka.rider.licenseverification;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            // filePath MUST be a FULL FILE PATH
            Path path = Paths.get(file.getFilePath());
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File not readable: " + path);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }
}
