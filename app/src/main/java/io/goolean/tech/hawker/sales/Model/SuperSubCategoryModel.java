package io.goolean.tech.hawker.sales.Model;

public class SuperSubCategoryModel {

    String sSuperSubID;
    String sSuperSubCatName;
    String sSuperSubCatImageUrl;

    public void setPosition_key(String position_key) {
        this.position_key = position_key;
    }

    public String getPosition_key() {
        return position_key;
    }

    String position_key;

    public void setsSuperSubID(String sSuperSubID) {
        this.sSuperSubID = sSuperSubID;
    }

    public void setsSuperSubCatName(String sSuperSubCatName) {
        this.sSuperSubCatName = sSuperSubCatName;
    }

    public void setsSuperSubCatImageUrl(String sSuperSubCatImageUrl) {
        this.sSuperSubCatImageUrl = sSuperSubCatImageUrl;
    }

    public String getsSuperSubID() {
        return sSuperSubID;
    }

    public String getsSuperSubCatName() {
        return sSuperSubCatName;
    }

    public String getsSuperSubCatImageUrl() {
        return sSuperSubCatImageUrl;
    }
}
