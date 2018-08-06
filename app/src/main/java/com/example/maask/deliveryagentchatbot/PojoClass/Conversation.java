package com.example.maask.deliveryagentchatbot.PojoClass;

/**
 * Created by Maask on 8/6/2018.
 */

public class Conversation {

    public boolean isMe;
    public String conversation;

    public Conversation(boolean isMe, String conversation) {
        this.isMe = isMe;
        this.conversation = conversation;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public String getConversation() {
        return conversation;
    }

    public void setConversation(String conversation) {
        this.conversation = conversation;
    }
}
