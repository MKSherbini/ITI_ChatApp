package iti.jets.gfive.ui.helpers;

import iti.jets.gfive.common.models.UserDto;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class LoginManager {
    public static final String PHONE_NUMBER = "PHONE_NUMBER" ;
    public static final String PASSWORD = "PASSWORD" ;
    public boolean login(){
        return false;
    }
    public boolean Logout(){
        return false ;
    }
    public boolean Exit(){
        return false ;
    }
    private static void saveCredentials(){
        try (OutputStream output = new FileOutputStream("iti/jets/gfive/configs/config.properties")) {

            Properties prop = new Properties();

            // set the properties value
            prop.setProperty(PHONE_NUMBER, ModelsFactory.getInstance().getCurrentUserModel().getPhoneNumber());
            prop.setProperty(PASSWORD,  ModelsFactory.getInstance().getCurrentUserModel().getPassword());
            prop.setProperty("db.password", "password");

            // save properties to project root folder
            prop.store(output, "The current user credentials  ");

            System.out.println(prop);

        } catch (IOException io) {
            io.printStackTrace();
        }
    }
    private UserDto readCredentials(String phone ){
        return null ;
    }
}
