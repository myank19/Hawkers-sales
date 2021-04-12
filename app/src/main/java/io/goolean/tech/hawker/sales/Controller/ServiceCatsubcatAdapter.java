package io.goolean.tech.hawker.sales.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.goolean.tech.hawker.sales.Model.Model_Item_Category;
import io.goolean.tech.hawker.sales.Model.SubCategoryModel;
import io.goolean.tech.hawker.sales.R;

import static android.content.Context.MODE_PRIVATE;

public class ServiceCatsubcatAdapter extends BaseExpandableListAdapter {
    Context context;
    List<Model_Item_Category> item_categoryList;
    ArrayList<ArrayList<SubCategoryModel>> item_subcatList;
    int updateIndex, grpIndex;
    List<String> subitemsIdList;
    List<String> catitemsIdList;
    List<Boolean> mybooleanList;
    HashSet<String> hashSet;
    SharedPreferences.Editor editor;
    String catitemsIdStr = "", subitemsIdStr = "";
    ExpandableListView expandableListView;
    boolean flagGrpunchk = false;
    SubCategoryModel subCategoryModel;


    public ServiceCatsubcatAdapter(Context context, List<Model_Item_Category> item_categoryList, ArrayList<ArrayList<SubCategoryModel>> item_subcatList,
                                   ExpandableListView expandableListView) {
        this.context = context;
        this.item_categoryList = item_categoryList;
        this.item_subcatList = item_subcatList;
        this.expandableListView = expandableListView;
        subitemsIdList = new ArrayList<String>();
        catitemsIdList = new ArrayList<String>();
        mybooleanList = new ArrayList<Boolean>();
        hashSet = new HashSet<String>();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public int getGroupCount() {
        return item_categoryList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (item_subcatList.get(groupPosition).size() > 0) {
            return item_subcatList.get(groupPosition).size();
        } else {
            return 0;
        }
    }

    @Override
    public Model_Item_Category getGroup(int groupPosition) {
        return item_categoryList.get(groupPosition);
    }

    @Override
    public SubCategoryModel getChild(int groupPosition, int childPosition) {
        return item_subcatList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return item_categoryList.get(groupPosition).hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        Model_Item_Category model_item_category = (Model_Item_Category) getGroup(groupPosition);
        ViewHolder holder = null;


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_group_layout, null);

            holder = new ViewHolder();
            holder.groupTv = (TextView) convertView.findViewById(R.id.grouptxt);
            holder.grpCheck = (CheckBox) convertView.findViewById(R.id.grpcheck);
            //holder.groupselectIv = (ImageView) convertView.findViewById(R.id.grpselectimg);
            //   holder.alamat=(TextView)convertView.findViewById(R.id.address);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.grpCheck.setText(model_item_category.getCatname());
        //expandableListView.setGroupIndicator(null);


        holder.grpCheck.setOnCheckedChangeListener(null);

        if (item_categoryList.get(groupPosition).getSelected()) {

            holder.grpCheck.setChecked(true);

        } else {
            holder.grpCheck.setChecked(false);
        }

        final ViewHolder finalHolder = holder;
        holder.grpCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    Model_Item_Category model_item_category = (Model_Item_Category) getGroup(groupPosition);
                    item_categoryList.get(groupPosition).setSelected(isChecked);

                    if (isChecked == true) {
                        hashSet.add(model_item_category.getId());
                        model_item_category.setSelected(true);
                        catitemsIdStr = hashSet.toString().replaceAll("\\[|\\]", "");
                        Log.d("catitemsIdListStrr", "catList:" + catitemsIdStr);

                    } else {
                        hashSet.remove(model_item_category.getId());
                        model_item_category.setSelected(false);

                        catitemsIdStr = hashSet.toString().replaceAll("\\[|\\]", "");
                        Log.d("catitemsIdListStrr", "catList:" + catitemsIdStr);

                        subitemsIdList.remove(subCategoryModel.getsSubID());

                        for (int i=0;i<item_subcatList.get(groupPosition).size();i++) {
                            if (subCategoryModel.getSelected()==true) {
                                subCategoryModel.setSelected(false);
                                finalHolder.childCheck.setSelected(false);
                                finalHolder.childCheck.setChecked(false);
                                subitemsIdList.remove(subCategoryModel.getsSubID());
                                //Toast.makeText(context,"child removed",Toast.LENGTH_SHORT).show();
                            }
                        }
                        subitemsIdStr = convertListtoString(subitemsIdList);
                        notifyDataSetChanged();

                    }

                    editor = context.getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
                    editor.putString("catitem", catitemsIdStr);
                    editor.apply();
                    Log.d("catitemsIdStr", "itemListstr: " + catitemsIdStr);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.grpCheck.setChecked(model_item_category.getSelected());

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
         subCategoryModel = getChild(groupPosition, childPosition);
        ViewHolder holder = null;
        updateIndex = childPosition;
        grpIndex = groupPosition;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_child_layout, null);

