package iti.jets.gfive.common.interfaces;

import iti.jets.gfive.common.models.MessageDto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface MessageDBInter extends Remote {
   List<MessageDto> selectAllMessages(String receiverNumber , String senderNumber) throws RemoteException;
   int insertMessage(MessageDto messageDto) throws RemoteException;
   int updateMessageState(String receiverNumber , String senderNumber) throws RemoteException;
   byte[] getFile(int recordId) throws RemoteException;
}
