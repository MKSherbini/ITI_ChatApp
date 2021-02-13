package iti.jets.gfive.ui.helpers;

import iti.jets.gfive.common.interfaces.NotificationReceiveInter;
import iti.jets.gfive.common.interfaces.NotificationsLabelInter;
import iti.jets.gfive.common.models.NotificationDto;
import javafx.application.Platform;
import javafx.scene.control.Label;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class NotificationsLabel extends UnicastRemoteObject implements NotificationReceiveInter {
    private static NotificationsLabel notificationsLabel = null;
    private Label notificationLabelId;

    private NotificationsLabel() throws RemoteException {
        super();
    }

    public synchronized static NotificationsLabel getInstance(){
        if(notificationsLabel == null){
            try {
                System.out.println("creating new obj ");
                return notificationsLabel = new NotificationsLabel();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
        System.out.println("getting the obj");
        return notificationsLabel;
    }

    public void setNotificationLabel(Label notificationLabelId){
        this.notificationLabelId = notificationLabelId;
        //System.out.println(notificationLabelId + "setting notification label");

    }

//    public void increaseNotificationsNumber(){
////        int notificationsNumber = Integer.parseInt(this.notificationLabelId.getText());
////        notificationsNumber+=1;
////        System.out.println(notificationsNumber);
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                int notificationsNumber = Integer.parseInt(notificationLabelId.getText());
//                notificationsNumber+=1;
//                notificationLabelId.setText(notificationsNumber+"");
//            }
//        });
//    }

    @Override
    public void receive(NotificationDto notification) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int notificationsNumber = Integer.parseInt(notificationLabelId.getText());
                notificationsNumber+=1;
                notificationLabelId.setText(notificationsNumber+"");
            }
        });
    }


}
