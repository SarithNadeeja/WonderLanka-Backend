package com.wanderlanka.admin.licenseverification;

import java.util.List;

public class AdminLicenseVerificationDTO {

    private Long verificationId;
    private Long userId;
    private String status;
    private List<FileDTO> files;

    public static class FileDTO {
        private Long id;
        private String fileSide;
        private String filePath;

        public FileDTO(Long id, String fileSide, String filePath) {
            this.id = id;
            this.fileSide = fileSide;
            this.filePath = filePath;
        }

        public Long getId() { return id; }
        public String getFileSide() { return fileSide; }
        public String getFilePath() { return filePath; }
    }

    public AdminLicenseVerificationDTO(
            Long verificationId,
            Long userId,
            String status,
            List<FileDTO> files
    ) {
        this.verificationId = verificationId;
        this.userId = userId;
        this.status = status;
        this.files = files;
    }

    public Long getVerificationId() { return verificationId; }
    public Long getUserId() { return userId; }
    public String getStatus() { return status; }
    public List<FileDTO> getFiles() { return files; }
}
