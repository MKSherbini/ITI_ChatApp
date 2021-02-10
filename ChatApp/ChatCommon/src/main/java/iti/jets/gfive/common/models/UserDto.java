package iti.jets.gfive.common.models;

import java.io.Serializable;
import java.sql.Date;

public class UserDto implements Serializable {
    private String phoneNumber;
    private String username;
    private String password;
    private String email;
    private String gender;
    private String country;
    private Date birthDate;
    private String bio;
    private String status;

    public UserDto(String phoneNumber, String username, String password, String gender, Date birthDate){
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.birthDate = birthDate;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
