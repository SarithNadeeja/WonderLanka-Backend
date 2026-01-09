package com.wanderlanka.rider.profilepicture;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;


@RestController
@RequestMapping("/api/rider/vehicle-profile-picture")
public class VehicleProfilePictureController {

    private final VehicleProfilePictureService service;

    public VehicleProfilePictureController(VehicleProfilePictureService service) {
        this.service = service;
    }

    // ===== UPLOAD =====
    @PostMapping
    public String uploadProfilePicture(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("Uploaded file is empty");
        }

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId;
        if (principal instanceof Long l) {
            userId = l;
        } else if (principal instanceof String s) {
            userId = Long.parseLong(s);
        } else {
            throw new RuntimeException("Invalid JWT principal: " + principal);
        }

        try {
            String projectRoot = System.getProperty("user.dir");

            Path uploadDir = Path.of(
                    projectRoot,
                    "uploads",
                    "profilepictures",
                    "vehicles"
            );
            Files.createDirectories(uploadDir);

            String extension = ".jpg";
            if (file.getContentType() != null &&
                    file.getContentType().contains("png")) {
                extension = ".png";
            }

            Path filePath = uploadDir.resolve(userId + extension);
            file.transferTo(filePath.toFile());

            String publicUrl =
                    "/uploads/profilepictures/vehicles/" + userId + extension;

            // SAVE URL TO DATABASE
            service.saveOrUpdate(userId, publicUrl);

            return publicUrl;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload profile picture");
        }
    }

    // ===== LOAD ON REFRESH =====
    @GetMapping
    public String getMyProfilePicture() {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId;
        if (principal instanceof Long l) {
            userId = l;
        } else if (principal instanceof String s) {
            userId = Long.parseLong(s);
        } else {
            throw new RuntimeException("Invalid JWT principal: " + principal);
        }

        return service.getImageUrl(userId);
    }

    @DeleteMapping
    public void deleteVehicleProfilePicture() {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Long userId =
                (principal instanceof Long l) ? l :
                        (principal instanceof String s) ? Long.parseLong(s) :
                                null;

        if (userId == null) {
            throw new RuntimeException("Invalid JWT principal");
        }

        service.delete(userId);
    }

}
