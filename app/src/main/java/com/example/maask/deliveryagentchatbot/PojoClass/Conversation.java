package com.example.maask.deliveryagentchatbot.PojoClass;

/**
 * Created by Maask on 8/6/2018.
 */

public class Conversation {

    public String speaker;
    public String expression;

    public Conversation() {}

    public Conversation(String speaker, String expression) {
        this.speaker = speaker;
        this.expression = expression;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