            holder = new ViewHolder();
            holder.childCheck = (CheckBox) convertView.findViewById(R.id.childCheck);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.childCheck.setText(subCategoryModel.getsSubCatName());

        holder.childCheck.setOnCheckedChangeListener(null);


        if (item_subcatList.get(groupPosition).get(childPosition).getSelected()) {
            holder.childCheck.setChecked(true);
        } else {
            holder.childCheck.setChecked(false);
        }
        final ViewHolder finalHolder = holder;
        holder.childCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {

                    SubCategoryModel subCategoryModel = getChild(groupPosition, childPosition);

                    item_subcatList.get(groupPosition).get(childPosition).setSelected(isChecked);


                    if (item_categoryList.get(groupPosition).getSelected() == true) {

                        if (isChecked == true) {
                            subitemsIdList.add(subCategoryModel.getsSubID());
                            subCategoryModel.setSelected(true);
                            subitemsIdStr = convertListtoString(subitemsIdList);
                            Log.d("subitemsIdList", "itemList: " + subitemsIdList.toString());
                            Log.d("mybooleanList", "booleanlist" + item_subcatList.get(groupPosition).get(childPosition).getSelected());
                        } else {
                            subitemsIdList.remove(subCategoryModel.getsSubID());
                            subCategoryModel.setSelected(false);

                            subitemsIdStr = convertListtoString(subitemsIdList);
                            Log.d("subitemsIdList", "itemList: " + subitemsIdList.toString());
                            Log.d("mybooleanList", "booleanlist" + item_subcatList.get(groupPosition).get(childPosition).getSelected());
                        }

                    } else {
                        Toast.makeText(context, "Please select group item", Toast.LENGTH_SHORT).show();

                        finalHolder.childCheck.setSelected(false);
                        subCategoryModel.setSelected(false);
                        finalHolder.childCheck.setChecked(false);
                        subitemsIdList.remove(subCategoryModel.getsSubID());
                        SubCategoryModel subCategoryModel1 = getChild(groupPosition, childPosition);
//                        for (int i=0;i<item_subcatList.get(groupPosition).size();i++) {
//                            if (subCategoryModel1.getSelected()==true) {
//                                subCategoryModel1.setSelected(false);
//                                finalHolder.childCheck.setSelected(false);
//                                finalHolder.childCheck.setChecked(false);
//                                subitemsIdList.remove(subCategoryModel.getsSubID());
//                                //Toast.makeText(context,"child removed",Toast.LENGTH_SHORT).show();
//                            }
//                        }
                       subitemsIdStr = convertListtoString(subitemsIdList);
//                        notifyDataSetChanged();
                    }

                    Log.d("banandc",subitemsIdList.toString());

                    editor = context.getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
                    editor.putString("subitem", subitemsIdStr);
                    editor.apply();
                    Log.d("subitemsIdStr", "itemListstr: " + subitemsIdStr);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        if (item_categoryList.get(groupPosition).getSelected() == true) {

            holder.childCheck.setChecked(subCategoryModel.getSelected());
        } else {
            holder.childCheck.setChecked(false);
        }

        //notifyDataSetChanged();

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        if (item_subcatList.get(groupPosition).size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    static class ViewHolder {
        //RelativeLayout selectedLayout;
        TextView groupTv;
        CheckBox childCheck, grpCheck;
        ImageView groupselectIv;
    }

    public String convertListtoString(List<String> subitemsIdList) {
        StringBuilder sb = new StringBuilder();

        int i = 0;
        while (i < subitemsIdList.size() - 1) {
            sb.append(subitemsIdList.get(i));
            sb.append(",");
            i++;
        }
        sb.append(subitemsIdList.get(i));
        String res = sb.toString();

        return res;
    }
}


