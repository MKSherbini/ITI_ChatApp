package iti.jets.gfive.common.interfaces;

import iti.jets.gfive.common.models.UserDto;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientConnectionInter extends Remote {
    void register(UserDto user, NotificationReceiveInter notif) throws RemoteException;
    void unregister(UserDto user) throws RemoteException;
}
