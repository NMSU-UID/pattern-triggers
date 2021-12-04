package nmsu.hcc.pattern_triggers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import nmsu.hcc.pattern_triggers.R;

public class MainActivity extends AppCompatActivity {

    LinearLayout llGif;
    ImageView ivCross;
    ImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        llGif = findViewById(R.id.llGif);
        ivCross = findViewById(R.id.ivCrossBtn);
        gifImageView = findViewById(R.id.gifView);

        findViewById(R.id.tvSettings).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });

        findViewById(R.id.tvTutorial).setOnClickListener(view -> {
            llGif.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .asGif()
                    .load(R.drawable.tutotial)
                    .into(gifImageView).clearOnDetach();
        });

        ivCross.setOnClickListener(view -> {
            llGif.setVisibility(View.GONE);
            Glide.with(gifImageView.getContext()).clear(gifImageView);
        });

    }

}