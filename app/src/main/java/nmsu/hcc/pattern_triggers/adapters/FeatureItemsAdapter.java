package nmsu.hcc.pattern_triggers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nmsu.hcc.pattern_triggers.AlphabetListDialog;
import nmsu.hcc.pattern_triggers.LocalStorage;
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

        String alphabetName = featureMappingList.get(position).getAlphabet()==null ? "" : featureMappingList.get(position).getAlphabet().getAlphabetName();
        viewHolder.adapterFeatureItemsBinding.tvAlphabet.setText(alphabetName);
        viewHolder.adapterFeatureItemsBinding.tvFeature.setText(featureMappingList.get(position).getFeatureName());

        viewHolder.adapterFeatureItemsBinding.tvUpdate.setOnClickListener(view -> {
            int selectedItemPosition = featureMappingList.get(position).getAlphabet()==null ? 0 : featureMappingList.get(position).getAlphabet().getAlphabetId();
            AlphabetListDialog alphabetListDialog = new AlphabetListDialog(
                    context,
                    LocalStorage.getInstance().getAlphabetList(),
                    selectedItemPosition,
                    featureMappingList.get(position).getFeatureName());
            alphabetListDialog.getButtonCLickListener((alphabet) -> {
                featureMappingList.get(position).setAlphabet(alphabet);
                notifyDataSetChanged();
                LocalStorage.getInstance().saveFeatureMapping(context, featureMappingList);
                alphabetListDialog.dismissDialog();
            });
            alphabetListDialog.setCancelable(true);
            alphabetListDialog.show();
        });

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


