package iti.jets.gfive.services;

import iti.jets.gfive.common.interfaces.NotificationCrudInter;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class NotificationDBCrudService {
    private static NotificationCrudInter notificationCrudInter = null;

    private NotificationDBCrudService(){}

    public static NotificationCrudInter getNotificationService(){
        if(notificationCrudInter == null){
            try {
                Registry registry = RegisteryObj.getInstance();
                notificationCrudInter = (NotificationCrudInter) registry.lookup("Notification-CRUD");
                return notificationCrudInter;
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
        return notificationCrudInter;
    }
}
