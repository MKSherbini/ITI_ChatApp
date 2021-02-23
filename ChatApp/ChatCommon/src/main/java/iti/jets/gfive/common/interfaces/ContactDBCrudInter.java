package iti.jets.gfive.common.interfaces;

import iti.jets.gfive.common.models.UserDto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ContactDBCrudInter extends Remote {
    //    ArrayList selectAllContacts(String userId) throws RemoteException;
    int insertContactRecord(String contactId, String currentUserId) throws RemoteException;

    ArrayList<UserDto> getContactsList(String userId) throws RemoteException;
    void updateUserContacts(String userId, UserDto contactInfo) throws RemoteException;
    UserDto getContactInfo(String contactId) throws RemoteException;

    boolean checkActiveChatBot(String contactId, String currentUserId) throws RemoteException;

    void updateActiveChatBot(String contactId, String currentUserId, boolean chatBotState) throws RemoteException;

//    int updateUserRecord(UserDto user) throws RemoteException;
//    int deleteUser(UserDto user) throws RemoteException;
//    boolean checkUserId(String userId) throws RemoteException;
}
