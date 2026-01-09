package com.wanderlanka.rider.profilepicture;

import com.wanderlanka.user.UserRepository;
import org.springframework.stereotype.Service;
import com.wanderlanka.user.User;

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
}
