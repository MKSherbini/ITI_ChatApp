package iti.jets.gfive.common.interfaces;

import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.common.models.NotificationDto;
import iti.jets.gfive.common.models.UserDto;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationReceiveInter extends Remote {
    void receive(NotificationDto notification) throws RemoteException;
    void receiveMsg(MessageDto messageDto) throws RemoteException;
    void updateContactsList() throws RemoteException;
    void updateStatus(UserDto user) throws RemoteException;
}
