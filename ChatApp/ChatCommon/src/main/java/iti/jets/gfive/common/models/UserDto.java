package iti.jets.gfive.common.models;

import javafx.scene.image.Image;

import java.io.*;
import java.sql.Blob;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;

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
    private ArrayList<UserDto> contacts;
    private transient Image image;
    // private java.sql.Blob image;
    //private byte[] image;
    //private File image ;

    public UserDto() {
    }

    public UserDto(String phoneNumber, String username, String password, String gender, String status, Image image) {
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.status = status;
        this.image = image;
    }

    public UserDto(String phoneNumber, String username, String password, String gender, Date birthDate, Image image) {
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.birthDate = birthDate;
        this.image = image;


    }

    public UserDto(String phoneNumber, String username, String password, String gender, Date birthDate, String country, String email, String bio) {
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.birthDate = birthDate;
        this.country = country;
        this.email = email;
        this.bio = bio;
        this.image = new Image(UserDto.class.getResource("/images/personal.jpg").toString());
    }

    public UserDto(String phoneNumber, Image image) {
        this.phoneNumber = phoneNumber;
        this.image = image;
    }

    public UserDto(String phoneNumber, String username, String status) {
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.status = status;
    }

    public UserDto(String phoneNumber, String status) {
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        var bytes = s.readAllBytes();
        InputStream i = new ByteArrayInputStream(bytes);
//        System.out.println(Arrays.toString(bytes));
        if (bytes.length > 0)
            image = SwingFXUtils.toFXImage(ImageIO.read(i), null);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        //image = new Image(UserDto.class.getResource("/iti/jets/gfive/images/sponge.png").toString());
        if (image != null)
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", s);
        //if (image == null) System.out.println("fimageeeeeeee");
        //if (image != null) System.out.println("gimageeeeeeee");
//        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File("D:/sent.png")); //72kb

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

    public void setContacts(ArrayList<UserDto> contacts) {
        this.contacts = contacts;
    }

    public ArrayList<UserDto> getContacts() {
        return this.contacts;
    }

    public String toString() {
        return ("Phone Number: " + this.phoneNumber + " Username: " + this.username);
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

}
