package nmsu.hcc.pattern_triggers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import nmsu.hcc.pattern_triggers.model.FeatureMapping;

public class LocalStorage {
    private static final LocalStorage ourInstance = new LocalStorage();

    public static LocalStorage getInstance() {
        return ourInstance;
    }

    private LocalStorage() { }

    private SharedPreferences getBaseConfig(Context context) {
        return context.getSharedPreferences("pattern_triggers_app", Context.MODE_PRIVATE);
    }

    public void saveFeatureMapping(Context context, ArrayList<FeatureMapping> featureMappingArrayList) {
        Type baseType = new TypeToken<ArrayList<FeatureMapping>>() {}.getType();
        String data = new Gson().toJson(featureMappingArrayList, baseType);
        getBaseConfig(context).edit().putString("key_feature_mapping", data).apply();
    }

    public ArrayList<FeatureMapping> getSavedFeatureMapping(Context context) {
        Type baseType = new TypeToken<ArrayList<FeatureMapping>>() {}.getType();
        String data = getBaseConfig(context).getString("key_feature_mapping", "");
        return new Gson().fromJson(data, baseType);
    }

    public void setData(Context context, String key, String data) {
        getBaseConfig(context).edit().putString(key, data).apply();
    }

    public void setData(Context context, String key, int data) {
        getBaseConfig(context).edit().putInt(key, data).apply();
    }

    public void setData(Context context, String key, float data) {
        getBaseConfig(context).edit().putFloat(key, data).apply();
    }

    public String getStringData(Context context, String key) {
        return getBaseConfig(context).getString(key, "");
    }

    public int getIntData(Context context, String key) {
        return getBaseConfig(context).getInt(key, -1);
    }

    public float getFloatData(Context context, String key) {
        return getBaseConfig(context).getFloat(key, -1.0f);
    }

    public void clearData(Context context, String key) {
        getBaseConfig(context).edit().remove(key).apply();
    }

    public void clearAllData(Context context, String key) {
        getBaseConfig(context).edit().clear().apply();
    }


}
