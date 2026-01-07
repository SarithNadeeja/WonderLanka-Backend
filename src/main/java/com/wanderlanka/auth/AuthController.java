package com.wanderlanka.auth;
import com.wanderlanka.dto.*;
import com.wanderlanka.security.JwtService;
import com.wanderlanka.tourist.TouristProfileRepository;
import com.wanderlanka.user.User;

import com.wanderlanka.user.UserRepository;
import com.wanderlanka.user.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TouristProfileRepository touristProfileRepository;


    public AuthController(AuthService authService,
                          UserRepository userRepository,
                          JwtService jwtService,
                          TouristProfileRepository touristProfileRepository) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.jwtService = jwtService;
        this.touristProfileRepository = touristProfileRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){

        boolean registered = authService.register(
                request.getEmail(),
                request.getPassword()
        );
        if(!registered){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email Already Exist");
        }

        return ResponseEntity.ok("Registration successfully");
    }

    @GetMapping("/verify")
    public String verify(@RequestParam String token) {

        return authService.verifyEmail(token);
    }

    @PostMapping("/onboarding/role")
    public ResponseEntity<?> assignRole(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody RoleRequest request
    ) {
        String token = authHeader.substring(7);
        Long userId = jwtService.extractUserId(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        authService.assignRoleToUser(user, UserRole.valueOf(request.getRole()));

        // 🔥 GENERATE NEW JWT WITH ROLE
        String newToken = jwtService.generateToken(
                user.getId(),
                user.getRole().name()
        );

        return ResponseEntity.ok(
                java.util.Map.of("token", newToken)
        );
    }


    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(
            @RequestBody EmailRequest emailRequest
    ) {
        String result = authService.resendVerificationEmail(
                emailRequest.getEmail()
        );

        if (result.equals("User not found")) {
            return ResponseEntity.badRequest().body(result);
        }

        if (result.equals("Email already verified")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {


        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserStatusResponse> getCurrentUser(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        Long userId = jwtService.extractUserId(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean profileCompleted = false;

        if (user.getRole() == UserRole.TOURIST) {
            profileCompleted = touristProfileRepository
                    .existsByUser(user);
        }

        return ResponseEntity.ok(
                new UserStatusResponse(
                        true,
                        user.getEmail(),
                        user.getRole(),
                        profileCompleted
                )
        );
    }




}
