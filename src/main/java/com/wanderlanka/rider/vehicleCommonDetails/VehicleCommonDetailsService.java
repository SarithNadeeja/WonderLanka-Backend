package com.wanderlanka.rider.vehicleCommonDetails;

import com.wanderlanka.user.User;
import com.wanderlanka.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class VehicleCommonDetailsService {
    private final VehicleCommonDetailsRepository repository;
    private final UserRepository userRepository;

    public VehicleCommonDetailsService(
            VehicleCommonDetailsRepository repository,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public VehicleCommonDetails save(VehicleCommonDetails incoming) {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof Long userId)) {
            throw new RuntimeException(
                    "Expected userId (Long) in JWT but got: " + principal.getClass()
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found for id: " + userId)
                );

        incoming.setUser(user);

        return repository.save(incoming);
    }

    public VehicleCommonDetails update(VehicleCommonDetails incoming) {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof Long userId)) {
            throw new RuntimeException("Invalid JWT principal");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        VehicleCommonDetails existing = repository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Rider details not found"));

        // 🔁 update fields only
        existing.setVehicleType(incoming.getVehicleType());
        existing.setVehicleBrand(incoming.getVehicleBrand());
        existing.setVehicleModel(incoming.getVehicleModel());
        existing.setRegNo(incoming.getRegNo());
        existing.setYear(incoming.getYear());

        return repository.save(existing);
    }
}
