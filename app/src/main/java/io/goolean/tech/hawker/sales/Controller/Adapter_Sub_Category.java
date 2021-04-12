package io.goolean.tech.hawker.sales.Controller;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Index;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.goolean.tech.hawker.sales.Constant.Urls;
import io.goolean.tech.hawker.sales.Model.Model_Item_Category;
import io.goolean.tech.hawker.sales.Model.SubCategoryModel;
import io.goolean.tech.hawker.sales.Model.SuperSubCategoryModel;
import io.goolean.tech.hawker.sales.Networking.VolleySingleton;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Seller;
import io.goolean.tech.hawker.sales.View.HomeActivity;




public class Adapter_Sub_Category extends RecyclerView.Adapter<Adapter_Sub_Category.ViewHolder> {
    private Context context;
    private List<SubCategoryModel> arrayList_model_categories;
    private HashMap<String, String> hashMapListChooseSubCat;
    private List<SuperSubCategoryModel> superSubCategoryModelsList;
    RecyclerView rvSelectSuperService;
    Adapter_Super_Sub_Category adapter_category;

    RequestQueue requestQueue;
    View view;
    String s1 = "";
    StringBuilder sb = new StringBuilder();
    SharedPreferences sharedpreferences;
    public String split = "";
    public String req;
    static ListMultimap<String, String> hashMapListChooseSuperSubCat;
    String strSelectedService = "";
    ListMultimap<String, String> myMultimap;

    SubCategoryModel subCategoryModel;
    List<Model_Item_Category> category_list;
    List<List<SubCategoryModel>> subcategoryList;



    public Adapter_Sub_Category(Context activity, List<SubCategoryModel> movieList, ListMultimap<String, String> myMultimap,
                                List<Model_Item_Category> category_list,List<List<SubCategoryModel>> subcategoryList) {
        this.context = activity;
        this.arrayList_model_categories = movieList;
        this.myMultimap = myMultimap;
        this.category_list = category_list;
        this.subcategoryList = subcategoryList;
        SharedPrefrence_Seller.getSharedPrefrencesFixerShopSubCategoryId(context);
        sharedpreferences = context.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        Log.d("arrayList_mod",""+arrayList_model_categories.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.list_design_subcatergory, parent, false);
        // hashMapListChooseSubCat = new HashMap<>();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        subCategoryModel = arrayList_model_categories.get(position);


        holder.catName.setText(subCategoryModel.getsSubCatName());

        holder.catName.setChecked(arrayList_model_categories.get(position).getSelected());
        holder.catName.setTag(position);

        String strSubCategory = SharedPrefrence_Seller.getFixr_shop_Subcategoryid();
        Log.i("SUB_CATE", arrayList_model_categories.toString());


        holder.catName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    strSelectedService = SharedPrefrence_Seller.getFixr_shop_categoryid();
                    //Toast.makeText(context,strSelectedService,Toast.LENGTH_LONG).show();
                    sb = new StringBuilder();
                    Log.d("subCatehoryList",""+arrayList_model_categories.size());
                    Log.d("subcatvalues",arrayList_model_categories.toString());
                    Integer pos = (Integer) holder.catName.getTag();
                    /*arrayList_model_categories = category_list.get(position).getSubCategoryModelList();
                    subCategoryModel = arrayList_model_categories.get(position);*/
                    subCategoryModel = arrayList_model_categories.get(position);

                    req = split;
                    if (!(subCategoryModel == null)) {
                        if (subCategoryModel.getSelected()) {
                            arrayList_model_categories.remove(pos);
                            String split1 = "";
                            subCategoryModel = arrayList_model_categories.get(pos);
                            arrayList_model_categories.get(pos).setSelected(true);
                            Toast.makeText(context,subCategoryModel.getsSubID(),Toast.LENGTH_SHORT).show();
                            //split+=subCategoryModel.getsSubID()+"";
                            //split +=subCategoryModel.getsSubID();
                            //String split1 = subCategoryModel.getsSubID();
                            subCategoryModel.setSelected(false);
                            for (int count = 0; count < arrayList_model_categories.size(); count++) {
                                if (arrayList_model_categories.get(count).getSelected() == true) {
                                    split1 += arrayList_model_categories.get(count).getsSubID() + ",";
                                }
                            }
                            if (subCategoryModel.getCheckLevel().equals("level_3")) {
                                if (hashMapListChooseSubCat == null) {

                                } else {
                                    hashMapListChooseSuperSubCat.removeAll(subCategoryModel.getsSubID());
                                }
                                SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSuperSubCategoryId(context, String.valueOf(hashMapListChooseSuperSubCat));
                            }
                            myMultimap.remove(subCategoryModel.getStrSubPosition_key(), subCategoryModel.getsSubID());
                            Log.i("MyMAP", "" + myMultimap);
                            Toast.makeText(context, split1, Toast.LENGTH_LONG).show();

                            SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSubCategoryId(context, subCategoryModel.getsSubID());

                        } else {


                            subCategoryModel.setSelected(true);
                            myMultimap.put(subCategoryModel.getStrSubPosition_key(), subCategoryModel.getsSubID());
                            Log.i("MyMAP", "" + myMultimap);

                            //concatenate with comma
                            split += subCategoryModel.getsSubID() + ",";

                            //null check split string and remove comma in last
                            if ((split != null) && (split.length() > 0)) {
                                SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSubCategoryId(context, split.substring(0, split.length() - 1));
                            }

                            //Toast.makeText(context, split, Toast.LENGTH_LONG).show();

                            if (subCategoryModel.getCheckLevel().equals("level_3")) {

                                openSuperSubCate(context, subCategoryModel.getsSubID());
                            }

                        }
                    }

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arrayList_model_categories ? arrayList_model_categories.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox catName;

        public ViewHolder(View itemView) {
            super(itemView);
            catName = (CheckBox) itemView.findViewById(R.id.txt_CatName);
        }
    }

