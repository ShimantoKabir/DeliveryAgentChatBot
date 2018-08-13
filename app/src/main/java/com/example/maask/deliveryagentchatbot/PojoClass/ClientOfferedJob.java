package com.example.maask.deliveryagentchatbot.PojoClass;

/**
 * Created by Maask on 8/12/2018.
 */

public class ClientOfferedJob {

    private String delivered;
    private String productAttributes;
    private String productDescription;
    private String productType;
    private String publishData;
    private String startAndEndLatLon;
    private Integer unitWeight;

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
