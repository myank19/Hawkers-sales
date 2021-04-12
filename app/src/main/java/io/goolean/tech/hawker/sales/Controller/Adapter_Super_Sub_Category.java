package io.goolean.tech.hawker.sales.Controller;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.ListMultimap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.goolean.tech.hawker.sales.Model.Model_Item_Category;
import io.goolean.tech.hawker.sales.Model.SubCategoryModel;
import io.goolean.tech.hawker.sales.Model.SuperSubCategoryModel;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Seller;

public class Adapter_Super_Sub_Category extends RecyclerView.Adapter<Adapter_Super_Sub_Category.ViewHolder>  {
    private Context context;
    private List<SuperSubCategoryModel> arrayList_model_categories;
    View view;
    ListMultimap<String, String> hashMapListChooseSuperSubCat;

    public Adapter_Super_Sub_Category(Context activity, List<SuperSubCategoryModel> movieList, ListMultimap<String,String> hashMap) {
        this.context=activity;
        this.arrayList_model_categories= movieList;
        this.hashMapListChooseSuperSubCat = hashMap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.list_design_subcatergory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SuperSubCategoryModel  subCategoryModel= arrayList_model_categories.get(position);
        holder.catName.setText(subCategoryModel.getsSuperSubCatName());

        holder.catName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){

                    hashMapListChooseSuperSubCat.put(subCategoryModel.getPosition_key(),subCategoryModel.getsSuperSubID());
                 //   Toast.makeText(context, hashMapListChooseSuperSubCat+"", Toast.LENGTH_SHORT).show();
                    SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSuperSubCategoryId(context, String.valueOf(hashMapListChooseSuperSubCat));
                }else {
                    hashMapListChooseSuperSubCat.remove(subCategoryModel.getPosition_key(),subCategoryModel.getsSuperSubID());
                  //  Toast.makeText(context, hashMapListChooseSuperSubCat+"", Toast.LENGTH_SHORT).show();
                    SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSuperSubCategoryId(context, String.valueOf(hashMapListChooseSuperSubCat));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList_model_categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox catName;
        public ViewHolder(View itemView) {
            super(itemView);
            catName = (CheckBox)itemView.findViewById(R.id.txt_CatName);
        }
    }

}
