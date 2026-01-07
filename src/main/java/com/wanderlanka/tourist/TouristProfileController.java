package com.wanderlanka.tourist;

import com.wanderlanka.dto.TouristProfileRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onboarding")
@CrossOrigin(origins = "http://localhost:3000")
public class TouristProfileController {

    private final TouristProfileService touristProfileService;

    public TouristProfileController(TouristProfileService touristProfileService) {
        this.touristProfileService = touristProfileService;
    }

    @PostMapping("/tourist-profile")
    public ResponseEntity<String> createProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TouristProfileRequest request
    ) {
        return ResponseEntity.ok(
                touristProfileService.createProfile(authHeader, request)
        );
    }

}
