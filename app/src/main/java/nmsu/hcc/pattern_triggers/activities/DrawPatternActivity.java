package nmsu.hcc.pattern_triggers.activities;

import android.os.Bundle;
import android.util.Log;
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

public class DrawPatternActivity extends ImageActivity {

    DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideToolbar();
        setContentView(R.layout.activity_draw_pattern);

        drawingView = findViewById(R.id.llCanvas);
        drawingView.getParsedTextListener(new ParsedTextListener() {
            @Override
            public void parsedText(String text) {
                Log.e("DrawPatternActivity", "Parsed Text: "+text);
                takeAction(text);
                finish();
            }
        });

        /*drawingView.getLatestBitmapImage(new LatestBitmapImageListener() {
            @Override
            public void latestBitmapImage(Bitmap imageBitmap) {
                Log.e("latestBitmapImage", "bytes: "+String.valueOf(imageBitmap.getByteCount()));
                Log.e("latestBitmapImage", "height: "+String.valueOf(imageBitmap.getHeight()));
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, 1024, 764, false);
                imageView.setImageBitmap(scaledBitmap);

                InputImage image = InputImage.fromBitmap(scaledBitmap, 0);
                TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
                Task<Text> result = recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                // Task completed successfully
                                // ...
                                Log.e("Parsed Text", "Parsed Text: "+visionText.getText());
                                textView.setText(visionText.getText());
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                        Log.e("Parsed Text", String.valueOf(e));
                                    }
                                });
            }
        });

        findViewById(R.id.btnOpenGallery).setOnClickListener(view -> {
            getImageFromGallery(new ImageGetListener() {
                @Override
                public void successfullyGetImage(File imageFile, Bitmap imageBitmap) throws FileNotFoundException {
                    InputImage image = InputImage.fromBitmap(imageBitmap, 0);
                    TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
                    Task<Text> result = recognizer.process(image)
                                    .addOnSuccessListener(visionText -> {
                                        // Task completed successfully
                                        // ...
                                        Log.e("Parsed Text", "Parsed Text: "+visionText.getText());
                                        textView.setText(visionText.getText());
                                    })
                                    .addOnFailureListener(
                                            new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Task failed with an exception
                                                    // ...
                                                    Log.e("Parsed Text", String.valueOf(e));
                                                }
                                            });

                }

                @Override
                public void failToGetImage(String message) {

                }
            });
        });

         */

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

}