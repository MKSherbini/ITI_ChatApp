package iti.jets.gfive.common.interfaces;

import iti.jets.gfive.common.models.GroupMessagesDto;
import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.common.models.UserDto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientConnectionInter extends Remote {
    void register(UserDto user, NotificationReceiveInter notif) throws RemoteException;
    void unregister(NotificationReceiveInter notif) throws RemoteException;
    void sendMsg(MessageDto msg) throws RemoteException;
    void sendFile(MessageDto msg) throws RemoteException;
    void puplishStatus(UserDto user) throws RemoteException;
    void publishPicture(UserDto user) throws RemoteException;
    void sendGroupMsg(List<String> list , String id , String message ,String name) throws RemoteException;
    void addMemberToGroupChat(String number) throws RemoteException;
    void RemoveMemeberFromChatGroup(String number) throws RemoteException;
    void createGroupInAllMemebers(String groupname , List<String> list , String id)throws RemoteException;
    void sendFileToGroup(GroupMessagesDto msg, List<String> groupMembers) throws RemoteException;
    void publishAnnouncement(String announce) throws RemoteException;
    void publishName(UserDto user) throws RemoteException;
}
