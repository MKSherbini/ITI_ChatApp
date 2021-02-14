package iti.jets.gfive.common.interfaces;

import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.common.models.NotificationDto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;

public interface NotificationCrudInter extends Remote {
    int insertNotification(String content, String senderId,
                           Date date, boolean completed, String receiverId) throws RemoteException;
    ArrayList<NotificationDto> getNotificationList(String userId) throws RemoteException;
    void sendNotification(String userId) throws RemoteException;

}
