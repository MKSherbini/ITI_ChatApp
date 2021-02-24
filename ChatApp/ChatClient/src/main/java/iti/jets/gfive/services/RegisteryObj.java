package iti.jets.gfive.services;

import iti.jets.gfive.common.IPConn;
import iti.jets.gfive.ui.helpers.StageCoordinator;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RegisteryObj {
    private static Registry registryObj = null;

    private RegisteryObj() {
    }

    public static Registry getInstance() {
        if (registryObj == null) {
            try {
                System.setProperty("java.rmi.server.hostname", IPConn.IP);
                registryObj = LocateRegistry.getRegistry(IPConn.IP, 8000);
                return registryObj;
            } catch (RemoteException e) {
                e.printStackTrace();
                StageCoordinator.getInstance().reset();
            }
        }
        return registryObj;
    }
}
