package iti.jets.gfive;

import iti.jets.gfive.common.interfaces.ContactDBCrudInter;
import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.server.ContactDBCrudImpl;
import iti.jets.gfive.server.UserDBCrudImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public Server(){
        try{
            UserDBCrudInter obj = new UserDBCrudImpl();
            ContactDBCrudInter contactObj = new ContactDBCrudImpl();
            int port = 8000;
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("UserDB-CRUD", obj);
            registry.rebind("ContactDB-CRUD", contactObj);
            System.out.println("Server is up and running on port " + port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }


}
