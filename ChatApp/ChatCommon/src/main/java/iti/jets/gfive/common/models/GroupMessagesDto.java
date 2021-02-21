package iti.jets.gfive.common.models;

import java.io.Serializable;

public class GroupMessagesDto implements Serializable {
    String id;
    String message;
    String sendernumber;

    public GroupMessagesDto() {
    }

    public GroupMessagesDto(String id, String message, String sendernumber) {
        this.id = id;
        this.message = message;
        this.sendernumber = sendernumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendernumber() {
        return sendernumber;
    }

    public void setSendernumber(String sendernumber) {
        this.sendernumber = sendernumber;
    }
}
