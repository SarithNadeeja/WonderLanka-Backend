package com.wanderlanka.admin.dev;

import com.wanderlanka.admin.model.AdminUser;
import com.wanderlanka.admin.repo.AdminUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dev")
@CrossOrigin
public class AdminDevController {

    private final AdminUserRepository repo;
    private final PasswordEncoder encoder;

    public AdminDevController(
            AdminUserRepository repo,
            PasswordEncoder encoder
    ) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @PostMapping("/create")
    public String createAdmin(
            @RequestParam String username,
            @RequestParam String password
    ) {

        if (repo.findByUsername(username).isPresent()) {
            return "Admin already exists";
        }

        AdminUser admin = new AdminUser();
        admin.setUsername(username);
        admin.setPassword(encoder.encode(password));

        repo.save(admin);

        return "Admin created successfully";
    }
}

