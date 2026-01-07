package com.wanderlanka.auth;

import com.wanderlanka.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);

    void deleteByUser(User User);
}
