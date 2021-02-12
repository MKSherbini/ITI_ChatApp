package iti.jets.gfive.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;

public interface NotificationCrudInter extends Remote {
    int insertNotification(String content, String senderId, Date date, boolean completed) throws RemoteException;
}
