package iti.jets.gfive.services;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RegisteryObj {
    private static Registry registryObj = null;

    private RegisteryObj(){}

    public static Registry getInstance(){
        if(registryObj == null){
            try {
                registryObj = LocateRegistry.getRegistry(8000);
                return registryObj;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return registryObj;
    }
}
