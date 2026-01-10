package com.wanderlanka.admin.auth;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/auth")
@CrossOrigin
public class AdminAuthController {

    private final AdminAuthService service;

    public AdminAuthController(AdminAuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AdminLoginRequest req) {
        String token = service.login(req.getUsername(), req.getPassword());
        return Map.of("token", token);
    }
}

