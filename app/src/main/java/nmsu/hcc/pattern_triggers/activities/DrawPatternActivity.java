package nmsu.hcc.pattern_triggers.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import nmsu.hcc.pattern_triggers.DrawingView;
import nmsu.hcc.pattern_triggers.LaunchApplicationHelper;
import nmsu.hcc.pattern_triggers.R;
import nmsu.hcc.pattern_triggers.listeners.ParsedTextListener;

public class DrawPatternActivity extends ImageActivity {

    DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void takeAction(String s){
        switch (s) {
            case "M":
                LaunchApplicationHelper.switchFlashLight(this, true);
                break;
            case "N":
                LaunchApplicationHelper.switchFlashLight(this, false);
                break;
            case "B":
                LaunchApplicationHelper.openApplication(this, "com.android.chrome");
                //LaunchApplicationHelper.launchApp(this, "com.android.chrome");
                break;
            case "V":
            case "v":
                LaunchApplicationHelper.openApplication(this, "com.google.android.youtube");
                //LaunchApplicationHelper.launchApp(this, "com.google.android.youtube");
                break;
            default:
                Toast.makeText(this, "Did not matched with anything", Toast.LENGTH_LONG).show();
                break;
        }
    }

}