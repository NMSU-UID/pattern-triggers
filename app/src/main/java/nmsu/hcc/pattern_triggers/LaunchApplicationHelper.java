package nmsu.hcc.pattern_triggers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.Locale;

public class LaunchApplicationHelper {

    public static void openApplication(Context context, String packageName) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        } else {
            Toast.makeText(context, "No application", Toast.LENGTH_LONG).show();
        }
    }

    public static String getCameraPackageName(Context context) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        ResolveInfo cameraInfo  = null;

        List<ResolveInfo> pkgList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if(pkgList != null && pkgList.size() > 0) {
            cameraInfo = pkgList.get(0);
        }
        return(cameraInfo.resolvePackageName);
    }

    public static void switchFlashLight(Context context, boolean status) {

        CameraManager mCameraManager;
        String mCameraId;

        boolean isFlashAvailable = context.getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);

        if (!isFlashAvailable) {
            Toast.makeText(context, "Flash light not found", Toast.LENGTH_LONG).show();
        } else {

            mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            try {
                mCameraId = mCameraManager.getCameraIdList()[0];
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mCameraManager.setTorchMode(mCameraId, status);
                    }
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
