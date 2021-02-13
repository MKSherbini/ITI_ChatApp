package iti.jets.gfive.classes;

import iti.jets.gfive.common.interfaces.NotificationReceiveInter;
import iti.jets.gfive.common.models.UserDto;

public class ConnectedClient {
    private UserDto client;
    private NotificationReceiveInter receiveNotif;

    public ConnectedClient(UserDto client, NotificationReceiveInter receiveNotif){
        this.client = client;
        this.receiveNotif = receiveNotif;
    }

    public UserDto getClient() {
        return client;
    }

    public void setClient(UserDto client) {
        this.client = client;
    }

    public NotificationReceiveInter getReceiveNotif() {
        return receiveNotif;
    }

    public void setReceiveNotif(NotificationReceiveInter receiveNotif) {
        this.receiveNotif = receiveNotif;
    }
}
