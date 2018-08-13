package com.example.maask.deliveryagentchatbot.ResponseClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Maask on 8/5/2018.
 */

public class Response {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("sessionId")
    @Expose
    private String sessionId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public static class Fulfillment {

        @SerializedName("speech")
        @Expose
        private String speech;
        @SerializedName("messages")
        @Expose
        private List<Message> messages = null;

        public String getSpeech() {
            return speech;
        }

        public void setSpeech(String speech) {
            this.speech = speech;
        }

        public List<Message> getMessages() {
            return messages;
        }

        public void setMessages(List<Message> messages) {
            this.messages = messages;
        }

    }

    public static class Message {

        @SerializedName("type")
        @Expose
        private Integer type;
        @SerializedName("speech")
        @Expose
        private String speech;

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getSpeech() {
            return speech;
        }

        public void setSpeech(String speech) {
            this.speech = speech;
        }

    }

    public static class Result {

        @SerializedName("source")
        @Expose
        private String source;
        @SerializedName("resolvedQuery")
        @Expose
        private String resolvedQuery;
        @SerializedName("action")
        @Expose
        private String action;
        @SerializedName("actionIncomplete")
        @Expose
        private Boolean actionIncomplete;
        @SerializedName("fulfillment")
        @Expose
        private Fulfillment fulfillment;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getResolvedQuery() {
            return resolvedQuery;
        }

        public void setResolvedQuery(String resolvedQuery) {
            this.resolvedQuery = resolvedQuery;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Boolean getActionIncomplete() {
            return actionIncomplete;
        }

        public void setActionIncomplete(Boolean actionIncomplete) {
            this.actionIncomplete = actionIncomplete;
        }

        public Fulfillment getFulfillment() {
            return fulfillment;
        }

        public void setFulfillment(Fulfillment fulfillment) {
            this.fulfillment = fulfillment;
        }

    }

    public static class Status {

        @SerializedName("code")
        @Expose
        private Integer code;
        @SerializedName("errorType")
        @Expose
        private String errorType;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getErrorType() {
            return errorType;
        }

        public void setErrorType(String errorType) {
            this.errorType = errorType;
        }

    }

}
