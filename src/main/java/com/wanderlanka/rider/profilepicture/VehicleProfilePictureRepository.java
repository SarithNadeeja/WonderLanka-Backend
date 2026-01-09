package com.wanderlanka.rider.profilepicture;

import com.wanderlanka.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleProfilePictureRepository extends JpaRepository<VehicleProfilePicture, Long> {
    Optional<VehicleProfilePicture> findByUser(User user);
}

