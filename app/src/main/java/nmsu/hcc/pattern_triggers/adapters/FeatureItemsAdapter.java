package nmsu.hcc.pattern_triggers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nmsu.hcc.pattern_triggers.R;
import nmsu.hcc.pattern_triggers.databinding.AdapterFeatureItemsBinding;
import nmsu.hcc.pattern_triggers.model.FeatureMapping;

public class FeatureItemsAdapter extends RecyclerView.Adapter<FeatureItemsAdapter.ViewHolder> {

    private Context context;
    /*OnItemClickListener onItemClickListener;*/

    ArrayList<FeatureMapping> featureMappingList;

    public FeatureItemsAdapter(Context context, ArrayList<FeatureMapping> featureMappingList) {
        this.context = context;
        this.featureMappingList = featureMappingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_feature_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if(featureMappingList.get(position).getAlphabet()!=null){
            viewHolder.adapterFeatureItemsBinding.tvUpdate.setText("Update");
        } else {
            viewHolder.adapterFeatureItemsBinding.tvUpdate.setText("Set");
        }

        //viewHolder.openHoursBinding.tvWeekday.setText(openHoursList.get(position).getWeekday());
        //viewHolder.openHoursBinding.tvOpenHour.setText(openHoursList.get(position).getStartTime() + " : " + openHoursList.get(position).getEndTime());
    }

    @Override
    public int getItemCount() {
        return featureMappingList.size();
    }

    /*public interface OnItemClickListener{
        void onItemClicked(int position, KeywordSearchResponse.Hit location);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {

        AdapterFeatureItemsBinding adapterFeatureItemsBinding;

        public ViewHolder(AdapterFeatureItemsBinding adapterFeatureItemsBinding) {
            super(adapterFeatureItemsBinding.getRoot());
            this.adapterFeatureItemsBinding = adapterFeatureItemsBinding;

        }
    }


}


