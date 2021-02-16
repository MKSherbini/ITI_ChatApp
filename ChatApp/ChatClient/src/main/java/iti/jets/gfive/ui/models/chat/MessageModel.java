package iti.jets.gfive.ui.models.chat;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
public class MessageModel {
    private String messageName;
    private String senderName;
    private String receiverName;
    private String state;
    private String content;
    private Date messageDate;

    public MessageModel(String messageName, String senderName, String receiverName, String state, String content, Date messageDate) {
        this.messageName = messageName;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.state = state;
        this.content = content;
        this.messageDate = messageDate;
    }

    public MessageModel(String senderName, String receiverName, String content) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.content = content;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }
}
