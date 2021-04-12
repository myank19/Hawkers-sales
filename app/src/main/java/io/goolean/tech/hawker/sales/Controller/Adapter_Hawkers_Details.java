package io.goolean.tech.hawker.sales.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import io.goolean.tech.hawker.sales.Model.HawkersDetails;
import io.goolean.tech.hawker.sales.R;

public class Adapter_Hawkers_Details extends RecyclerView.Adapter<Adapter_Hawkers_Details.ViewHolder>  {
    private Context context;
    private List<HawkersDetails> hawkersDetailsList;
    private HawkersDetails hawkersDetails;


    public Adapter_Hawkers_Details(Context activity, ArrayList<HawkersDetails> hawkersDetailsList) {
        this.context=activity;
        this.hawkersDetailsList= hawkersDetailsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_hawkerdetail_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        hawkersDetails = hawkersDetailsList.get(position);
        holder.hawkerName.setText(hawkersDetails.getHawkername());
        holder.hawker_code.setText(hawkersDetails.getHawkercaode());
        holder.reg_date.setText(hawkersDetails.getRegdate());
        holder.hawker_mobile_num.setText(hawkersDetails.getHawker_mobilenum());


    }
    @Override
    public int getItemCount() {
        return (null != hawkersDetailsList ? hawkersDetailsList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView hawkerName;
        public TextView hawker_code;
        public TextView reg_date;
        public TextView hawker_mobile_num;

        public ViewHolder(View itemView) {
            super(itemView);
            hawkerName = itemView.findViewById(R.id.tv_name);
            hawker_code = itemView.findViewById(R.id.tv_hawker_code);
            reg_date = itemView.findViewById(R.id.tv_date);
            hawker_mobile_num = itemView.findViewById(R.id.tv_hawker_mobile);
        }
    }
}
