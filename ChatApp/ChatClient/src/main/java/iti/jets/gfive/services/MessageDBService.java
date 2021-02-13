package iti.jets.gfive.services;

import iti.jets.gfive.common.interfaces.MessageDBInter;
import iti.jets.gfive.common.interfaces.NotificationCrudInter;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class MessageDBService {
    private static MessageDBInter messageDBInter = null;

    private MessageDBService(){}

    public static MessageDBInter getMessageService(){
        if(messageDBInter == null){
            try {
                Registry registry = RegisteryObj.getInstance();
                messageDBInter = (MessageDBInter) registry.lookup("MessageDb");
                return messageDBInter;
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
        return messageDBInter;
    }
}