    private void openSuperSubCate(final Context context, final String subCatID) {


        final Dialog dialogSelectService = new Dialog(view.getRootView().getContext());
        dialogSelectService.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSelectService.setContentView(R.layout.layout_super_sub_category_info);
        dialogSelectService.setCanceledOnTouchOutside(false);
        dialogSelectService.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialogSelectService.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        SharedPrefrence_Seller.getSharedPrefrencesFixerShopSuperSubCategoryId(view.getRootView().getContext());
        Button close_button = dialogSelectService.findViewById(R.id.close_button);
        rvSelectSuperService = dialogSelectService.findViewById(R.id.rvSelectSuperService);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvSelectSuperService.getContext(), gridLayoutManager.getOrientation());
        rvSelectSuperService.setHasFixedSize(true);
        rvSelectSuperService.setLayoutManager(gridLayoutManager);
        rvSelectSuperService.addItemDecoration(dividerItemDecoration);
        superSubCategoryModelsList = new ArrayList<>();
        hashMapListChooseSuperSubCat = ArrayListMultimap.create();


        func_fetch_CategoryAPI(dialogSelectService, view.getRootView().getContext(), rvSelectSuperService, subCatID);

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


              /*  String strSelectedSuperService = SharedPrefrence_Seller.getFixr_shop_SuperSubcategoryid().replace("[","").replace("]","");
                strSelectedSuperService = strSelectedSuperService.replaceAll("\\s+","");*/
                String strSelectedSuperService = SharedPrefrence_Seller.getFixr_shop_SuperSubcategoryid();
                strSelectedService = strSelectedService + strSelectedSuperService + ",";

                SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSuperSubCategoryId(view.getRootView().getContext(), strSelectedService);
                Log.i("Shekhar", strSelectedService);
                dialogSelectService.dismiss();
            }
        });

    }

    private void func_fetch_CategoryAPI(final Dialog dialogSelectService, final Context context, final RecyclerView rvSelectSuperService, final String subCatID) {
        requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_SALES_FETCH_SUPER_CATEGORY_ITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String strStatau = obj.getString("status");
                            if (strStatau.equals("1")) {
                                dialogSelectService.show();
                                String str = obj.getString("data");
                                superSubCategoryModelsList.clear();
                                JSONArray jsonArray = new JSONArray(str);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsoObject = jsonArray.getJSONObject(i);
                                    SuperSubCategoryModel superSubCategoryModel = new SuperSubCategoryModel();
                                    superSubCategoryModel.setsSuperSubID(jsoObject.getString("id"));
                                    superSubCategoryModel.setsSuperSubCatName(jsoObject.getString("super_sub_cat_name"));
                                    superSubCategoryModel.setPosition_key(jsoObject.getString("position_key"));
                                    superSubCategoryModelsList.add(superSubCategoryModel);
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        rvSelectSuperService.setAdapter(adapter_category);
//                        adapter_category.notifyDataSetChanged();

                        adapter_category = new Adapter_Super_Sub_Category(view.getRootView().getContext(), superSubCategoryModelsList, hashMapListChooseSuperSubCat);
                        rvSelectSuperService.setAdapter(adapter_category);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            Toast.makeText(context, "It took longer than expected to get the response from Server.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Something went wrong ! Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sub_cat_id", subCatID);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQue(stringRequest);
    }

}

