package iti.jets.gfive.services;

import iti.jets.gfive.common.interfaces.UserDBCrudInter;
import iti.jets.gfive.ui.helpers.StageCoordinator;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class UserDBCrudService {
    private static UserDBCrudInter userDBCrudInter = null;

    private UserDBCrudService() {
    }

    public static UserDBCrudInter getUserService() {
        if (userDBCrudInter == null) {
            try {
                Registry registry = RegisteryObj.getInstance();
                userDBCrudInter = (UserDBCrudInter) registry.lookup("UserDB-CRUD");
                return userDBCrudInter;
            } catch (Exception e) {
                e.printStackTrace();
                StageCoordinator.getInstance().reset();
            }
        }
        return userDBCrudInter;
//        return null;
    }
}
