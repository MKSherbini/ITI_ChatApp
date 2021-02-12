package iti.jets.gfive.server;

import iti.jets.gfive.common.interfaces.ClientConnectionInter;
import iti.jets.gfive.common.models.UserDto;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientConnectionImpl extends UnicastRemoteObject implements ClientConnectionInter {
    ArrayList<UserDto> clientsPool = new ArrayList<>();
    public ClientConnectionImpl() throws RemoteException {}

    @Override
    public void register(UserDto user) throws RemoteException {
        clientsPool.add(user);
        System.out.println("client " + user.getPhoneNumber() + " is added to the pool");
    }

    @Override
    public void unregister(UserDto user) throws RemoteException {
        clientsPool.remove(user);
        System.out.println("client " + user.getUsername() + "is removed from the pool");
    }
}
