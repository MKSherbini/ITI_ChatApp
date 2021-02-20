package iti.jets.gfive.common.interfaces;

import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.common.models.UserDto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientConnectionInter extends Remote {
    void register(UserDto user, NotificationReceiveInter notif) throws RemoteException;
    void unregister(NotificationReceiveInter notif) throws RemoteException;
    void sendMsg(MessageDto msg) throws RemoteException;
    void addMemberToGroupChat(String number) throws RemoteException;
    void RemoveMemeberFromChatGroup(String number) throws RemoteException;
    void createGroupInAllMemebers(String groupname , List<String> list)throws RemoteException;
}
