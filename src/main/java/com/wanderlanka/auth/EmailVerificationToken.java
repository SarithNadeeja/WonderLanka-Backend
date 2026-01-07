package com.wanderlanka.auth;

import com.wanderlanka.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token = UUID.randomUUID().toString();

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @OneToOne
    private User user;

    public EmailVerificationToken(User user){
        this.user = user;
        this.expiryDate = LocalDateTime.now().plusMinutes(15);
    }

    public EmailVerificationToken(){

    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public User getUser() {
        return user;
    }
}
