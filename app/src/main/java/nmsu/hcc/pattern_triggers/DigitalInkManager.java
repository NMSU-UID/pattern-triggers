package nmsu.hcc.pattern_triggers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.vision.digitalink.DigitalInkRecognition;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions;
import com.google.mlkit.vision.digitalink.Ink;

public class DigitalInkManager {
    Ink.Builder inkBuilder = Ink.builder();
    Ink.Stroke.Builder strokeBuilder;
    Ink ink;

    DigitalInkRecognitionModelIdentifier modelIdentifier = null;
    DigitalInkRecognitionModel model;
    DigitalInkRecognizer recognizer;

    RemoteModelManager remoteModelManager = RemoteModelManager.getInstance();

    private static final DigitalInkManager digitalInkManagerInstance = new DigitalInkManager();

    public static DigitalInkManager getInstance() {
        return digitalInkManagerInstance;
    }

    private DigitalInkManager() { }

    public void initMLModel(){
        // Specify the recognition model for a language
        try {
            modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US");
        } catch (MlKitException e) {
            // language tag failed to parse, handle error.
        }

        model = DigitalInkRecognitionModel.builder(modelIdentifier).build();

        // Get a recognizer for the language
        recognizer =
                DigitalInkRecognition.getClient(
                        DigitalInkRecognizerOptions.builder(model).build());

        remoteModelManager.isModelDownloaded(model).addOnCompleteListener(task -> {
            if (!task.getResult()) {
                remoteModelManager
                        .download(model, new DownloadConditions.Builder().build())
                        .addOnSuccessListener(aVoid -> Log.e("DigitalInkManager", "Model downloaded"))
                        .addOnFailureListener(
                                e -> Log.e("DigitalInkManager", "Error while downloading a model: " + e));
            } else {
                Log.e("DigitalInkManager", "Model already exists");
            }
        });

    }

}
