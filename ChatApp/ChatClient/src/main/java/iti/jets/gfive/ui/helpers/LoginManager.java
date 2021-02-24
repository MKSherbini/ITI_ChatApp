package iti.jets.gfive.ui.helpers;

import iti.jets.gfive.common.interfaces.*;
import iti.jets.gfive.common.models.GroupDto;
import iti.jets.gfive.common.models.NotificationDto;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.*;
import iti.jets.gfive.ui.controllers.RegisterController;
import iti.jets.gfive.ui.models.CurrentUserModel;
import javafx.scene.image.Image;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LoginManager {
    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    public static final String PASSWORD = "PASSWORD";
    public static final String ACTION_LOGOUT = "ACTION_LOGOUT";
    public static final String ACTION_EXIT = "ACTION_EXIT";

    private String phone, password;
    private static LoginManager loginManager;
    private UserDto userDto = new UserDto();

    //Synchronize only one instance
    public synchronized static LoginManager getInstance() {
        if (loginManager == null) {
            loginManager = new LoginManager();
        }
        return loginManager;
    }

    // this method to read data from properties file and inflate it into phone and password
    private LoginManager() {
        readCredentials();
    }

    // checks that the user was exit or loged out the last time
    // true  : user exited
    // false : user loged out
    public boolean canLogin() {
        if (!(phone == null || phone.equals(""))) {
            ModelsFactory.getInstance().getCurrentUserModel().setPhoneNumber(phone);
        }
        if (!(password == null || password.equals(""))) {
            ModelsFactory.getInstance().getCurrentUserModel().setPassword(password);
            return true;
        }
        return false;
    }

    public void getNotifications(UserDto user) {
        NotificationCrudInter notificationCrudInter = NotificationDBCrudService.getNotificationService();
        try {
            ArrayList<NotificationDto> notificationsList = notificationCrudInter.getNotificationList(user.getPhoneNumber());
            NotificationMsgHandler notificationMsgHandler = NotificationMsgHandler.getInstance();
            notificationMsgHandler.addNotifications(notificationsList);
        } catch (RemoteException e) {
            e.printStackTrace();
            StageCoordinator.getInstance().reset();
            return;
        }
    }

    // This method to intitialize the user data once he opened the app if he was exit at the last time
    public void initCurrentUser() {

        try {
            UserDBCrudInter userServices = UserDBCrudService.getUserService();
            if (userServices == null) return;
            //System.out.println("befor");
            Image image = new Image(RegisterController.class.getResource("/iti/jets/gfive/images/personal.jpg").toString());
            userDto = userServices.selectFromDB(phone, password);
            userDto.setPhoneNumber(phone);
            StageCoordinator.getInstance().registerUser(userDto);
            ContactDBCrudInter contactDBCrudInter = ContactDBCrudService.getContactService();
            ArrayList<UserDto> contacts = null;
            List<GroupDto> groups = null;
            try {
                contacts = contactDBCrudInter.getContactsList(userDto.getPhoneNumber());
            } catch (RemoteException e) {
                e.printStackTrace();
                StageCoordinator.getInstance().reset();
                return;
            }
            ContactsListView c = ContactsListView.getInstance();
            c.fillContacts(contacts); // Sherbini: todo this was null for me, should be handled
            groups = GroupChatService.getGroupChatInter().selectAllGroups(ModelsFactory.getInstance().getCurrentUserModel().getPhoneNumber());
            c.fillGroups(groups);
            getNotifications(userDto);
            ModelsFactory modelsFactory = ModelsFactory.getInstance();
            CurrentUserModel currentUserModel = modelsFactory.getCurrentUserModel();
            currentUserModel.setPhoneNumber(phone);
            currentUserModel.setUsername(userDto.getUsername());
            currentUserModel.setStatus(userDto.getStatus());
            //in case the user did not enter the date in registeration
            Date date = userDto.getBirthDate();
            if (date != null) {
                currentUserModel.setDate(userDto.getBirthDate().toLocalDate());
            }
            currentUserModel.setCountry(userDto.getCountry());
            currentUserModel.setGender(userDto.getGender());
            currentUserModel.setEmail(userDto.getEmail());
            currentUserModel.setPassword(password);
            currentUserModel.setBio(userDto.getBio());
            currentUserModel.setImage(userDto.getImage());
            ClientConnectionService.getClientConnService().puplishStatus(userDto);
        } catch (RemoteException e) {
            e.printStackTrace();
            StageCoordinator.getInstance().reset();
        }

    }


    public boolean login() {
        return false;
    }

    // This method unregister the user
    // save user data
    // remove the password from the current user model to force the user to login again once he logged out
    public void Logout() {
        StageCoordinator.getInstance().unregisterCurrentUser(false);
        ModelsFactory.getInstance().getCurrentUserModel().setPassword("");
        saveCredentials(ACTION_LOGOUT);
    }

    // This method to
    // unregister the user from the server and
    // saves his phone and password to used in the next login to the application .
    public void Exit() {
        StageCoordinator.getInstance().unregisterCurrentUser(true);
        saveCredentials(ACTION_EXIT);
    }

    // this method to write the user data to properties file
    // saves his phone and password to used in the next login to the application
    private void saveCredentials(String action) {
        try (OutputStream output = new FileOutputStream("src/main/resources/config.properties")) {
            Properties prop = new Properties();
            // set the properties value
            prop.setProperty(PHONE_NUMBER, ModelsFactory.getInstance().getCurrentUserModel().getPhoneNumber());
            if (action.equals(ACTION_EXIT)) {
                prop.setProperty(PASSWORD, ModelsFactory.getInstance().getCurrentUserModel().getPassword());
            }
            if (action.equals(ACTION_LOGOUT)) {
                prop.setProperty(PASSWORD, "");
            }
            // save the file to src/main/resources/config.properties
            prop.store(output, "The current user credentials  ");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    // This method to read the saved password and phone number to perform implicit login
    private void readCredentials() {
        Properties prop = null;
        try (InputStream input = LoginManager.class.getResourceAsStream("/config.properties")) {
            prop = new Properties();
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            if (null != prop) {
                phone = prop.getProperty(PHONE_NUMBER);
                password = prop.getProperty(PASSWORD);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
