package io.goolean.tech.hawker.sales.Controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.goolean.tech.hawker.sales.Model.HawkerDetailModel;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Storage.LocalStoreData;
import io.goolean.tech.hawker.sales.View.UpdateDistributionDetail;

public class HawkerDetailAdapter extends RecyclerView.Adapter<HawkerDetailAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private ArrayList<HawkerDetailModel> hawkerDetailModelArrayList;
    private HawkerDetailModel hawkerDetailModel;
    ArrayList<HawkerDetailModel> detailModelArrayList;
    LocalStoreData localStoreData;
    UpdateClick updateClick;

    public interface UpdateClick {
        void onUpdateClickListener(int position, HawkerDetailModel helper);
    }

    public void OnUpdateClickMethod(UpdateClick updateClick) {
        this.updateClick = updateClick;
    }

    public HawkerDetailAdapter(Context applicationContext, ArrayList<HawkerDetailModel> hawkerDetailModels) {
        context = applicationContext;
        this.detailModelArrayList = hawkerDetailModels;
        this.hawkerDetailModelArrayList = detailModelArrayList;
        localStoreData = new LocalStoreData(context);
        LocalStoreData.init(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listitem_hawker_detail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        hawkerDetailModel = hawkerDetailModelArrayList.get(position);
        holder.tvHawkerCode.setText(hawkerDetailModel.getHawker_code());
        holder.tvHawkerName.setText(hawkerDetailModel.getName() + "  (" + hawkerDetailModel.getMobile_no_contact() + ")" + "\n" + hawkerDetailModel.getBusiness_name());
        holder.tvDateTime.setText(hawkerDetailModel.getRegistered_time());
        holder.tv_location.setText(hawkerDetailModel.getHawker_register_address());
        holder.tvDistance.setText(hawkerDetailModel.getDistance() + " km ");

        holder.cardView_hawker_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateClick != null) {
                    updateClick.onUpdateClickListener(position, hawkerDetailModel);
                }

               /* String sHawkerCode = hawkerDetailModelArrayList.get(position).getHawker_code();
                localStoreData.saveHawkerCode(sHawkerCode);
                Intent intent = new Intent(context, UpdateDistributionDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != hawkerDetailModelArrayList ? hawkerDetailModelArrayList.size() : 0);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    hawkerDetailModelArrayList = detailModelArrayList;
                } else {
                    ArrayList<HawkerDetailModel> filteredList = new ArrayList<>();
                    for (HawkerDetailModel hawkerDetailModel : detailModelArrayList) {
                        if (hawkerDetailModel.getName().toLowerCase().contains(charString)) {
                            filteredList.add(hawkerDetailModel);
                        } else if (hawkerDetailModel.getMobile_no_contact().toLowerCase().contains(charString)) {
                            filteredList.add(hawkerDetailModel);
                        } else if (hawkerDetailModel.getsAll().toLowerCase().contains(charString)) {
                            filteredList.add(hawkerDetailModel);
                        }
                    }
                    hawkerDetailModelArrayList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = hawkerDetailModelArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                hawkerDetailModelArrayList = (ArrayList<HawkerDetailModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvHawkerName, tvDateTime, tv_location, tvHawkerCode, tvDistance;// init the item view's
        CardView cardView_hawker_detail;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvHawkerName = itemView.findViewById(R.id.tvHawkerName);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            tv_location = itemView.findViewById(R.id.tv_location);
            tvHawkerCode = itemView.findViewById(R.id.tvHawkerCode);
            cardView_hawker_detail = itemView.findViewById(R.id.cardView_hawker_detail);
            tvDistance = itemView.findViewById(R.id.tvDistance);
        }
    }
}