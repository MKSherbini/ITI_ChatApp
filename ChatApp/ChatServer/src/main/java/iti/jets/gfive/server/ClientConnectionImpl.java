package iti.jets.gfive.server;

import iti.jets.gfive.classes.ConnectedClient;
import iti.jets.gfive.common.interfaces.ClientConnectionInter;
import iti.jets.gfive.common.interfaces.NotificationReceiveInter;
import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.common.models.NotificationDto;
import iti.jets.gfive.common.models.UserDto;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.sql.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientConnectionImpl extends UnicastRemoteObject implements ClientConnectionInter {
    public static ConcurrentLinkedQueue<ConnectedClient> clientsPool = new ConcurrentLinkedQueue<>();

    public ClientConnectionImpl() throws RemoteException {
    }

    @Override
    public void register(UserDto user, NotificationReceiveInter notif) {
        ConnectedClient client = new ConnectedClient(user, notif);
        clientsPool.add(client);
        System.out.println("client " + user.getPhoneNumber() + " is added to the pool, clients count is " + clientsPool.size());
    }

    @Override
    public void unregister(NotificationReceiveInter notif) throws RemoteException {
        clientsPool.forEach(connectedClient -> {
            if (connectedClient.getReceiveNotif().equals(notif)) {
                clientsPool.remove(connectedClient);
                System.out.println("client " + connectedClient.getClient().getPhoneNumber() + " is removed from the pool");
            }
        });
    }

    @Override
    public void sendMsg(MessageDto msg) throws RemoteException {
        clientsPool.forEach(connectedClient -> {
            if (connectedClient.getClient().getPhoneNumber().equals(msg.getReceiverNumber())) {
                try {
                    connectedClient.getReceiveNotif().receiveMsg(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    clientsPool.remove(connectedClient); // drop him, he prolly died
                }
            }
        });
    }

    @Override
    public void puplishStatus(UserDto user) throws RemoteException {
        if (clientsPool == null || clientsPool.size() < 1)
            return;
        clientsPool.forEach(connectedClient -> {
            if (!connectedClient.getClient().getPhoneNumber().equals(user.getPhoneNumber())) {
                try {
                    connectedClient.getReceiveNotif().updateStatus(user);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void sendFile(MessageDto msg) throws RemoteException {
        clientsPool.forEach(connectedClient -> {
            if(connectedClient.getClient().getPhoneNumber().equals(msg.getReceiverNumber())){
                try {
                    connectedClient.getReceiveNotif().receiveFile(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void publishAnnouncement(String announce) throws RemoteException {
        if (clientsPool == null || clientsPool.size() < 1)
            return;
        clientsPool.forEach(connectedClient -> {

                try {
                    connectedClient.getReceiveNotif().receive(
                            new NotificationDto(1000,announce,"01000000000",
                                    new Date(new java.util.Date().getTime()),
                                    false, connectedClient.getClient().getPhoneNumber()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

        });
    }
}
