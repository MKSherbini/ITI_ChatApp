package iti.jets.gfive.ui.models.serialization;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "chat-conversation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChatModel {
    String chatName;
    String chatOwner;

    @XmlElementWrapper(name = "messages")
    @XmlElement(name = "message")
    List<MessageModel> messages = new ArrayList<>();

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatOwner() {
        return chatOwner;
    }

    public void setChatOwner(String chatOwner) {
        this.chatOwner = chatOwner;
    }

    public List<MessageModel> getMessages() {
        return messages;
    }

}
