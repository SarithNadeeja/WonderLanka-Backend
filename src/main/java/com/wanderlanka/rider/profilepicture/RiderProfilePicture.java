package com.wanderlanka.rider.profilepicture;

import com.wanderlanka.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "rider_profile_picture",
        uniqueConstraints = @UniqueConstraint(columnNames = "user_id")
)
public class RiderProfilePicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String imageUrl;

    private LocalDateTime uploadedAt = LocalDateTime.now();

    public Long getId(){
        return id;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getUploadedAt(){
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt){
        this.uploadedAt = uploadedAt;
    }
}
