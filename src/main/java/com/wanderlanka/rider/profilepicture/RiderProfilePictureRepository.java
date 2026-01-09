package com.wanderlanka.rider.profilepicture;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wanderlanka.user.User;

import java.util.Optional;


public interface RiderProfilePictureRepository extends JpaRepository<RiderProfilePicture, Long> {
    Optional<RiderProfilePicture> findByUser(User user);
    Optional<RiderProfilePicture> findByUserId(Long userId);
}
