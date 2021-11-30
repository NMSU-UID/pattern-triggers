package nmsu.hcc.pattern_triggers.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nmsu.hcc.pattern_triggers.R;
import nmsu.hcc.pattern_triggers.databinding.AdapterAlphabetListBinding;
import nmsu.hcc.pattern_triggers.model.Alphabet;

public class AlphabetListAdapter extends RecyclerView.Adapter<AlphabetListAdapter.ViewHolder> {

    private Context context;
    OnItemClickListener onItemClickListener;
    int selectedItemPosition;

    ArrayList<Alphabet> alphabetArrayList;

    public AlphabetListAdapter(Context context, ArrayList<Alphabet> alphabetArrayList, int selectedItemPosition) {
        this.context = context;
        this.alphabetArrayList = alphabetArrayList;
        this.selectedItemPosition = selectedItemPosition;
        Log.e("AlphabetListAdapter", "Array size: "+alphabetArrayList.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_alphabet_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if(alphabetArrayList.get(position).getAlphabetId()==0){
            viewHolder.adapterAlphabetListBinding.tvAlphabet.setVisibility(View.GONE);
            viewHolder.adapterAlphabetListBinding.tvAlphabet.setText("");
            viewHolder.adapterAlphabetListBinding.ivNothing.setVisibility(View.VISIBLE);
        } else {
            viewHolder.adapterAlphabetListBinding.tvAlphabet.setVisibility(View.VISIBLE);
            viewHolder.adapterAlphabetListBinding.tvAlphabet.setText(alphabetArrayList.get(position).getAlphabetName());
            viewHolder.adapterAlphabetListBinding.ivNothing.setVisibility(View.GONE);
        }

        if(position==selectedItemPosition){
            viewHolder.adapterAlphabetListBinding.flBorder.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_primary));
        } else {
            viewHolder.adapterAlphabetListBinding.flBorder.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_light));
        }

        viewHolder.adapterAlphabetListBinding.getRoot().setOnClickListener(view -> {
            this.selectedItemPosition = position;
            notifyDataSetChanged();
            onItemClickListener.onItemClicked(position, position==0 ? null:alphabetArrayList.get(position));
        });

        //viewHolder.openHoursBinding.tvWeekday.setText(openHoursList.get(position).getWeekday());
        //viewHolder.openHoursBinding.tvOpenHour.setText(openHoursList.get(position).getStartTime() + " : " + openHoursList.get(position).getEndTime());
    }

    @Override
    public int getItemCount() {
        return alphabetArrayList.size();
    }

    public interface OnItemClickListener{
        void onItemClicked(int position, Alphabet alphabet);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AdapterAlphabetListBinding adapterAlphabetListBinding;

        public ViewHolder(AdapterAlphabetListBinding adapterAlphabetListBinding) {
            super(adapterAlphabetListBinding.getRoot());
            this.adapterAlphabetListBinding = adapterAlphabetListBinding;

        }
    }


}


