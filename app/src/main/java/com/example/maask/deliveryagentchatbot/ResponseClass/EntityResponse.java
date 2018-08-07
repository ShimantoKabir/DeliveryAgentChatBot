package com.example.maask.deliveryagentchatbot.ResponseClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Maask on 8/7/2018.
 */

public class EntityResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("isOverridable")
    @Expose
    private Boolean isOverridable;
    @SerializedName("entries")
    @Expose
    private List<Entry> entries = null;
    @SerializedName("isEnum")
    @Expose
    private Boolean isEnum;
    @SerializedName("automatedExpansion")
    @Expose
    private Boolean automatedExpansion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsOverridable() {
        return isOverridable;
    }

    public void setIsOverridable(Boolean isOverridable) {
        this.isOverridable = isOverridable;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public Boolean getIsEnum() {
        return isEnum;
    }

    public void setIsEnum(Boolean isEnum) {
        this.isEnum = isEnum;
    }

    public Boolean getAutomatedExpansion() {
        return automatedExpansion;
    }

    public void setAutomatedExpansion(Boolean automatedExpansion) {
        this.automatedExpansion = automatedExpansion;
    }

    public static class Entry {

        @SerializedName("value")
        @Expose
        private String value;
        @SerializedName("synonyms")
        @Expose
        private List<String> synonyms = null;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public List<String> getSynonyms() {
            return synonyms;
        }

        public void setSynonyms(List<String> synonyms) {
            this.synonyms = synonyms;
        }

    }

}
