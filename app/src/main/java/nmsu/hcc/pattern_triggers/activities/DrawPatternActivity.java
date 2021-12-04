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
import nmsu.hcc.pattern_triggers.network.ApiManager;
import nmsu.hcc.pattern_triggers.network.listeners.PerformanceTrackerListener;
import nmsu.hcc.pattern_triggers.network.response.PerformanceTrackingResponse;

import static nmsu.hcc.pattern_triggers.LocalStorage.FEATURE_GOOGLE_CHROME;
import static nmsu.hcc.pattern_triggers.LocalStorage.FEATURE_TURN_OFF_TORCH;
import static nmsu.hcc.pattern_triggers.LocalStorage.FEATURE_TURN_ON_TORCH;
import static nmsu.hcc.pattern_triggers.LocalStorage.FEATURE_YOUTUBE;

public class DrawPatternActivity extends ImageActivity implements PopupMenu.OnMenuItemClickListener {

    DrawingView drawingView;
    TextView tvMenu;
    PopupMenu popupMenu;
    ApiManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideToolbar();
        setContentView(R.layout.activity_draw_pattern);

        drawingView = findViewById(R.id.llCanvas);
        tvMenu = findViewById(R.id.tvMenu);

        apiManager = new ApiManager(this);

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

    private void takeAction(String alphabet){
        int featureId = LocalStorage.getInstance().getFeatureIdByAlphabetName(this, alphabet);
        boolean success = true;
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
                success = false;
                Toast.makeText(this, "Did not matched with anything", Toast.LENGTH_LONG).show();
                break;
        }

        callPerformanceTrackingApi(alphabet, success);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return true;
    }

    private void callPerformanceTrackingApi(String alphabet, boolean success){
        apiManager.performanceTracking(alphabet, success, new PerformanceTrackerListener() {
            @Override
            public void onSuccess(PerformanceTrackingResponse performanceTrackingResponse) {
                Log.e("DrawPatternActivity", "performanceTrackingResponse: success - " + performanceTrackingResponse.getCode());
            }

            @Override
            public void onFailed(String message, int responseCode) {
                Log.e("DrawPatternActivity", "performanceTrackingResponse: failed - " + responseCode + " : " + message);
            }

            @Override
            public void startLoading(String requestId) {

            }

            @Override
            public void endLoading(String requestId) {

            }
        });

    }
}