package nmsu.hcc.pattern_triggers.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import nmsu.hcc.pattern_triggers.LaunchApplicationHelper;
import nmsu.hcc.pattern_triggers.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnSettings).setOnClickListener(view -> {
            //LaunchApplicationHelper.openApplication(MainActivity.this, "com.sec.android.app.camera");
            //LaunchApplicationHelper.openApplication(MainActivity.this, LaunchApplicationHelper.getCameraPackageName(MainActivity.this));
            //LaunchApplicationHelper.switchFlashLight(MainActivity.this, true);
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });

        findViewById(R.id.btnTurnOffTorch).setOnClickListener(view -> {
            LaunchApplicationHelper.switchFlashLight(MainActivity.this, false);
        });

    }

}