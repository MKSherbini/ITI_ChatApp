package iti.jets.gfive.common.interfaces;

import iti.jets.gfive.common.models.UserDto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface UserDBCrudInter extends Remote {
    ArrayList selectFromDB(String sql) throws RemoteException;
    int insertUserRecord(UserDto user) throws RemoteException;
    int updateUserRecord(UserDto user) throws RemoteException;
    int deleteUser(UserDto user) throws RemoteException;
    boolean checkUserId(String userId) throws RemoteException;
}
