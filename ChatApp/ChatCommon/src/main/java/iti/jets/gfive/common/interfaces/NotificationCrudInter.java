package iti.jets.gfive.common.interfaces;

import iti.jets.gfive.common.models.NotifDBRecord;
import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.common.models.NotificationDto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;

public interface NotificationCrudInter extends Remote {
    NotifDBRecord insertNotification(String content, String senderId,
                                     Date date, boolean completed, String receiverId) throws RemoteException;
    ArrayList<NotificationDto> getNotificationList(String userId) throws RemoteException;
    void sendNotification(NotificationDto notif) throws RemoteException;
    boolean pendingNotification(String senderId, String receiverId) throws RemoteException;
    int updateNotificationStatus(int notifId) throws RemoteException;
    boolean reverseNotification(String senderId, String receiverId) throws RemoteException;
}
