package com.wanderlanka.auth;

import com.wanderlanka.dto.LoginRequest;
import com.wanderlanka.dto.LoginResponse;
import com.wanderlanka.security.JwtService;
import com.wanderlanka.tourist.TouristProfileRepository;
import com.wanderlanka.user.User;
import com.wanderlanka.mail.EmailService;
import com.wanderlanka.user.UserRepository;
import com.wanderlanka.auth.EmailVerificationToken;
import com.wanderlanka.auth.EmailVerificationRepository;
import com.wanderlanka.user.UserRole;

import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository verificationRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TouristProfileRepository touristProfileRepository;

    @Autowired
    private JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       EmailVerificationRepository verificationRepository,
                       EmailService emailService,
                       PasswordEncoder passwordEncoder,
                       TouristProfileRepository touristProfileRepository){
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.verificationRepository = verificationRepository;
        this.passwordEncoder = passwordEncoder;
        this.touristProfileRepository = touristProfileRepository;
    }


    public boolean register(String email, String rawPassword){

        if(userRepository.findByEmail(email).isPresent()){
            return false;
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(email,encodedPassword);
        userRepository.save(user);

        EmailVerificationToken token = new EmailVerificationToken(user);
        verificationRepository.save(token);

        emailService.sendVerificationEmail(email, token.getToken());

        return true;
    }

    @Transactional
    public String verifyEmail(String tokenValue){

        EmailVerificationToken token =
                verificationRepository.findByToken(tokenValue).orElse(null);

        // CASE 1: token exists
        if (token != null) {

            User user = token.getUser();

            // expired
            if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
                return "Verification Link Expired";
            }

            // already verified
            if (user.isEmailVerified()) {
                verificationRepository.delete(token);
                return "Email Already Verified";
            }


            // normal success
            user.verifyEmail();
            userRepository.save(user);
            verificationRepository.delete(token);

            return "Email Verification Successfully";
        }

        // CASE 2: token does NOT exist
        // user may already be verified
        User user = userRepository.findAll()
                .stream()
                .filter(User::isEmailVerified)
                .findFirst()
                .orElse(null);

        if (user != null) {
            return "Email Already Verified";
        }

        return "Invalid Verification Link";
    }

    @Transactional
    public void assignRoleToUser(User user, UserRole role){
        if(!user.isEmailVerified()){
            throw new IllegalStateException("Email not verified");
        }
        if (user.hasRole() && user.getRole() != UserRole.TOURIST) {
            throw new IllegalStateException("Role already assigned");
        }

        user.assignRole(role);
        userRepository.save(user);
    }


    @Transactional
    public String resendVerificationEmail(String email){

        User user = userRepository.findByEmail(email).orElse(null);

        if(user == null){
            return "User not found";
        }

        if(user.isEmailVerified()){
            return "Email already verified";
        }

        verificationRepository.deleteByUser(user);
        verificationRepository.flush();

        EmailVerificationToken newToken = new EmailVerificationToken(user);
        verificationRepository.save(newToken);

        emailService.sendVerificationEmail(user.getEmail(), newToken.getToken());

        return "Verification email sent";
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getRole() != null ? user.getRole().name() : null
        );

        boolean emailVerified = user.isEmailVerified();
        boolean roleSelected = user.getRole() != null;
        boolean profileCompleted = true;

        if(user.getRole() == UserRole.TOURIST){
            profileCompleted = touristProfileRepository.findByUserId(user.getId()).isPresent();// or however you track it
        }

        return new LoginResponse(
                token,
                emailVerified,
                roleSelected,
                profileCompleted,
                roleSelected ? user.getRole().name() : null
        );
    }




}
