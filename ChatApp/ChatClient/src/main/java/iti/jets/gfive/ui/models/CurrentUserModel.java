package iti.jets.gfive.ui.models;

import iti.jets.gfive.common.models.UserDto;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.sql.Blob;
import java.sql.Date;
import java.time.LocalDate;

public class CurrentUserModel {

    private final StringProperty phoneNumber = new SimpleStringProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty gender = new SimpleStringProperty();
    private final StringProperty country = new SimpleStringProperty();
    private final StringProperty bio = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> birthDate= new SimpleObjectProperty<>();
    private ObjectProperty<Blob> BlobProperty = new SimpleObjectProperty<>();


    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public String getBio() {
        return bio.get();
    }

    public StringProperty bioProperty() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio.set(bio);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
    public Image getImage() {
        return imageProperty.get();
    }

    public void setImage(Image image) {
        this.imageProperty.set(image);
    }
      
    public ObjectProperty<Image> imageProperty() {
        return imageProperty ;
    }
    public ObjectProperty<LocalDate> dateProperty() {
        return birthDate ;
    }

    public final LocalDate getDate() {
        return birthDate.get();
    }

    public final void setDate(LocalDate date) {
        this.birthDate.set(date);
    }

}
