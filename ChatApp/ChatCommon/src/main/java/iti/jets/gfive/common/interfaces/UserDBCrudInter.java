package iti.jets.gfive.common.interfaces;

import iti.jets.gfive.common.models.UserDto;
import javafx.scene.image.Image;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface UserDBCrudInter extends Remote {
    UserDto selectFromDB(String phoneNumber) throws RemoteException;

    UserDto selectFromDB(String phoneNumber , String password) throws RemoteException;
    int insertUserRecord(UserDto user) throws RemoteException;
    int updateUserRecord(UserDto user) throws RemoteException;
    int updateUserPhoto(UserDto user) throws RemoteException;
    int updateUserStatus(UserDto user) throws RemoteException;
    Image selectUserImage(String number) throws RemoteException;
    int deleteUser(UserDto user) throws RemoteException;
    boolean checkUserId(String userId) throws RemoteException;
}
