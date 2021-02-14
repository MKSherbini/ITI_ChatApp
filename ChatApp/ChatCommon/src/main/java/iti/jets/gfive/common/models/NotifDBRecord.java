package iti.jets.gfive.common.models;

import java.io.Serializable;

public class NotifDBRecord implements Serializable {
    int rowsAffected;
    int notifId;

    public NotifDBRecord(int rowsAffected, int notifId){
        this.rowsAffected = rowsAffected;
        this.notifId = notifId;
    }

    public int getRowsAffected() {
        return rowsAffected;
    }

    public void setRowsAffected(int rowsAffected) {
        this.rowsAffected = rowsAffected;
    }

    public int getNotifId() {
        return notifId;
    }

    public void setNotifId(int notifId) {
        this.notifId = notifId;
    }
}
