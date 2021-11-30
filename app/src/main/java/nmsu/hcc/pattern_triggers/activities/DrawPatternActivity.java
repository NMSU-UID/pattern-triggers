package nmsu.hcc.pattern_triggers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import nmsu.hcc.pattern_triggers.DrawingView;
import nmsu.hcc.pattern_triggers.LaunchApplicationHelper;
import nmsu.hcc.pattern_triggers.LocalStorage;
import nmsu.hcc.pattern_triggers.R;
import nmsu.hcc.pattern_triggers.listeners.ParsedTextListener;

import static nmsu.hcc.pattern_triggers.LocalStorage.FEATURE_GOOGLE_CHROME;
import static nmsu.hcc.pattern_triggers.LocalStorage.FEATURE_TURN_OFF_TORCH;
import static nmsu.hcc.pattern_triggers.LocalStorage.FEATURE_TURN_ON_TORCH;
import static nmsu.hcc.pattern_triggers.LocalStorage.FEATURE_YOUTUBE;

public class DrawPatternActivity extends ImageActivity implements PopupMenu.OnMenuItemClickListener {

    DrawingView drawingView;
    TextView tvMenu;
    PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideToolbar();
        setContentView(R.layout.activity_draw_pattern);

        drawingView = findViewById(R.id.llCanvas);
        tvMenu = findViewById(R.id.tvMenu);

        drawingView.getParsedTextListener(text -> {
            Log.e("DrawPatternActivity", "Parsed Text: "+text);
            takeAction(text);
            finish();
        });


        popupMenu = new PopupMenu(this, tvMenu);
        popupMenu.getMenu().add(1, R.id.settings, 1, "Settings");
        popupMenu.setOnMenuItemClickListener(DrawPatternActivity.this);

        tvMenu.setOnClickListener(view -> {
            popupMenu.show();
        });

    }

    private void hideToolbar() {
        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException ignored){}
    }

    private void takeAction(String s){
        int featureId = LocalStorage.getInstance().getFeatureIdByAlphabetName(this, s);
        switch (featureId) {
            case FEATURE_GOOGLE_CHROME:
                LaunchApplicationHelper.openApplication(this, "com.android.chrome");
                break;
            case FEATURE_YOUTUBE:
                LaunchApplicationHelper.openApplication(this, "com.google.android.youtube");
                break;
            case FEATURE_TURN_ON_TORCH:
                LaunchApplicationHelper.switchFlashLight(this, true);
                break;
            case FEATURE_TURN_OFF_TORCH:
                LaunchApplicationHelper.switchFlashLight(this, false);
                break;
            default:
                Toast.makeText(this, "Did not matched with anything", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return true;
    }
}