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

    public static class Context {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("parameters")
        @Expose
        private Parameters parameters;
        @SerializedName("lifespan")
        @Expose
        private Integer lifespan;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Parameters getParameters() {
            return parameters;
        }

        public void setParameters(Parameters parameters) {
            this.parameters = parameters;
        }

        public Integer getLifespan() {
            return lifespan;
        }

        public void setLifespan(Integer lifespan) {
            this.lifespan = lifespan;
        }

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

    public static class Metadata {

        @SerializedName("intentId")
        @Expose
        private String intentId;
        @SerializedName("webhookUsed")
        @Expose
        private String webhookUsed;
        @SerializedName("webhookForSlotFillingUsed")
        @Expose
        private String webhookForSlotFillingUsed;
        @SerializedName("intentName")
        @Expose
        private String intentName;

        public String getIntentId() {
            return intentId;
        }

        public void setIntentId(String intentId) {
            this.intentId = intentId;
        }

        public String getWebhookUsed() {
            return webhookUsed;
        }

        public void setWebhookUsed(String webhookUsed) {
            this.webhookUsed = webhookUsed;
        }

        public String getWebhookForSlotFillingUsed() {
            return webhookForSlotFillingUsed;
        }

        public void setWebhookForSlotFillingUsed(String webhookForSlotFillingUsed) {
            this.webhookForSlotFillingUsed = webhookForSlotFillingUsed;
        }

        public String getIntentName() {
            return intentName;
        }

        public void setIntentName(String intentName) {
            this.intentName = intentName;
        }

    }

    public static class Parameters {}

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
        @SerializedName("parameters")
        @Expose
        private Parameters parameters;
        @SerializedName("contexts")
        @Expose
        private List<Context> contexts = null;
        @SerializedName("metadata")
        @Expose
        private Metadata metadata;
        @SerializedName("fulfillment")
        @Expose
        private Fulfillment fulfillment;
        @SerializedName("score")
        @Expose
        private Integer score;

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

        public Parameters getParameters() {
            return parameters;
        }

        public void setParameters(Parameters parameters) {
            this.parameters = parameters;
        }

        public List<Context> getContexts() {
            return contexts;
        }

        public void setContexts(List<Context> contexts) {
            this.contexts = contexts;
        }

        public Metadata getMetadata() {
            return metadata;
        }

        public void setMetadata(Metadata metadata) {
            this.metadata = metadata;
        }

        public Fulfillment getFulfillment() {
            return fulfillment;
        }

        public void setFulfillment(Fulfillment fulfillment) {
            this.fulfillment = fulfillment;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

    }

    public static class Status {

        @SerializedName("code")
        @Expose
        private Integer code;
        @SerializedName("errorType")
        @Expose
        private String errorType;
        @SerializedName("webhookTimedOut")
        @Expose
        private Boolean webhookTimedOut;

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

        public Boolean getWebhookTimedOut() {
            return webhookTimedOut;
        }

        public void setWebhookTimedOut(Boolean webhookTimedOut) {
            this.webhookTimedOut = webhookTimedOut;
        }

    }

}
