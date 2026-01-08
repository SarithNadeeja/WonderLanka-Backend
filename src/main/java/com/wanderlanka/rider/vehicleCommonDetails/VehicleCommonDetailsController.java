package com.wanderlanka.rider.vehicleCommonDetails;

import com.wanderlanka.user.User;
import com.wanderlanka.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rider/vehicle-common-details")
public class VehicleCommonDetailsController {
    private final VehicleCommonDetailsService service;
    private final VehicleCommonDetailsRepository repository;
    private final UserRepository userRepository;

    public VehicleCommonDetailsController(
            VehicleCommonDetailsService service,
            VehicleCommonDetailsRepository repository,
            UserRepository userRepository
    ) {
        this.service = service;
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public VehicleCommonDetails save(
            @RequestBody VehicleCommonDetails details
    ) {
        return service.save(details);
    }

    // ✅ NEW: GET logged-in rider personal details
    @GetMapping
    public VehicleCommonDetails getMyDetails() {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof Long userId)) {
            throw new RuntimeException("Invalid JWT principal");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return repository.findByUser(user).orElse(null);
    }

    @PutMapping
    public VehicleCommonDetails update(
            @RequestBody VehicleCommonDetails details
    ) {
        return service.update(details);
    }
}
