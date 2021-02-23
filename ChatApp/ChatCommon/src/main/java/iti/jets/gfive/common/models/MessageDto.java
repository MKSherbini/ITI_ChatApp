package iti.jets.gfive.common.models;

import java.io.Serializable;
import java.sql.Date;

public class MessageDto implements Serializable {
    private int id;
    private String messageName;
    private String senderNumber;
    private String receiverNumber;
    private String state;
    private String content;
    private Date messageDate;
    private byte[] fileContent;

    public MessageDto() {
    }

    public MessageDto(int id, String messageName, String senderNumber, String receiverNumber, String state, String content, Date messageDate) {
        this.id = id;
        this.messageName = messageName;
        this.senderNumber = senderNumber;
        this.receiverNumber = receiverNumber;
        this.state = state;
        this.content = content;
        this.messageDate = messageDate;
    }
    public MessageDto(int id, String messageName, String senderNumber, String receiverNumber, String state, byte[] fileContent, Date messageDate){
        this.id = id;
        this.messageName = messageName;
        this.senderNumber = senderNumber;
        this.receiverNumber = receiverNumber;
        this.state = state;
        this.fileContent = fileContent;
        this.messageDate = messageDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
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

    @Override
    public String toString() {
        return "MessageDto{" +
                "messageName='" + messageName + '\'' +
                ", senderNumber='" + senderNumber + '\'' +
                ", receiverNumber='" + receiverNumber + '\'' +
                ", state='" + state + '\'' +
                ", content='" + content + '\'' +
                ", messageDate=" + messageDate +
                '}';
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
}
