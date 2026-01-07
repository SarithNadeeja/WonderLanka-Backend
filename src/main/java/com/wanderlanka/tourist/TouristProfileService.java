package com.wanderlanka.tourist;

import com.wanderlanka.dto.TouristProfileRequest;
import com.wanderlanka.security.JwtService;
import com.wanderlanka.user.User;
import com.wanderlanka.user.UserRepository;
import com.wanderlanka.user.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TouristProfileService {

    private final UserRepository userRepository;
    private final TouristProfileRepository touristProfileRepository;
    private final JwtService jwtService;

    public TouristProfileService(
            UserRepository userRepository,
            TouristProfileRepository touristProfileRepository,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.touristProfileRepository = touristProfileRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public String createProfile(String authHeader, TouristProfileRequest request) {

        String token = authHeader.substring(7);
        Long userId = jwtService.extractUserId(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEmailVerified()) {
            return "Email not verified";
        }

        if (user.getRole() != UserRole.TOURIST) {
            return "Invalid role";
        }

        if (touristProfileRepository.findByUserId(user.getId()).isPresent()) {
            return "Profile already exists";
        }

        TouristProfile profile = new TouristProfile(
                user,
                request.getFirstName(),
                request.getLastName(),
                request.getCountry(),
                request.getWhatsapp()
        );

        touristProfileRepository.save(profile);

        return "Tourist profile created";
    }
}
