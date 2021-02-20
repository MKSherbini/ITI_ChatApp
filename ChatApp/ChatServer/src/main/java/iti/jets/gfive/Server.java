package iti.jets.gfive;

import iti.jets.gfive.common.interfaces.*;
import iti.jets.gfive.server.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public Server(){
        try{
            UserDBCrudInter obj = new UserDBCrudImpl();
            ContactDBCrudInter contactObj = new ContactDBCrudImpl();
            ClientConnectionInter clientConnectionObj = new ClientConnectionImpl();
            NotificationCrudInter notificationCrudObj = new NotificationCrudImpl();
            MessageDBInter messageDBInter = new MessageDBImpl();
            GroupChatInter groupChatInter = new GroupChatImpl();
            int port = 8000;
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("UserDB-CRUD", obj);
            registry.rebind("ContactDB-CRUD", contactObj);
            registry.rebind("ClientConnectionService", clientConnectionObj);
            registry.rebind("Notification-CRUD", notificationCrudObj);
            registry.rebind("MessageDb" ,messageDBInter);
            registry.rebind("GroupchatDb" ,groupChatInter);
            System.out.println("Server is up and running on port " + port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }


}
