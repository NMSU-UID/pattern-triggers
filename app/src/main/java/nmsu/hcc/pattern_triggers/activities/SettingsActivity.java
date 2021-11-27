package nmsu.hcc.pattern_triggers.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import nmsu.hcc.pattern_triggers.R;
import nmsu.hcc.pattern_triggers.model.FeatureMapping;

public class SettingsActivity extends AppCompatActivity {

    ArrayList<FeatureMapping> featureMappingArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.btnTryIt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, DrawPatternActivity.class));
            }
        });
    }
}