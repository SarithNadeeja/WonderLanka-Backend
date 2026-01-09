package com.wanderlanka.rider.profilepicture;

import com.wanderlanka.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.wanderlanka.user.User;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class VehicleProfilePictureService {
    private final VehicleProfilePictureRepository repository;
    private final UserRepository userRepository;

    public VehicleProfilePictureService(
            VehicleProfilePictureRepository repository,
            UserRepository userRepository

    ){
        this.repository = repository;
        this.userRepository = userRepository;
    }
    public String saveOrUpdate(Long userId, String imageUrl) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        VehicleProfilePicture picture =
                repository.findByUser(user)
                        .orElse(new VehicleProfilePicture());

        picture.setUser(user);
        picture.setImageUrl(imageUrl);

        repository.save(picture);

        return imageUrl;
    }

    public String getImageUrl(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow();

        return repository.findByUser(user)
                .map(VehicleProfilePicture::getImageUrl)
                .orElse(null);
    }

    @Transactional
    public void delete(Long userId) {

        VehicleProfilePicture picture = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Vehicle image not found"));

        try {
            String projectRoot = System.getProperty("user.dir");

            Path filePath = Path.of(
                    projectRoot,
                    picture.getImageUrl().replaceFirst("^/", "")
            );

            Files.deleteIfExists(filePath);

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete vehicle image file", e);
        }

        repository.delete(picture);
    }

}
