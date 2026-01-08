package com.wanderlanka.rider.riderPersonalDetails;

import com.wanderlanka.user.User;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "rider_personal_details",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "user_id")
        }
)
public class RiderPersonalDetails {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id" ,nullable = false)
        private User user;

        @Column(name = "first_name")
        private String firstName;

        @Column(name = "last_name")
        private String lastName;

        @Column(name = "gender")
        private String gender;

        @Column(name = "birthday")
        private LocalDate birthday;

        public Long getId(){
                return id;
        }

        public User getUser(){
                return user;
        }

        public void setUser(User user){
                this.user = user;
        }

        public String getFirstName(){
                return firstName;
        }

        public void setFirstName(String firstName){
                this.firstName = firstName;
        }

        public String getLastName(){
                return lastName;
        }

        public void setLastName(String lastName){
                this.lastName = lastName;
        }

        public String getGender(){
                return gender;
        }

        public void setGender(String gender){
                this.gender = gender;
        }

        public LocalDate getBirthday(){
                return birthday;
        }

        public void setBirthday(LocalDate birthday){
                this.birthday = birthday;
        }

}
