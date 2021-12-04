package nmsu.hcc.pattern_triggers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import nmsu.hcc.pattern_triggers.model.Alphabet;
import nmsu.hcc.pattern_triggers.model.FeatureMapping;

public class LocalStorage {

    public static final int FEATURE_GOOGLE_CHROME = 0;
    public static final int FEATURE_YOUTUBE = 1;
    public static final int FEATURE_TURN_ON_TORCH = 2;
    public static final int FEATURE_TURN_OFF_TORCH = 3;

    private static final LocalStorage ourInstance = new LocalStorage();

    public static LocalStorage getInstance() {
        return ourInstance;
    }

    private LocalStorage() { }

    private SharedPreferences getBaseConfig(Context context) {
        return context.getSharedPreferences("pattern_triggers_app", Context.MODE_PRIVATE);
    }

    public ArrayList<Alphabet> getAlphabetList(){
        ArrayList<Alphabet> alphabetArrayList = new ArrayList<>();

        alphabetArrayList.add(new Alphabet(0, ""));
        alphabetArrayList.add(new Alphabet(1, "B"));
        alphabetArrayList.add(new Alphabet(2, "V"));
        alphabetArrayList.add(new Alphabet(3, "M"));
        alphabetArrayList.add(new Alphabet(4, "N"));

        return alphabetArrayList;
    }

    public Alphabet getAlphabetById(int id){

        if (id==0) return null;

        for (Alphabet alphabet: getAlphabetList()) {
            if (alphabet.getAlphabetId()==id) return alphabet;
        }
        return null;
    }

    public int getFeatureIdByAlphabetName(Context context, String alphabetName){
        ArrayList<FeatureMapping> featureMappingArrayList = getSavedFeatureMapping(context);
        if(featureMappingArrayList==null){
            return -1;
        }
        for (FeatureMapping feature: getSavedFeatureMapping(context)) {
            // not case sensitive; to support the lowercase like v or x
            if (feature.getAlphabet()==null) return -1;
            if(feature.getAlphabet().getAlphabetName().equalsIgnoreCase(alphabetName)){
                return feature.getFeatureId();
            }
        }
        return -1;
    }

    public ArrayList<FeatureMapping> getDefaultFeatureList(){
        ArrayList<FeatureMapping> featureMappings = new ArrayList<>();

        featureMappings.add(new FeatureMapping(FEATURE_GOOGLE_CHROME, "Google Chrome", null));
        featureMappings.add(new FeatureMapping(FEATURE_YOUTUBE, "Youtube", null));
        featureMappings.add(new FeatureMapping(FEATURE_TURN_ON_TORCH, "Turn 'on' torch", null));
        featureMappings.add(new FeatureMapping(FEATURE_TURN_OFF_TORCH, "Turn 'off' torch", null));

        return featureMappings;
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
