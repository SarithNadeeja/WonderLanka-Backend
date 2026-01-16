package com.wanderlanka.rider.licenseverification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * Upload driving license images
     * Each upload creates a NEW verification attempt
     */
    public void uploadLicenseImages(
            Long userId,
            MultipartFile frontImage,
            MultipartFile backImage
    ) throws IOException {

        // 1️⃣ Always create a NEW verification attempt
        LicenseVerification verification = new LicenseVerification();
        verification.setUserId(userId);
        verification.setStatus(LicenseVerificationStatus.PENDING);
        verificationRepository.save(verification);

        Long verificationId = verification.getId();

        // 2️⃣ Create verification-specific folder
        Path verificationFolder = Paths.get(
                baseUploadPath,
                "LicenseVerification",
                "user-" + userId,
                "verification-" + verificationId
        );

        Files.createDirectories(verificationFolder);

        // 3️⃣ Save front image
        saveFile(
                verification,
                frontImage,
                LicenseFileSide.FRONT,
                verificationFolder.resolve("front.jpg")
        );

        // 4️⃣ Save back image
        saveFile(
                verification,
                backImage,
                LicenseFileSide.BACK,
                verificationFolder.resolve("back.jpg")
        );
    }

    /**
     * Save a single license image
     * Always creates a NEW DB row
     */
    private void saveFile(
            LicenseVerification verification,
            MultipartFile multipartFile,
            LicenseFileSide side,
            Path filePath
    ) throws IOException {

        if (multipartFile == null || multipartFile.isEmpty()) {
            return;
        }

        // Write file to disk
        Files.write(filePath, multipartFile.getBytes());

        // Create new file record (no overwrite)
        LicenseVerificationFile fileEntity = new LicenseVerificationFile();
        fileEntity.setVerification(verification);
        fileEntity.setFileSide(side);
        fileEntity.setFilePath(filePath.toString());

        fileRepository.save(fileEntity);
    }
}
