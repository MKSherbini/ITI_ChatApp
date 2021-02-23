package iti.jets.gfive.services;

import iti.jets.gfive.common.interfaces.GroupChatInter;
import iti.jets.gfive.common.interfaces.MessageDBInter;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class GroupChatService {
    private static GroupChatInter groupChatInter = null;

    private GroupChatService(){}

    public static GroupChatInter getGroupChatInter(){
        if(groupChatInter == null){
            try {
                Registry registry = RegisteryObj.getInstance();
                groupChatInter = (GroupChatInter) registry.lookup("GroupchatDb");
                return groupChatInter;
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }
        return groupChatInter;
    }
}
