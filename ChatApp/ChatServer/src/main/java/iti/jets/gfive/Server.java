package iti.jets.gfive;

import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.server.UserDBCrudImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public Server(){
        try{
            UserDBCrudInter obj = new UserDBCrudImpl();
            Registry registry = LocateRegistry.createRegistry(3000);
            registry.rebind("UserDB-CRUD", obj);
            System.out.println("Done");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }


}
