package nmsu.hcc.pattern_triggers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.io.FileNotFoundException;

public class DrawPatternActivity extends ImageActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_pattern);

        findViewById(R.id.btnOpenGallery).setOnClickListener(view -> {
            getImageFromGallery(new ImageGetListener() {
                @Override
                public void successfullyGetImage(File imageFile, Bitmap imageBitmap) throws FileNotFoundException {
                    InputImage image = InputImage.fromBitmap(imageBitmap, 0);
                    TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
                    Task<Text> result = recognizer.process(image)
                                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                                        @Override
                                        public void onSuccess(Text visionText) {
                                            // Task completed successfully
                                            // ...
                                            Log.e("Parsed Text", "Parsed Text: "+visionText.getText());
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

                @Override
                public void failToGetImage(String message) {

                }
            });
        });

    }
}