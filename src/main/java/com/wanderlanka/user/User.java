package com.wanderlanka.user;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean emailVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;



    protected User() {
        // JPA only
    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public Long getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public boolean isEmailVerified(){
        return emailVerified;
    }

    public void verifyEmail(){
        this.emailVerified = true;
    }

    public UserRole getRole(){
        return role;
    }

    public boolean hasRole(){
        return this.role != null;
    }

    public void assignRole(UserRole role){
        if(this.role != null){
            throw new IllegalStateException("Role Already assigned");
        }
        this.role = role;
    }

}
