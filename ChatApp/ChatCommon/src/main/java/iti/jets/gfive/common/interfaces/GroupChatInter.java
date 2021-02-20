package iti.jets.gfive.common.interfaces;

import iti.jets.gfive.common.models.GroupDto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GroupChatInter extends Remote {
   int insert(String groupname , List<String> list) throws RemoteException;
   List<GroupDto> selectAllGroups(String number) throws RemoteException;
}
