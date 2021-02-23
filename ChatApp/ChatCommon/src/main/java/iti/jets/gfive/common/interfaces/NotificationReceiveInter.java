package iti.jets.gfive.common.interfaces;

import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.common.models.NotificationDto;
import iti.jets.gfive.common.models.UserDto;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationReceiveInter extends Remote {
    void receive(NotificationDto notification) throws RemoteException;

    void receiveMsg(MessageDto messageDto) throws RemoteException;

    void updateContactsList(UserDto contactInfo) throws RemoteException;

    void receiveFile(MessageDto messageDto) throws RemoteException;

    void updateStatus(UserDto user) throws RemoteException;

    void updatePicture(UserDto user) throws RemoteException;

    void addGroupInMembersList(String groupname, String id) throws RemoteException;

    void receiveGroupMessage(String id, String message, String name) throws RemoteException;

    void receivePing() throws RemoteException;
}
