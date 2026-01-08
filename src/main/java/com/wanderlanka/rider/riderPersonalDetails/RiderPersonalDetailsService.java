package com.wanderlanka.rider.riderPersonalDetails;

import com.wanderlanka.user.User;
import com.wanderlanka.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RiderPersonalDetailsService {

    private final RiderPersonalDetailsRepository repository;
    private final UserRepository userRepository;

    public RiderPersonalDetailsService(
            RiderPersonalDetailsRepository repository,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public RiderPersonalDetails save(RiderPersonalDetails incoming) {

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

    public RiderPersonalDetails update(RiderPersonalDetails incoming) {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof Long userId)) {
            throw new RuntimeException("Invalid JWT principal");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RiderPersonalDetails existing = repository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Rider details not found"));

        // 🔁 update fields only
        existing.setFirstName(incoming.getFirstName());
        existing.setLastName(incoming.getLastName());
        existing.setGender(incoming.getGender());
        existing.setBirthday(incoming.getBirthday());

        return repository.save(existing);
    }

}
