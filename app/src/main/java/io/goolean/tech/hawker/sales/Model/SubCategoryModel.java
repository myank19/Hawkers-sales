package io.goolean.tech.hawker.sales.Model;

public class SubCategoryModel {

    String sSubID,sSubCatName,sCatId,sSubCatImageUrl,strSubPosition_key,checkLevel;

    public boolean isSelected;


    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }




    public String getStrSubPosition_key() { return strSubPosition_key; }

    public void setStrSubPosition_key(String strSubPosition_key) { this.strSubPosition_key = strSubPosition_key; }

    public void setsSubID(String sSubID) {
        this.sSubID = sSubID;
    }

    public void setsSubCatName(String sSubCatName) {
        this.sSubCatName = sSubCatName;
    }

    public void setsSubCatImageUrl(String sSubCatImageUrl) { this.sSubCatImageUrl = sSubCatImageUrl; }

    public String getsSubID() {
        return sSubID;
    }

    public String getsSubCatName() {
        return sSubCatName;
    }

    public String getsSubCatImageUrl() {
        return sSubCatImageUrl;
    }
    public void setCheckLevel(String checkLevel) { this.checkLevel = checkLevel; }

    public String getCheckLevel() { return checkLevel; }

    public String getsCatId() {
        return sCatId;
    }

    public void setsCatId(String sCatId) {
        this.sCatId = sCatId;
    }
}
