package iti.jets.gfive.ui.helpers;

import iti.jets.gfive.common.models.UserDto;

import java.io.*;
import java.util.Properties;

public class LoginManager {
    public static final String PHONE_NUMBER = "PHONE_NUMBER" ;
    public static final String PASSWORD = "PASSWORD" ;
    public static final String ACTION_LOGOUT = "ACTION_LOGOUT" ;
    public static final String ACTION_EXIT = "ACTION_EXIT" ;

    private String phone , password;
    private static  LoginManager loginManager;

    public static LoginManager getInstance(){
        if (loginManager ==null){
            loginManager = new LoginManager();
        }
        return loginManager;
    }

    private LoginManager (){
        readCredentials();
    }
    public boolean canLogin(){
        if (phone != null && password != null )
            return true ;
        return false;
    }



    public boolean login(){
        return false;
    }
    public void Logout(){
       saveCredentials(ACTION_LOGOUT);
    }
    public void Exit(){
        saveCredentials(ACTION_EXIT);
    }
    private void saveCredentials(String action){
        try (OutputStream output = new FileOutputStream("src/main/resources/config.properties")) {
            Properties prop = new Properties();
            // set the properties value
            prop.setProperty(PHONE_NUMBER, ModelsFactory.getInstance().getCurrentUserModel().getPhoneNumber());
            if (action.equals(ACTION_EXIT)){
                prop.setProperty(PASSWORD,  ModelsFactory.getInstance().getCurrentUserModel().getPassword());
            }
            // save properties to project root folder
            prop.store(output, "The current user credentials  ");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
    private void  readCredentials(){
        Properties prop = null ;
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
           prop = new Properties();
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            if(null != prop){
                phone = prop.getProperty(PHONE_NUMBER);
                password = prop . getProperty(PASSWORD);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
