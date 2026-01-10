package com.wanderlanka.rider.insuranceverification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class InsuranceVerificationService {

    private final InsuranceVerificationRepository verificationRepository;
    private final InsuranceVerificationFileRepository fileRepository;

    @Value("${app.upload.base-path:uploads}")
    private String baseUploadPath;

    public InsuranceVerificationService(
            InsuranceVerificationRepository verificationRepository,
            InsuranceVerificationFileRepository fileRepository
    ) {
        this.verificationRepository = verificationRepository;
        this.fileRepository = fileRepository;
    }

    // ==============================
    // UPLOAD INSURANCE IMAGES
    // ==============================
    public void uploadInsuranceImages(
            Long userId,
            MultipartFile frontImage,
            MultipartFile backImage
    ) throws IOException {

        InsuranceVerification verification = verificationRepository
                .findByUserId(userId)
                .orElseGet(() -> {
                    InsuranceVerification v = new InsuranceVerification();
                    v.setUserId(userId);
                    v.setStatus(InsuranceVerificationStatus.NOT_SUBMITTED);
                    return verificationRepository.save(v);
                });

        Path userFolder = Paths.get(
                baseUploadPath,
                "InsuranceVerification",
                "user-" + userId
        );

        Files.createDirectories(userFolder);

        saveFile(
                verification,
                frontImage,
                InsuranceFileSide.FRONT,
                userFolder.resolve("front.jpg")
        );

        saveFile(
                verification,
                backImage,
                InsuranceFileSide.BACK,
                userFolder.resolve("back.jpg")
        );

        verification.setStatus(InsuranceVerificationStatus.PENDING);
        verificationRepository.save(verification);
    }

    private void saveFile(
            InsuranceVerification verification,
            MultipartFile multipartFile,
            InsuranceFileSide side,
            Path filePath
    ) throws IOException {

        if (multipartFile == null || multipartFile.isEmpty()) {
            return;
        }

        Files.write(filePath, multipartFile.getBytes());

        InsuranceVerificationFile fileEntity = fileRepository
                .findByVerificationAndFileSide(verification, side)
                .orElseGet(() -> {
                    InsuranceVerificationFile f = new InsuranceVerificationFile();
                    f.setVerification(verification);
                    f.setFileSide(side);
                    return f;
                });

        fileEntity.setFilePath(filePath.toString());
        fileRepository.save(fileEntity);
    }

    public String getInsuranceStatus(Long userId) {
        return verificationRepository
                .findByUserId(userId)
                .map(v -> v.getStatus().name())
                .orElse("NOT_SUBMITTED");
    }
}
