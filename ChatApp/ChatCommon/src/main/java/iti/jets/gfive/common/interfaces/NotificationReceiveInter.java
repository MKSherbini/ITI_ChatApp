package iti.jets.gfive.common.interfaces;

import iti.jets.gfive.common.models.NotificationDto;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationReceiveInter extends Remote {
    void receive(NotificationDto notification) throws RemoteException;
    void updateContactsList() throws RemoteException;
}
