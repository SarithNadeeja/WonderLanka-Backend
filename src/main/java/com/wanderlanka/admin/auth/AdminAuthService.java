package com.wanderlanka.admin.auth;

import com.wanderlanka.admin.model.AdminUser;
import com.wanderlanka.admin.repo.AdminUserRepository;
import com.wanderlanka.admin.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService {

    private final AdminUserRepository repo;
    private final JwtUtil jwt;
    private final PasswordEncoder encoder;

    public AdminAuthService(
            AdminUserRepository repo,
            JwtUtil jwt,
            PasswordEncoder encoder
    ) {
        this.repo = repo;
        this.jwt = jwt;
        this.encoder = encoder;
    }

    public String login(String username, String password) {

        System.out.println("=== ADMIN LOGIN DEBUG ===");
        System.out.println("INPUT username = [" + username + "]");
        System.out.println("INPUT password = [" + password + "]");

        AdminUser admin = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        System.out.println("DB username = [" + admin.getUsername() + "]");
        System.out.println("DB password hash = [" + admin.getPassword() + "]");

        boolean match = encoder.matches(password, admin.getPassword());
        System.out.println("PASSWORD MATCH = " + match);

        if (!match) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwt.generateToken(admin.getUsername());
    }

}
