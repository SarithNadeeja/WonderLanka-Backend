package com.wanderlanka.rider.licenseverification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LicenseVerificationService {

    private final LicenseVerificationRepository verificationRepository;
    private final LicenseVerificationFileRepository fileRepository;

    @Value("${app.upload.base-path:uploads}")
    private String baseUploadPath;

    public LicenseVerificationService(
            LicenseVerificationRepository verificationRepository,
            LicenseVerificationFileRepository fileRepository
    ) {
        this.verificationRepository = verificationRepository;
        this.fileRepository = fileRepository;
    }

    public void uploadLicenseImages(
            Long userId,
            MultipartFile frontImage,
            MultipartFile backImage
    ) throws IOException {

        // 1️⃣ Get or create verification record
        LicenseVerification verification = verificationRepository
                .findByUserId(userId)
                .orElseGet(() -> {
                    LicenseVerification v = new LicenseVerification();
                    v.setUserId(userId);
                    v.setStatus(LicenseVerificationStatus.NOT_SUBMITTED);
                    return verificationRepository.save(v);
                });

        // 2️⃣ Create user folder
        Path userFolder = Paths.get(
                baseUploadPath,
                "LicenseVerification",
                "user-" + userId
        );

        Files.createDirectories(userFolder);

        // 3️⃣ Save front image
        saveFile(
                verification,
                frontImage,
                LicenseFileSide.FRONT,
                userFolder.resolve("front.jpg")
        );

        // 4️⃣ Save back image
        saveFile(
                verification,
                backImage,
                LicenseFileSide.BACK,
                userFolder.resolve("back.jpg")
        );

        // 5️⃣ Update status → PENDING
        verification.setStatus(LicenseVerificationStatus.PENDING);
        verificationRepository.save(verification);
    }

    private void saveFile(
            LicenseVerification verification,
            MultipartFile multipartFile,
            LicenseFileSide side,
            Path filePath
    ) throws IOException {

        if (multipartFile == null || multipartFile.isEmpty()) {
            return;
        }

        // Overwrite existing file
        Files.write(filePath, multipartFile.getBytes());

        LicenseVerificationFile fileEntity = fileRepository
                .findByVerificationAndFileSide(verification, side)
                .orElseGet(() -> {
                    LicenseVerificationFile f = new LicenseVerificationFile();
                    f.setVerification(verification);
                    f.setFileSide(side);
                    return f;
                });

        fileEntity.setFilePath(filePath.toString());
        fileRepository.save(fileEntity);
    }

    public String getLicenseStatus(Long userId) {
        return verificationRepository
                .findByUserId(userId)
                .map(v -> v.getStatus().name())
                .orElse("NOT_SUBMITTED");
    }

}
