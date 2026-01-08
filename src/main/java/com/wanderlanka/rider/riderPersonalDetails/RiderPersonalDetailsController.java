package com.wanderlanka.rider.riderPersonalDetails;

import com.wanderlanka.user.User;
import com.wanderlanka.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rider/personal-details")
public class RiderPersonalDetailsController {

    private final RiderPersonalDetailsService service;
    private final RiderPersonalDetailsRepository repository;
    private final UserRepository userRepository;

    public RiderPersonalDetailsController(
            RiderPersonalDetailsService service,
            RiderPersonalDetailsRepository repository,
            UserRepository userRepository
    ) {
        this.service = service;
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public RiderPersonalDetails save(
            @RequestBody RiderPersonalDetails details
    ) {
        return service.save(details);
    }

    // ✅ NEW: GET logged-in rider personal details
    @GetMapping
    public RiderPersonalDetails getMyDetails() {

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
    public RiderPersonalDetails update(
            @RequestBody RiderPersonalDetails details
    ) {
        return service.update(details);
    }

}
