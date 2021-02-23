package iti.jets.gfive.common.models;

import java.io.Serializable;

public class GroupMessagesDto implements Serializable {
    String id;
    String groupId;
    String message;
    byte [] fileContent;
    String sendernumber;
    String message_name;

    public GroupMessagesDto() {
    }

    public GroupMessagesDto(String groupId, String message, String sendernumber, String message_name) {
        this.groupId = groupId;
        this.message = message;
        this.sendernumber = sendernumber;
        this.message_name = message_name;
    }
    public GroupMessagesDto(String groupId, byte [] fileContent, String sendernumber, String message_name) {
        this.groupId = groupId;
        this.fileContent = fileContent;
        this.sendernumber = sendernumber;
        this.message_name = message_name;
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

    public String getMessage_name() {
        return message_name;
    }

    public void setMessage_name(String message_name) {
        this.message_name = message_name;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
