package iti.jets.gfive.common.interfaces;

import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.common.models.UserDto;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientConnectionInter extends Remote {
    void register(UserDto user, NotificationReceiveInter notif) throws RemoteException;
    void unregister(NotificationReceiveInter notif) throws RemoteException;
    void sendMsg(MessageDto msg) throws RemoteException;
    void sendFile(MessageDto msg) throws RemoteException;
    void puplishStatus(UserDto user) throws RemoteException;
    void publishAnnouncement(String announce) throws RemoteException;
}
