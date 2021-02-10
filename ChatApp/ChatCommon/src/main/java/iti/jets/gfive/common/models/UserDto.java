package iti.jets.gfive.common.models;

import javafx.scene.image.Image;

import java.io.File;
import java.io.Serializable;
import java.sql.Blob;
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
    private Image image;
   // private java.sql.Blob image;
    //private byte[] image;
    //private File image ;

    public UserDto() {
    }

    public UserDto(String phoneNumber, String username, String password, String gender, Date birthDate){
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.birthDate = birthDate;

    }
    public UserDto(String phoneNumber, String username, String password, String gender, Date birthDate , String country , String email ,String bio){
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.birthDate = birthDate;
        this.country=country;
        this.email =email;
        this.bio =bio;
    }
    public UserDto(String phoneNumber,Image image){
        this.phoneNumber = phoneNumber;
        this.image =image;
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

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

}
