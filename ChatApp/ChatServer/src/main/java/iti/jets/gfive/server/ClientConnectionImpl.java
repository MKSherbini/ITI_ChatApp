package iti.jets.gfive.server;

import iti.jets.gfive.classes.ConnectedClient;
import iti.jets.gfive.common.interfaces.ClientConnectionInter;
import iti.jets.gfive.common.interfaces.GroupChatInter;
import iti.jets.gfive.common.interfaces.NotificationReceiveInter;
import iti.jets.gfive.common.models.GroupMessagesDto;
import iti.jets.gfive.common.models.MessageDto;
import iti.jets.gfive.common.models.NotificationDto;
import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.ui.helpers.StatsManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import java.sql.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientConnectionImpl extends UnicastRemoteObject implements ClientConnectionInter {
    public static Set<ConnectedClient> clientsPool = ConcurrentHashMap.newKeySet();
    public static Set<String> GroupChatMember = ConcurrentHashMap.newKeySet();

    public ClientConnectionImpl() throws RemoteException {
        var pinger = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ClientConnectionImpl.clientsPool.forEach(connectedClient -> {
                    try {
                        connectedClient.getReceiveNotif().receivePing();
                    } catch (RemoteException e) {
                        // gotcha u pos
                        e.printStackTrace();
                        try {
                            UserDBCrudImpl userDBCrud= new UserDBCrudImpl();
                            userDBCrud.updateUserConnection(connectedClient.getClient(),false);
                            connectedClient.getClient().setStatus("offline");
                            puplishStatus(connectedClient.getClient());
                        } catch (RemoteException remoteException) {
                            remoteException.printStackTrace();
                        }
                        ClientConnectionImpl.clientsPool.remove(connectedClient); // drop him, he prolly died
                        StatsManager.getInstance().updateConnectionStats();
                    }
                });
            }
        });
        pinger.setDaemon(true);
        pinger.start();
    }

    @Override
    public void register(UserDto user, NotificationReceiveInter notif) {
        ConnectedClient client = new ConnectedClient(user, notif);
        try {
            UserDBCrudImpl userDBCrud = new UserDBCrudImpl();
            userDBCrud.updateUserConnection(user, true);
            puplishStatus(user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        clientsPool.add(client);
        System.out.println("client " + user.getPhoneNumber() + " is added to the pool, clients count is " + clientsPool.size());
        StatsManager.getInstance().updateConnectionStats();
    }

    @Override
    public void unregister(NotificationReceiveInter notif) throws RemoteException {
        System.out.println("user in server unregister ");
        clientsPool.forEach(connectedClient -> {
            if (connectedClient.getReceiveNotif().equals(notif)) {
                System.out.println("cuurendnkdjknlkfndclkfdnc");
                try {
                    UserDBCrudImpl userDBCrud = new UserDBCrudImpl();
                    userDBCrud.updateUserConnection(connectedClient.getClient(), false);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                // publish that iam offline this object will
                // be removed immediately from the server
                try {
                    UserDto user = connectedClient.getClient();
                    user.setStatus("offline");
                    puplishStatus(user);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                clientsPool.remove(connectedClient);
                System.out.println("client " + connectedClient.getClient().getPhoneNumber() + " is removed from the pool");
            }
        });
        StatsManager.getInstance().updateConnectionStats();
    }

    @Override
    public void sendMsg(MessageDto msg) throws RemoteException {
        clientsPool.forEach(connectedClient -> {
            if (connectedClient.getClient().getPhoneNumber().equals(msg.getReceiverNumber())) {
                try {
                    connectedClient.getReceiveNotif().receiveMsg(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    clientsPool.remove(connectedClient); // drop him, he prolly died
                    StatsManager.getInstance().updateConnectionStats();
                }
            }
        });
    }

    @Override
    public void puplishStatus(UserDto user) throws RemoteException {
        if (clientsPool == null || clientsPool.size() < 1)
            return;
        clientsPool.forEach(connectedClient -> {
            if (!connectedClient.getClient().getPhoneNumber().equals(user.getPhoneNumber())) {
                try {
                    connectedClient.getReceiveNotif().updateStatus(user);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    clientsPool.remove(connectedClient); // drop him, he prolly died
                    StatsManager.getInstance().updateConnectionStats();
                }
            }
        });
    }


    @Override
    public void publishPicture(UserDto user) throws RemoteException {
        System.out.println("INSIDE THE PUBLIC PIC");
        if (clientsPool == null || clientsPool.size() < 1)
            return;
        clientsPool.forEach(connectedClient -> {
            if (!connectedClient.getClient().getPhoneNumber().equals(user.getPhoneNumber())) {
                try {
                    connectedClient.getReceiveNotif().updatePicture(user);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    clientsPool.remove(connectedClient); // drop him, he prolly died
                    StatsManager.getInstance().updateConnectionStats();
                }
            }
        });
    }

    @Override
    public void sendFile(MessageDto msg) throws RemoteException {
        clientsPool.forEach(connectedClient -> {
            if (connectedClient.getClient().getPhoneNumber().equals(msg.getReceiverNumber())) {
                try {
                    connectedClient.getReceiveNotif().receiveFile(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    clientsPool.remove(connectedClient); // drop him, he prolly died
                    StatsManager.getInstance().updateConnectionStats();
                }
            }
        });
    }
    @Override
    public void publishAnnouncement(String announce) throws RemoteException {
        if (clientsPool == null || clientsPool.size() < 1)
            return;
        clientsPool.forEach(connectedClient -> {

                try {
                    connectedClient.getReceiveNotif().receive(
                            new NotificationDto(1000,announce,"01000000000",
                                    new Date(new java.util.Date().getTime()),
                                    false, connectedClient.getClient().getPhoneNumber()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

        });
    }

    @Override
    public void addMemberToGroupChat(String number) throws RemoteException {
        GroupChatMember.add(number);
        System.out.println("size--->" + GroupChatMember.size());
    }

    @Override
    public void RemoveMemeberFromChatGroup(String number) throws RemoteException {
        GroupChatMember.remove(number);
        System.out.println("size--->" + GroupChatMember.size());

    }

    @Override
    public void createGroupInAllMemebers(String groupname, List<String> members, String id) throws RemoteException {
        //loop on the list and then clean it
        clientsPool.forEach(connectedClient -> {
            for (String phonenumber : members) {
                System.out.println("1---->" + phonenumber);
                if (connectedClient.getClient().getPhoneNumber().equals(phonenumber)) {
                    try {

                        System.out.println("2---->" + connectedClient.getClient().getPhoneNumber());
                        connectedClient.getReceiveNotif().addGroupInMembersList(groupname, id);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        clientsPool.remove(connectedClient); // drop him, he prolly died

StatsManager.getInstance().updateConnectionStats();
                    }
                }
            }
        });
        GroupChatMember.clear();

    }

    @Override
    public void sendGroupMsg(List<String> list, String id, String message, String name) throws RemoteException {
        clientsPool.forEach(connectedClient -> {
            for (String phonenumber : list) {
                if (connectedClient.getClient().getPhoneNumber().equals(phonenumber)) {
                    try {

                        System.out.println("2---->" + connectedClient.getClient().getPhoneNumber());
                        connectedClient.getReceiveNotif().receiveGroupMessage(id, message, name, false, "-1");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        clientsPool.remove(connectedClient); // drop him, he prolly died
                        StatsManager.getInstance().updateConnectionStats();
                    }
                }
            }

        });
    }

    @Override
    public void sendFileToGroup(GroupMessagesDto msg, List<String> groupMembers) throws RemoteException {
        clientsPool.forEach(connectedClient -> {
            groupMembers.forEach(groupMember -> {
                if (connectedClient.getClient().getPhoneNumber().equals(groupMember)) {
                    try {
                        GroupChatImpl c = new GroupChatImpl();
                        connectedClient.getReceiveNotif().receiveGroupMessage(msg.getGroupId(), msg.getMessage_name(),
                                c.getUserName(msg.getSendernumber()), true, msg.getId());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        clientsPool.remove(connectedClient); // drop him, he prolly died
                        StatsManager.getInstance().updateConnectionStats();
                    }
                }
            });
        });
    }
}
