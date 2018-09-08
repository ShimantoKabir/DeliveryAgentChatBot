package com.example.maask.deliveryagentchatbot.PojoClass;

import java.util.ArrayList;

/**
 * Created by Maask on 8/12/2018.
 */

public class ClientOfferedJob {

    private String clientId;
    private String parentKey;
    private String delivered;
    private String productAttributes;
    private String productDescription;
    private String productType;
    private String publishData;
    private String startAndEndLatLon;
    private Integer unitWeight;
    private ArrayList<AppliedDeliveryManInfo> appliedDeliveryManInfos = new ArrayList<>();

    public ClientOfferedJob() {}

    public ArrayList<AppliedDeliveryManInfo> getAppliedDeliveryManInfos() {
        return appliedDeliveryManInfos;
    }

    public void setAppliedDeliveryManInfos(ArrayList<AppliedDeliveryManInfo> appliedDeliveryManInfos) {
        this.appliedDeliveryManInfos = appliedDeliveryManInfos;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    public String getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(String productAttributes) {
        this.productAttributes = productAttributes;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getPublishData() {
        return publishData;
    }

    public void setPublishData(String publishData) {
        this.publishData = publishData;
    }

    public String getStartAndEndLatLon() {
        return startAndEndLatLon;
    }

    public void setStartAndEndLatLon(String startAndEndLatLon) {
        this.startAndEndLatLon = startAndEndLatLon;
    }

    public Integer getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(Integer unitWeight) {
        this.unitWeight = unitWeight;
    }

}
