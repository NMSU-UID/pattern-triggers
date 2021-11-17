package nmsu.hcc.pattern_triggers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnOpenApplication).setOnClickListener(view -> {
            //LaunchApplicationHelper.openApplication(MainActivity.this, "com.sec.android.app.camera");
            //LaunchApplicationHelper.openApplication(MainActivity.this, LaunchApplicationHelper.getCameraPackageName(MainActivity.this));
            //LaunchApplicationHelper.switchFlashLight(MainActivity.this, true);
            startActivity(new Intent(MainActivity.this, DrawPatternActivity.class));
        });

        findViewById(R.id.btnTurnOffTorch).setOnClickListener(view -> {
            LaunchApplicationHelper.switchFlashLight(MainActivity.this, false);
        });

    }

    private void takeAction(String s){
        if(s.equals("M")){
            LaunchApplicationHelper.switchFlashLight(MainActivity.this, true);
        } else if(s.equals("N")){
            LaunchApplicationHelper.switchFlashLight(MainActivity.this, false);
        } else if(s.equals("B")){
            LaunchApplicationHelper.openApplication(MainActivity.this, "com.android.chrome");
        } else if(s.equals("V") || s.equals("v")){
            LaunchApplicationHelper.openApplication(MainActivity.this, "com.google.android.youtube");
        } else {
            Toast.makeText(this,"Did not matched with anything", Toast.LENGTH_LONG).show();
        }
    }

}