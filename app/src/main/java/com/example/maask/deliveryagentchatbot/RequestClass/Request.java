package com.example.maask.deliveryagentchatbot.RequestClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Maask on 8/5/2018.
 */

public class Request {

    @SerializedName("contexts")
    @Expose
    private List<String> contexts = null;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("query")
    @Expose
    private String query;
    @SerializedName("sessionId")
    @Expose
    private String sessionId;
    @SerializedName("timezone")
    @Expose
    private String timezone;

    public List<String> getContexts() {
        return contexts;
    }

    public void setContexts(List<String> contexts) {
        this.contexts = contexts;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

}
