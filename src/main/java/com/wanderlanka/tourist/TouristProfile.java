package com.wanderlanka.tourist;

import com.wanderlanka.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "tourist_profiles")
public class TouristProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String country;

    @Column
    private String whatsapp;


    protected TouristProfile(){

    }

    public TouristProfile(User user, String firstName, String lastName, String country, String whatsapp){
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.whatsapp = whatsapp;

    }

    public Long getId(){
        return id;
    }

    public User getUser(){
        return user;
    }
}
