package iti.jets.gfive.common.interfaces;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationsLabelInter extends Remote {
    void increaseNotificationsNumber() throws RemoteException;
}
