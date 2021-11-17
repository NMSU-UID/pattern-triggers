package nmsu.hcc.pattern_triggers;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;

public interface ImageGetListener {
    void successfullyGetImage(File imageFile, Bitmap imageBitmap) throws FileNotFoundException;

    void failToGetImage(String message);
}
