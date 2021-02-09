package iti.jets.gfive.services;

import iti.jets.gfive.common.interfaces.UserDBCrudInter;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class UserDBCrudService {
    private static UserDBCrudInter userDBCrudInter = null;

    private UserDBCrudService(){}

    public static UserDBCrudInter getUserService(){
        if(userDBCrudInter == null){
            Registry registry = null;
            try {
                registry = LocateRegistry.getRegistry(3000);
                userDBCrudInter = (UserDBCrudInter) registry.lookup("UserDB-CRUD");
                return userDBCrudInter;
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
        return userDBCrudInter;
    }
}
