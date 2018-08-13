package com.example.maask.deliveryagentchatbot.HelperClass;

/**
 * Created by Maask on 8/11/2018.
 */

public class ExtraUserQuery {

    public boolean needExtraUserQuery;
    public String extraUserQuery;

    public ExtraUserQuery(boolean needExtraUserQuery, String extraUserQuery) {
        this.needExtraUserQuery = needExtraUserQuery;
        this.extraUserQuery = extraUserQuery;
    }

    public boolean isNeedExtraUserQuery() {
        return needExtraUserQuery;
    }

    public void setNeedExtraUserQuery(boolean needExtraUserQuery) {
        this.needExtraUserQuery = needExtraUserQuery;
    }

    public String getExtraUserQuery() {
        return extraUserQuery;
    }

    public void setExtraUserQuery(String extraUserQuery) {
        this.extraUserQuery = extraUserQuery;
    }
}
