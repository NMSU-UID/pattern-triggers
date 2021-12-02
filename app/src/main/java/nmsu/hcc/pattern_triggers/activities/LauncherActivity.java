package nmsu.hcc.pattern_triggers.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import nmsu.hcc.pattern_triggers.DigitalInkManager;
import nmsu.hcc.pattern_triggers.LocalStorage;
import nmsu.hcc.pattern_triggers.R;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_activity);

        DigitalInkManager.getInstance().initMLModel();

        if(LocalStorage.getInstance().getSavedFeatureMapping(this)!=null){
            if(LocalStorage.getInstance().getSavedFeatureMapping(this).size()!=0){
                startActivity(new Intent(LauncherActivity.this, DrawPatternActivity.class));
                finish();
            } else {
                new Handler().postDelayed(() -> {
                    Intent mainIntent = new Intent(LauncherActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }, 1500);
            }
        } else {
            new Handler().postDelayed(() -> {
                Intent mainIntent = new Intent(LauncherActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }, 1500);
        }
    }
}