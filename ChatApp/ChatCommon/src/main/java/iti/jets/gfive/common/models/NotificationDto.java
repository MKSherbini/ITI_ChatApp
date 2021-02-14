package iti.jets.gfive.common.models;

import java.io.Serializable;
import java.sql.Date;

public class NotificationDto implements Serializable{
    int id;
    String content;
    String senderId;
    Date notificationDate;
    boolean completed;
    String receiverId;

    public NotificationDto(int id, String content, String senderId, Date notificationDate, boolean completed, String receiverId){
        this.id = id;
        this.content = content;
        this.senderId = senderId;
        this.notificationDate = notificationDate;
        this.completed = completed;
        this.receiverId = receiverId;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}
