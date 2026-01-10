package com.wanderlanka.admin.config;

import com.wanderlanka.admin.model.AdminUser;
import com.wanderlanka.admin.repo.AdminUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminDataSeeder implements CommandLineRunner {

    private final AdminUserRepository repo;
    private final PasswordEncoder encoder;

    public AdminDataSeeder(
            AdminUserRepository repo,
            PasswordEncoder encoder
    ) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {

        if (repo.findByUsername("admin").isPresent()) {
            System.out.println("Admin already exists");
            return;
        }

        AdminUser admin = new AdminUser();
        admin.setUsername("admin");
        admin.setPassword(encoder.encode("admin123"));

        repo.save(admin);

        System.out.println("Default admin created");
    }
}

