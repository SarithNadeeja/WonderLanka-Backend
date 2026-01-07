package com.wanderlanka.tourist;

import com.wanderlanka.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TouristProfileRepository extends JpaRepository<TouristProfile, Long> {
    Optional<TouristProfile> findByUserId(Long userId);
    boolean existsByUser(User user);
}
