package com.wanderlanka.rider.vehicleCommonDetails;

import com.wanderlanka.user.User;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "vehicle_common_details",
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

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "vehicle_brand")
    private String vehicleBrand;

    @Column(name = "vehicle_model")
    private String vehicleModel;

    @Column(name = "vehicle_registration_no")
    private String regNo;

    @Column(name = "manufracture_year")
    private Integer year;


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

    public Integer getYear(){
        return year;
    }

    public void setYear(Integer year){
        this.year = year;
    }

}
