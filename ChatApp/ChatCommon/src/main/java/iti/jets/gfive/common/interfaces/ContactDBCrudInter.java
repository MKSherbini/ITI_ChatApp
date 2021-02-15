package iti.jets.gfive.common.interfaces;

import iti.jets.gfive.common.models.UserDto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ContactDBCrudInter extends Remote {
//    ArrayList selectAllContacts(String userId) throws RemoteException;
    int insertContactRecord(String contactId, String currentUserId) throws RemoteException;
    ArrayList<UserDto> getContactsList(String userId) throws RemoteException;
    void updateUserContacts(String userId) throws RemoteException;
//    int updateUserRecord(UserDto user) throws RemoteException;
//    int deleteUser(UserDto user) throws RemoteException;
//    boolean checkUserId(String userId) throws RemoteException;
}
