package iti.jets.gfive.server;

import iti.jets.gfive.classes.ConnectedClient;
import iti.jets.gfive.common.interfaces.ClientConnectionInter;
import iti.jets.gfive.common.interfaces.NotificationReceiveInter;
import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.common.models.UserDto;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientConnectionImpl extends UnicastRemoteObject implements ClientConnectionInter {
    public static ConcurrentLinkedQueue<ConnectedClient> clientsPool = new ConcurrentLinkedQueue<>();
    public static ConcurrentLinkedQueue<String> GroupChatMember = new ConcurrentLinkedQueue<>();

    public ClientConnectionImpl() throws RemoteException {}

    @Override
    public void register(UserDto user, NotificationReceiveInter notif) {
        ConnectedClient client = new ConnectedClient(user, notif);
        clientsPool.add(client);
        System.out.println("client " + user.getPhoneNumber() + " is added to the pool");
    }

    @Override
    public void unregister(NotificationReceiveInter notif) throws RemoteException {
        clientsPool.forEach(connectedClient -> {
            if(connectedClient.getReceiveNotif().equals(notif)){
                clientsPool.remove(connectedClient);
                System.out.println("client " + connectedClient.getClient().getPhoneNumber() + " is removed from the pool");
            }
        });
    }

    @Override
    public void sendMsg(MessageDto msg) throws RemoteException {
        clientsPool.forEach(connectedClient -> {
            if(connectedClient.getClient().getPhoneNumber().equals(msg.getReceiverNumber())){
                try {
                    connectedClient.getReceiveNotif().receiveMsg(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void addMemberToGroupChat(String number) throws RemoteException {
        GroupChatMember.add(number);
        System.out.println("size--->"+GroupChatMember.size());
    }

    @Override
    public void RemoveMemeberFromChatGroup(String number) throws RemoteException {
        GroupChatMember.remove(number);
        System.out.println("size--->"+GroupChatMember.size());

    }

    @Override
    public void createGroupInAllMemebers(String groupname ,List<String> members) throws RemoteException {
        //loop on the list and then clean it
        clientsPool.forEach(connectedClient -> {
            for (String phonenumber : members) {
                   System.out.println("1---->"+phonenumber);
                if (connectedClient.getClient().getPhoneNumber().equals(phonenumber)){
                    try {

                        System.out.println("2---->"+connectedClient.getClient().getPhoneNumber());
                        connectedClient.getReceiveNotif().addGroupInMembersList(groupname);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        GroupChatMember.clear();

    }

    @Override
    public void sendGroupMsg(List<String> list, String id, String message , String name) throws RemoteException {
        clientsPool.forEach(connectedClient -> {
            for (String phonenumber : list) {
                if (connectedClient.getClient().getPhoneNumber().equals(phonenumber)){
                    try {

                        System.out.println("2---->"+connectedClient.getClient().getPhoneNumber());
                        connectedClient.getReceiveNotif().receiveGroupMessage(id,message,name);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }
}
