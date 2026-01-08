package com.wanderlanka.rider.riderPersonalDetails;

import com.wanderlanka.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RiderPersonalDetailsRepository extends JpaRepository<RiderPersonalDetails, Long> {
    Optional<RiderPersonalDetails> findByUser(User user);
}
