package io.goolean.tech.hawker.sales.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.goolean.tech.hawker.sales.Model.Model_Item_Category;
import io.goolean.tech.hawker.sales.Model.SubCategoryModel;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Seller;

import static android.content.Context.MODE_PRIVATE;

public class Adapter_Category extends RecyclerView.Adapter<Adapter_Category.ViewHolder> {
    private Context context;
    private List<Model_Item_Category> arrayList_model_categories;
    private HashMap<String, String> hashMapListChooseCat;
    private HashMap<String, String> hashMapListChooseCatStatus;
    private List<SubCategoryModel> subCategory;
    JSONArray subCategoryJsonArray;
    Model_Item_Category model_item_category;
    ListMultimap<String, String> myMultimap;
    Adapter_Sub_Category adapter_sub_category;
    private TextView tvService;
    private String strSelectedService, strSubSelectedService, strString1;
    List<String> list;
    SharedPreferences shared;
    ArrayList<String> arrPackage;
    List<String> l;
    List<String> lStaus;
    List<List<SubCategoryModel>> subcategoryList = new ArrayList<List<SubCategoryModel>>();


    public Adapter_Category(Context activity, List<Model_Item_Category> movieList, HashMap<String, String> hashMapListChooseCat,
                            HashMap<String, String> hashMapListChooseCatStatus, TextView tvService) {
        this.context = activity;
        this.arrayList_model_categories = movieList;
        this.hashMapListChooseCat = hashMapListChooseCat;
        this.hashMapListChooseCatStatus = hashMapListChooseCatStatus;
        this.tvService = tvService;

        shared = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", MODE_PRIVATE);
        // add values for your ArrayList any where...
        arrPackage = new ArrayList<>();

        SharedPrefrence_Seller.getSharedPrefrencesFixerShopCategoryId(context);
        SharedPrefrence_Seller.getSharedPrefrencesFixerShopSubCategoryId(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_design_catergory, parent, false);
        //   hashMapListChooseCat = new HashMap<>();
        l = new ArrayList<String>(hashMapListChooseCat.keySet());
        lStaus = new ArrayList<String>(hashMapListChooseCatStatus.keySet());
        subCategory = new ArrayList<>(12);
        myMultimap = ArrayListMultimap.create();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final int pos = position;
        model_item_category = arrayList_model_categories.get(position);
        holder.catName.setText(model_item_category.getCatname());
        holder.catName.setChecked(arrayList_model_categories.get(position).getSelected());
        holder.catName.setTag(position);
        if (arrayList_model_categories.get(position).getCheckLevel().equals("level_2")) {
            if (arrayList_model_categories.get(position).getSelected() == true) {
                parseData(holder, pos, model_item_category);
                holder.rvSubService.setVisibility(View.VISIBLE);
            }
        } else if (arrayList_model_categories.get(position).getCheckLevel().equals("level_3")) {
            if (arrayList_model_categories.get(position).getSelected() == true) {
                parseData(holder, pos,model_item_category);
                holder.rvSubService.setVisibility(View.VISIBLE);
            }
        }

        if (!SharedPrefrence_Seller.getFixr_shop_categoryid().equals("")) {
            retriveSharedValue(l);
        } else {
            hashMapListChooseCat.clear();
            hashMapListChooseCatStatus.clear();
        }



       /* strSelectedService = SharedPrefrence_Seller.getFixr_shop_categoryid();
        tvService.setText(strSelectedService);*/
        parseData(holder, position,model_item_category);

       /*if(arrayList_model_categories.get(position).getSubCategorysetJsonArray().length() != 0){
           parseData(holder,position);
       }
       */
        holder.catName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer pos = (Integer) holder.catName.getTag();
                if (arrayList_model_categories.get(pos).getSelected()) {
                    arrayList_model_categories.get(pos).setSelected(false);
                    myMultimap.removeAll(arrayList_model_categories.get(pos).getPosition_key());
                    SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSubCategoryId(context, String.valueOf(myMultimap));
                    hashMapListChooseCat.remove(arrayList_model_categories.get(pos).getId());
                    hashMapListChooseCatStatus.remove(arrayList_model_categories.get(pos).getSub_cat_status());
                    List<String> l = new ArrayList<String>(hashMapListChooseCat.keySet());
                    List<String> lStatus = new ArrayList<String>(hashMapListChooseCatStatus.keySet());
                    packagesharedPreferences(l);
                    packagesharedPreferencesSubStatus(lStatus);
                    // SharedPrefrence_Seller.saveSharedPrefrencesFixerShopCategoryId(context, String.valueOf(l));
                    //holder.rvSubService.setVisibility(View.GONE);

                    if (arrayList_model_categories.get(pos).getCheckLevel().equals("level_2")) {
                        holder.rvSubService.setVisibility(View.GONE);
                    } else if (arrayList_model_categories.get(pos).getCheckLevel().equals("level_3")) {
                        holder.rvSubService.setVisibility(View.GONE);
                    }


                      holder.rvSubService.setVisibility(View.GONE);

//                    else if (arrayList_model_categories.get(pos).getCheckLevel().equals("level_1")) {
//                        holder.rvSubService.setVisibility(View.GONE);
//                    }
                } else {

                    arrayList_model_categories.get(pos).setSelected(true);
                    hashMapListChooseCatStatus.put(arrayList_model_categories.get(pos).getSub_cat_status(), arrayList_model_categories.get(pos).getId());
                    List<String> lStatus = new ArrayList<String>(hashMapListChooseCatStatus.keySet());
                    packagesharedPreferencesSubStatus(lStatus);

                    hashMapListChooseCat.put(arrayList_model_categories.get(pos).getId(), arrayList_model_categories.get(pos).getId());
                    List<String> l = new ArrayList<String>(hashMapListChooseCat.keySet());
                    packagesharedPreferences(l);
                    /// check here
                    if (arrayList_model_categories.get(pos).getArrayStatus().equals("1")) {
                        if (arrayList_model_categories.get(pos).getCheckLevel().equals("level_2")) {
                            parseData(holder, pos,model_item_category);

                            holder.rvSubService.setVisibility(View.VISIBLE);
                        }
//                        } else if (arrayList_model_categories.get(pos).getCheckLevel().equals("level_1")) {
//                            parseData(holder, pos);
//                            holder.rvSubService.setVisibility(View.VISIBLE);
//                        }

//                        else if (arrayList_model_categories.get(pos).getCheckLevel().equals("level_3")) {
//                            parseData(holder, pos);
//                            holder.rvSubService.setVisibility(View.VISIBLE);
//                        }

                            parseData(holder, pos, model_item_category);
                            holder.rvSubService.setVisibility(View.VISIBLE);


                    }

                }
            }
        });
    }


    private void packagesharedPreferences(List<String> l) {
        SharedPreferences.Editor editor = shared.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(l);
        editor.putStringSet("DATE_LIST", set);
        editor.apply();
        SharedPrefrence_Seller.saveSharedPrefrencesFixerShopCategoryId(context, String.valueOf(l));
        Log.d("storesharedPreferences", "" + set);
    }

    @SuppressLint("LongLogTag")
    private void retriveSharedValue(List<String> l) {
        Set<String> set = shared.getStringSet("DATE_LIST", null);
        if (set != null) {
            l.addAll(set);
            Log.d("retrivesharedPreferences", "" + set);
            for (String ss : set) {
                hashMapListChooseCat.put(ss, ss);
            }
            Log.d("HASHMAP", "" + hashMapListChooseCat);
            hashMapListChooseCat = hashMapListChooseCat;
        }

    }

    private void packagesharedPreferencesSubStatus(List<String> l) {
        SharedPreferences.Editor editor = shared.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(l);
        editor.putStringSet("SUB_STATUS", set);
        editor.apply();
        SharedPrefrence_Seller.saveSharedPrefrencesFixerShopCategoryIdStatus(context, String.valueOf(l));
        Log.d("storesharedPreferences", "" + set);
    }

    @SuppressLint("LongLogTag")
    private void retriveSharedValueSubStatus(List<String> l) {
        Set<String> set = shared.getStringSet("SUB_STATUS", null);
        if (set != null) {
            l.addAll(set);
            Log.d("retrivesharedPreferences", "" + set);
            for (String ss : set) {
                hashMapListChooseCatStatus.put(ss, ss);
            }
            Log.d("HASHMAP", "" + hashMapListChooseCatStatus);
            hashMapListChooseCatStatus = hashMapListChooseCatStatus;
        }

    }

    private void parseData(final ViewHolder holder, final int position, Model_Item_Category model_item_category) {
        holder.rvSubService.setHasFixedSize(true);
        holder.rvSubService.setLayoutManager(new GridLayoutManager(context, 2));
        if (arrayList_model_categories.get(position).getSub_cat_status().equals("1")) {
            subCategory.clear();
            subCategoryJsonArray = arrayList_model_categories.get(position).getSubCategorysetJsonArray();
            for (int i = 0; i < subCategoryJsonArray.length(); i++) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = subCategoryJsonArray.getJSONObject(i);
                    SubCategoryModel subCategoryModel = new SubCategoryModel();
                    subCategoryModel.setsSubCatName(jsonObject.getString("sub_cat_name"));
                    subCategoryModel.setsSubID(jsonObject.getString("id"));
                    subCategoryModel.setStrSubPosition_key(jsonObject.getString("position_key"));
                    subCategoryModel.setCheckLevel(jsonObject.getString("check_level"));

                    strSubSelectedService = SharedPrefrence_Seller.getFixr_shop_Subcategoryid();
                    if (strSubSelectedService != null || !strSubSelectedService.equals("")) {
                        //  splitfun(strSubSelectedService);
                        strSubSelectedService = spiltString(strSubSelectedService);
                        spiltStringCategory(subCategoryModel, strSubSelectedService, jsonObject.getString("id"));
                    } else {
                        subCategoryModel.setSelected(false);
                    }
                    subCategory.add(subCategoryModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //subcategoryList.add(subCategory);
                model_item_category.setSubCategoryModelList(subCategory);
            }
            // Adapter_Sub_Category adapter_sub_category =  new Adapter_Sub_Category(context,subCategory,hashMapListChooseSubCat);
            adapter_sub_category = new Adapter_Sub_Category(context, subCategory, myMultimap,arrayList_model_categories,subcategoryList);
            holder.rvSubService.setAdapter(adapter_sub_category);
            adapter_sub_category.notifyDataSetChanged();
        } else {
            holder.rvSubService.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (null != arrayList_model_categories ? arrayList_model_categories.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox catName;
        RecyclerView rvSubService;

        public ViewHolder(View itemView) {
            super(itemView);
            catName = (CheckBox) itemView.findViewById(R.id.txt_CatName);
            rvSubService = itemView.findViewById(R.id.rvSubService);
        }
    }

    private String spiltString(String strSubSelectedService) {
        Matcher matcher = Pattern.compile("\\[([^\\]]+)").matcher(strSubSelectedService);
        List<String> tags = new ArrayList<>();
        int pos = -1;
        while (matcher.find(pos + 1)) {
            pos = matcher.start();
            tags.add(matcher.group(1));
        }
        strString1 = String.valueOf(tags);
        strString1 = strString1.replace("[", "").replace("]", "");
        strString1 = strString1.replaceAll("\\s+", "");
        return strString1;
    }

    private String spiltStringCategory(SubCategoryModel subCategoryModel, String SelectedService, String id) {
        String strpart = "";
        String[] parts = SelectedService.split(",");
        for (String part : parts) {
            strpart = part;
            if (strpart.equals(id)) {
                subCategoryModel.setSelected(true);
                // subCategoryModel.setSelected(false);
            }
        }
        return strpart;
    }
}



