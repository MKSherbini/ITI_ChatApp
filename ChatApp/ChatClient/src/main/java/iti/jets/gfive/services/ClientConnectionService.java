package iti.jets.gfive.services;

import iti.jets.gfive.common.interfaces.ClientConnectionInter;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class ClientConnectionService {
    private static ClientConnectionInter clientConnectionInter = null;

    private ClientConnectionService(){}

    public static ClientConnectionInter getClientConnService(){
        if(clientConnectionInter == null){
            Registry registry = RegisteryObj.getInstance();
            try {
                clientConnectionInter = (ClientConnectionInter) registry.lookup("ClientConnectionService");
                return clientConnectionInter;
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        return clientConnectionInter;
    }
}
