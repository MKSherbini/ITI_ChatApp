package iti.jets.gfive.services;

import iti.jets.gfive.common.interfaces.ContactDBCrudInter;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class ContactDBCrudService {
    private static ContactDBCrudInter contactDBCrudInter = null;

    private ContactDBCrudService(){}

    public static ContactDBCrudInter getContactService(){
        if(contactDBCrudInter == null){
            try {
                Registry registry = RegisteryObj.getInstance();
                contactDBCrudInter = (ContactDBCrudInter) registry.lookup("ContactDB-CRUD");
                return contactDBCrudInter;
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
        return contactDBCrudInter;
    }
}
