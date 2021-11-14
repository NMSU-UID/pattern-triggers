package nmsu.hcc.pattern_triggers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public abstract class ImageActivity extends AppCompatActivity {

    private static final String TAG = "ImageActivity";
    private ImageGetListener imageGetListener;
    private String imageDirName;
    private File imageFile;
    private String imageFilePath;
    private int SCALE_IMAGE = 4;
    private String tempImagePath;
    private String imageName;

    private final int IMAGE_CAPTURE_PERMISSION = 300;
    private final int GALLERY_IMAGE_REQUEST = 301;
    private final int WRITE_EXTERNAL_STORAGE = 302;
    private final int WRITE_EXTERNAL_STORAGE_FOR_GALLERY = 303;
    private final int CAMERA_REQUEST = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.imageDirName = setImageDirName();
    }

    protected String setImageDirName() {
        return "skoline";
    }

    protected void getImageFromGallery(ImageGetListener imageGetListener) {
        this.imageGetListener = imageGetListener;
        imageName = new String();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_FOR_GALLERY);
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_IMAGE_REQUEST);
                }
            } else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_IMAGE_REQUEST);
            }
        } catch (Exception ex) {
            Log.e(TAG, "dispatchTakePictureIntent: " + ex.getMessage().toString());
            try {
                imageGetListener.failToGetImage(ex.getMessage().toString());
            } catch (Exception e) {
            }
        }
    }

    protected void getImageFromGallery(String imageName, ImageGetListener imageGetListener) {
        this.imageGetListener = imageGetListener;
        this.imageName = imageName;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_FOR_GALLERY);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, GALLERY_IMAGE_REQUEST);

                }
            } else {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLERY_IMAGE_REQUEST);
            }
        } catch (Exception ex) {
            Log.e(TAG, "dispatchTakePictureIntent: " + ex.getMessage().toString());
            try {
                imageGetListener.failToGetImage(ex.getMessage().toString());
            } catch (Exception e) {
            }
        }
    }

    protected void getImageFromCamera(ImageGetListener imageGetListener) {
        getImageFromCamera(new String(), imageGetListener);
    }

    protected void getImageFromCamera(String imageName, ImageGetListener imageGetListener) {
        this.imageGetListener = imageGetListener;
        this.imageName = imageName;
        File photoFile = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_CAPTURE_PERMISSION);
                } else {
                    photoFile = createImage();
                }
            } else {
                photoFile = createImage();
            }
            dispatchTakePictureIntent(photoFile);
        } catch (Exception ex) {
            Log.e(TAG, "dispatchTakePictureIntent: " + ex.getMessage().toString());
            try {
                imageGetListener.failToGetImage(ex.getMessage().toString());
            } catch (Exception e) {
            }
        }

    }

    private void dispatchTakePictureIntent(File photoFile) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, getPackageName() + "" +
                        ".fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo
                        .SCREEN_ORIENTATION_LOCKED);
                takePictureIntent.putExtra("android.intent.extras.QUALITY_HIGH", 1);
                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities
                        (takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, photoURI, Intent
                            .FLAG_GRANT_WRITE_URI_PERMISSION | Intent
                            .FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    private File createImage() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
                } else {
                    return createImageFile();
                }
            } else {
                return createImageFile();
            }
        } catch (IOException ex) {
            Log.e(TAG, "dispatchTakePictureIntent: " + ex.getMessage().toString());
            try {
                imageGetListener.failToGetImage(ex.getMessage().toString());
            } catch (Exception e) {
            }
        }
        return null;
    }

    private File createImageFile() throws IOException {
        imageName = imageName.isEmpty() ? new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) : imageName;
        File dir = new File(Environment.getExternalStorageDirectory() + "/" + imageDirName + "/Temp");
        File image = File.createTempFile(imageName, ".jpg", dir);
        imageFile = image;
        tempImagePath = imageFile.getAbsolutePath();
        return image;
    }

    /*Save the image bitmap to image directory*/
    private void saveImage(Bitmap bitmap) {
        String tempImageName = imageName = imageName.isEmpty() ? System.currentTimeMillis() + "_" + new Random().nextInt() + ".jpg" : imageName + ".jpg";
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + imageDirName + "/Image";
        File dir = new File(file_path);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, tempImageName);
        imageFile = file;
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            Log.e(TAG, "saveImage: " + e.getMessage().toString());
        }
        imageFilePath = file_path + "/" + tempImageName;
    }


    /*After getting image check the orientation of the image*/
    private Bitmap checkRotation(String photoPath, Bitmap bitmap) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(bitmap, 270);
        }
        return bitmap;
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /*All Kind of permission related stuff*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            switch (requestCode) {
                case IMAGE_CAPTURE_PERMISSION:
                case WRITE_EXTERNAL_STORAGE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        dispatchTakePictureIntent(createImage());
                    } else {
                        Log.e(TAG, "onRequestPermissionsResult: Permission granted");
                        imageGetListener.failToGetImage("You have to accept permission to continue");
                    }
                    return;
                }
                case WRITE_EXTERNAL_STORAGE_FOR_GALLERY: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_IMAGE_REQUEST);
                    } else {
                        Log.e(TAG, "onRequestPermissionsResult: Permission granted");
                        imageGetListener.failToGetImage("You have to accept permission to continue");
                    }
                    return;
                }

            }
        } catch (Exception e) {
        }
    }

    /*After Image capture*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap getImage = BitmapFactory.decodeFile(tempImagePath);
                    getImage = Bitmap.createScaledBitmap(getImage, getImage.getWidth() / SCALE_IMAGE, getImage.getHeight() / SCALE_IMAGE, true);
                    getImage = checkRotation(tempImagePath, getImage);
                    if (getImage != null) {
                        saveImage(getImage);
                        imageGetListener.successfullyGetImage(imageFile, getImage);
                    } else {
                        imageGetListener.failToGetImage("Can't save image to storage");
                    }

                } catch (Exception e) {
                    Log.e(TAG, "onActivityResult (Error) : " + e.getMessage().toString());
                    try {
                        imageGetListener.failToGetImage(e.getMessage().toString());
                    } catch (Exception ex) {
                    }
                }
            } else {
                try {
                    imageGetListener.failToGetImage("Image capture canceled");
                } catch (Exception e) {
                }
            }
        } else if (requestCode == GALLERY_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                try {
                    Bitmap getImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    getImage = Bitmap.createScaledBitmap(getImage, getImage.getWidth() / SCALE_IMAGE, getImage.getHeight() / SCALE_IMAGE, true);
                    saveImage(getImage);
                    imageGetListener.successfullyGetImage(imageFile, getImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                imageGetListener.failToGetImage("Image selection canceled");
            }
        }
    }

    /*Files Creating thread*/
    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File dir = new File(Environment.getExternalStorageDirectory() + "/" + imageDirName + "/Temp");
                    File imageDir = new File(Environment.getExternalStorageDirectory() + "/" + imageDirName + "/Image");
                    if (!dir.exists()) dir.mkdirs();
                    if (!imageDir.exists()) imageDir.mkdirs();
                    if (dir.isDirectory()) {
                        String[] children = dir.list();
                        for (int i = 0; i < children.length; i++) {
                            new File(dir, children[i]).delete();
                        }
                    } else {
                        dir.mkdirs();
                        imageDir.mkdir();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error IMG Directory: " + e.getMessage().toString());
                }
            }
        }).start();
    }
}
