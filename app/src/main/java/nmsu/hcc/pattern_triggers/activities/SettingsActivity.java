package nmsu.hcc.pattern_triggers.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import nmsu.hcc.pattern_triggers.LocalStorage;
import nmsu.hcc.pattern_triggers.R;
import nmsu.hcc.pattern_triggers.adapters.AlphabetListAdapter;
import nmsu.hcc.pattern_triggers.adapters.FeatureItemsAdapter;
import nmsu.hcc.pattern_triggers.databinding.ActivitySettingsBinding;
import nmsu.hcc.pattern_triggers.model.FeatureMapping;

public class SettingsActivity extends AppCompatActivity {

    ArrayList<FeatureMapping> featureMappingArrayList = new ArrayList<>();
    ActivitySettingsBinding activitySettingsBinding;
    FeatureItemsAdapter featureItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Settings");
        activitySettingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        if(LocalStorage.getInstance().getSavedFeatureMapping(this) == null){
            featureMappingArrayList = LocalStorage.getInstance().getDefaultFeatureList();
        } else {
            featureMappingArrayList = LocalStorage.getInstance().getSavedFeatureMapping(this);
        }

        activitySettingsBinding.rvFeatureList.setLayoutManager(new LinearLayoutManager(this));
        featureItemsAdapter = new FeatureItemsAdapter(this, featureMappingArrayList);
        activitySettingsBinding.rvFeatureList.setAdapter(featureItemsAdapter);

        findViewById(R.id.tvTryIt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, DrawPatternActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}