package com.wanderlanka.rider.profilepicture;

import com.wanderlanka.user.UserRepository;
import org.springframework.stereotype.Service;
import com.wanderlanka.user.User;

@Service
public class RiderProfilePictureService {
    private final RiderProfilePictureRepository repository;
    private final UserRepository userRepository;

    public RiderProfilePictureService(
            RiderProfilePictureRepository repository,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public String saveOrUpdate(Long userId, String imageUrl) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RiderProfilePicture picture =
                repository.findByUser(user)
                        .orElse(new RiderProfilePicture());

        picture.setUser(user);
        picture.setImageUrl(imageUrl);

        repository.save(picture);

        return imageUrl;
    }

    public String getImageUrl(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow();

        return repository.findByUser(user)
                .map(RiderProfilePicture::getImageUrl)
                .orElse(null);
    }
}
