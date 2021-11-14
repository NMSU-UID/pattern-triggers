package nmsu.hcc.pattern_triggers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnOpenApplication).setOnClickListener(view -> {
            //LaunchApplicationHelper.openApplication(MainActivity.this, "com.sec.android.app.camera");
            //LaunchApplicationHelper.openApplication(MainActivity.this, LaunchApplicationHelper.getCameraPackageName(MainActivity.this));
            LaunchApplicationHelper.switchFlashLight(MainActivity.this, true);
        });

        findViewById(R.id.btnTurnOffTorch).setOnClickListener(view -> {
            LaunchApplicationHelper.switchFlashLight(MainActivity.this, false);
        });

    }
}