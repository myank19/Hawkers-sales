package io.goolean.tech.hawker.sales.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.common.collect.ListMultimap;
import java.util.List;
import io.goolean.tech.hawker.sales.Model.QuestionCheckModel;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Seller;

public class Question_Adapter extends RecyclerView.Adapter<Question_Adapter.ViewHolder>  {
    View view;
    private Context context;
    private List<QuestionCheckModel> lquestionCheckModels;
    ListMultimap<String,String> myMultimap;

    public Question_Adapter(Context activity, List<QuestionCheckModel> movieList, ListMultimap<String,String> myMultimap) {
        this.context=activity;
        this.lquestionCheckModels= movieList;
        this.myMultimap = myMultimap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.list_design_subcatergory, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final QuestionCheckModel  questionCheckModel= lquestionCheckModels.get(position);
        holder.tvQuestion.setText(questionCheckModel.getsQuestion());
        holder.catName.setChecked(lquestionCheckModels.get(position).isSelected());
        holder.catName.setTag(position);
        holder.catName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer pos = (Integer) holder.catName.getTag();
                if (lquestionCheckModels.get(pos).isSelected()) {
                    lquestionCheckModels.get(pos).setSelected(false);
                    myMultimap.remove("Question",lquestionCheckModels.get(pos).getsQuestionID());
                    SharedPrefrence_Seller.saveHawkerQuestionData(context, String.valueOf(myMultimap));
                } else {
                    lquestionCheckModels.get(pos).setSelected(true);
                    myMultimap.put("Question",lquestionCheckModels.get(pos).getsQuestionID());
                    SharedPrefrence_Seller.saveHawkerQuestionData(context, String.valueOf(myMultimap));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != lquestionCheckModels ? lquestionCheckModels.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox catName;
        public TextView tvQuestion;
        public ViewHolder(View itemView) {
            super(itemView);
            catName = (CheckBox)itemView.findViewById(R.id.txt_CatName);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
        }
    }
}
