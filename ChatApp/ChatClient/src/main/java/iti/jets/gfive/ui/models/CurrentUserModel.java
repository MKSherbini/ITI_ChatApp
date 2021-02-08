package iti.jets.gfive.ui.models;

import com.mysql.cj.jdbc.Blob;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.time.LocalDate;

public class CurrentUserModel {

    private final StringProperty phoneNumber = new SimpleStringProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty gender = new SimpleStringProperty();
    private final StringProperty country = new SimpleStringProperty();
    private final StringProperty bio = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> birthDate= new SimpleObjectProperty<>();
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();


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


    public ObjectProperty<LocalDate> dateProperty() {
        return birthDate ;
    }

    public final LocalDate getDate() {
        return birthDate.get();
    }

    public final void setDate(LocalDate date) {
        this.birthDate.set(date);
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

    public ObjectProperty<Image> imageProperty() {
        return image ;
    }

    public final Image getImage() {
        return image.get();
    }

    public final void setImage(Image image) {
        this.image.set(image);
    }
}
