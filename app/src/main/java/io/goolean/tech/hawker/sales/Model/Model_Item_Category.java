package io.goolean.tech.hawker.sales.Model;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Model_Item_Category implements Serializable {

    String id,catname,img_url,position_key,sub_cat_status,checkLevel;
    JSONArray subCategorysetJsonArray;
    String arrayStatus;
    private List<SubCategoryModel> subCategoryModelList;

    public String getArrayStatus() {
        return arrayStatus;
    }

    public void setArrayStatus(String arrayStatus) {
        this.arrayStatus = arrayStatus;
    }

    private boolean isSelected;


    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public void setPosition_key(String position_key) { this.position_key = position_key; }
    public void setCatId(String id) {
        this.id= id;
    }
    public void setSub_cat_status(String sub_cat_status) { this.sub_cat_status = sub_cat_status; }
    public void setCatName(String cat_name) {
        this.catname=cat_name;
    }
    public void setCatIconName(String image_url) {
        this.img_url=image_url;
    }
    public void setCheckLevel(String checkLevel) { this.checkLevel = checkLevel; }




    public String getCheckLevel() { return checkLevel; }
    public String getPosition_key() {
        return position_key;
    }
    public String getSub_cat_status() {
        return sub_cat_status;
    }
    public String getCatname() {
        return catname;
    }
    public String getId() {
        return id;
    }
    public String getImg_url() {
        return img_url;
    }

    public JSONArray getSubCategorysetJsonArray() {
        return subCategorysetJsonArray;
    }

    public void setSubCategorysetJsonArray(JSONArray subCategoryString) {
        this.subCategorysetJsonArray = subCategoryString;
    }

    public List<SubCategoryModel> getSubCategoryModelList() {
        return subCategoryModelList;
    }

    public void setSubCategoryModelList(List<SubCategoryModel> subCategoryModelList) {
        this.subCategoryModelList = subCategoryModelList;
    }
}
