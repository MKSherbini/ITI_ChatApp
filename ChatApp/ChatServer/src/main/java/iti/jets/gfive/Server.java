package iti.jets.gfive;

import iti.jets.gfive.common.interfaces.*;
import iti.jets.gfive.server.*;
import javafx.event.ActionEvent;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {
    private static Server instance;
    private final int defaultPort = 8000;
    private final List<Remote> servicesList;
    private boolean isServerRunning;

    public static synchronized Server getInstance() {
        if (instance == null) instance = new Server();
        return instance;
    }

    private Server() {
        servicesList = new ArrayList<>();
    }

    public boolean toggleServerRunning() {
        if (isServerRunning) {
            stopServer();
        } else {
            startServer();
        }
        return isServerRunning;
    }

    public void startServer() {
        if (isServerRunning) return;
        startServer(defaultPort);
        isServerRunning = true;
    }

    public void stopServer() {
        if (!isServerRunning) return;
        stopServer(defaultPort);
        isServerRunning = false;
    }

    private void startServer(int port) {
        try {
            UserDBCrudInter obj = new UserDBCrudImpl();
            servicesList.add(obj);
            ContactDBCrudInter contactObj = new ContactDBCrudImpl();
            servicesList.add(contactObj);
            ClientConnectionInter clientConnectionObj = new ClientConnectionImpl();
            servicesList.add(clientConnectionObj);
            NotificationCrudInter notificationCrudObj = new NotificationCrudImpl();
            servicesList.add(notificationCrudObj);
            MessageDBInter messageDBInter = new MessageDBImpl();
            servicesList.add(messageDBInter);
            GroupChatInter groupChatInter = new GroupChatImpl();
            servicesList.add(groupChatInter);

            Registry registry = null;
            try {
                registry = LocateRegistry.createRegistry(port);
                System.out.println("Created registry on port " + port);
            } catch (RemoteException e) {
                registry = LocateRegistry.getRegistry(port);
                System.out.println("Got registry on port " + port);
            }

            registry.rebind("UserDB-CRUD", obj);
            registry.rebind("ContactDB-CRUD", contactObj);
            registry.rebind("ClientConnectionService", clientConnectionObj);
            registry.rebind("Notification-CRUD", notificationCrudObj);
            registry.rebind("MessageDb", messageDBInter);
            registry.rebind("GroupchatDb", groupChatInter);
            System.out.println("Server is up and running on port " + port);
            System.out.println("registry.list() = " + Arrays.toString(registry.list()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void stopServer(int port) {
        try {
            servicesList.forEach(o ->
                    {
                        try {
                            UnicastRemoteObject.unexportObject(o, true);
                        } catch (NoSuchObjectException e) {
                            e.printStackTrace();
                        }
                    }
            );
            servicesList.clear();

            Registry registry = LocateRegistry.getRegistry(port);
            Arrays.stream(registry.list()).forEach(s -> {
                try {
                    registry.unbind(s);
                } catch (RemoteException | NotBoundException e) {
                    e.printStackTrace();
                }
            });

            System.out.println("registry.list() = " + Arrays.toString(registry.list()));
            System.out.println("Server is not up and running on port " + port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        getInstance().startServer();
    }


}
