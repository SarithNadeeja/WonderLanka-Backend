package com.wanderlanka.rider.vehicleCommonDetails;

import com.wanderlanka.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VehicleCommonDetailsRepository extends JpaRepository<VehicleCommonDetails, Long> {
    Optional<VehicleCommonDetails> findByUser(User user);
}
