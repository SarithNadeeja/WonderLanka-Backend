package com.wanderlanka.rider.vehicleCommonDetails;

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
public class VehicleCommonDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" ,nullable = false)
    private User user;

    @Column(name = "Vehicle_type")
    private String vehicleType;

    @Column(name = "Vehicle_brand")
    private String vehicleBrand;

    @Column(name = "Vehicle_model")
    private String vehicleModel;

    @Column(name = "Vehicle_Registration_no")
    private String regNo;

    @Column(name = "Manufracture_year")
    private int year;


    public Long getId(){
        return id;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public String getVehicleType(){
        return vehicleType;
    }

    public void setVehicleType(String vehicleType){
        this.vehicleType = vehicleType;
    }

    public String getVehicleBrand(){
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand){
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleModel(){
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel){
        this.vehicleModel = vehicleModel;
    }

    public String getRegNo(){
        return regNo;
    }

    public void setRegNo(String regNo){
        this.regNo = regNo;
    }

    public int getYear(){
        return year;
    }

    public void setYear(int year){
        this.year = year;
    }

}
