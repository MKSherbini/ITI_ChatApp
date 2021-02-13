package iti.jets.gfive.server;

import iti.jets.gfive.classes.ConnectedClient;
import iti.jets.gfive.common.interfaces.ClientConnectionInter;
import iti.jets.gfive.common.interfaces.NotificationReceiveInter;
import iti.jets.gfive.common.models.UserDto;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientConnectionImpl extends UnicastRemoteObject implements ClientConnectionInter {
    public static ConcurrentLinkedQueue<ConnectedClient> clientsPool = new ConcurrentLinkedQueue<>();

    public ClientConnectionImpl() throws RemoteException {}

    @Override
    public void register(UserDto user, NotificationReceiveInter notif) throws RemoteException {
        ConnectedClient client = new ConnectedClient(user, notif);
        clientsPool.add(client);
        System.out.println("client " + user.getPhoneNumber() + " is added to the pool");
    }

    @Override
    public void unregister(UserDto user) throws RemoteException {
        clientsPool.forEach(connectedClient -> {
            if(connectedClient.getClient().equals(user))
                clientsPool.remove(connectedClient);
        });
        System.out.println("client " + user.getUsername() + "is removed from the pool");
    }
}
