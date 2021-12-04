package nmsu.hcc.pattern_triggers.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

import nmsu.hcc.pattern_triggers.LocalStorage;
import nmsu.hcc.pattern_triggers.network.listeners.FeatureMappingListener;
import nmsu.hcc.pattern_triggers.network.listeners.PerformanceTrackerListener;
import nmsu.hcc.pattern_triggers.network.response.FeatureMappingResponse;
import nmsu.hcc.pattern_triggers.network.response.PerformanceTrackingResponse;
import okhttp3.ResponseBody;

import static nmsu.hcc.pattern_triggers.network.ResponseCode.INVALID_JSON_RESPONSE;

public class ApiManager {

    private Context context;
    ApiHandler apiHandler;

    private String reqIdPerformanceTracker;
    private String reqIdFeatureMapping;

    PerformanceTrackerListener performanceTrackerListener;
    FeatureMappingListener featureMappingListener;

    public ApiManager(Context context) {
        this.context = context;
        apiHandler = new ApiHandler(context) {
            @Override
            public void startApiCall(String requestId) {
                if (requestId.equals(reqIdPerformanceTracker)) {
                    performanceTrackerListener.startLoading(requestId);
                } else if (requestId.equals(reqIdFeatureMapping)) {
                    featureMappingListener.startLoading(requestId);
                }
            }

            @Override
            public void endApiCall(String requestId) {
                if (requestId.equals(reqIdPerformanceTracker)) {
                    performanceTrackerListener.endLoading(requestId);
                } else if (requestId.equals(reqIdFeatureMapping)) {
                    featureMappingListener.endLoading(requestId);
                }
            }

            @Override
            public void successResponse(String requestId, ResponseBody responseBody, String baseUrl, String path, String requestType) {

                if (requestId.equals(reqIdPerformanceTracker)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        performanceTrackerListener.onSuccess(new Gson().fromJson(jsonObject.toString(), PerformanceTrackingResponse.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                        performanceTrackerListener.onFailed("Invalid  Response", INVALID_JSON_RESPONSE);
                    }
                } else if (requestId.equals(reqIdFeatureMapping)) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        featureMappingListener.onSuccess(new Gson().fromJson(jsonObject.toString(), FeatureMappingResponse.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                        featureMappingListener.onFailed("Invalid  Response", INVALID_JSON_RESPONSE);
                    }
                }

            }

            @Override
            public void failResponse(String requestId, int responseCode, String message) {
                if (requestId.equals(reqIdPerformanceTracker)) {
                    performanceTrackerListener.onFailed(message, responseCode);
                } else if (requestId.equals(reqIdFeatureMapping)) {
                    featureMappingListener.onFailed(message, responseCode);
                }
            }
        };
    }

    public String performanceTracking(String alphabet, boolean success, PerformanceTrackerListener performanceTrackerListener) {
        this.performanceTrackerListener = performanceTrackerListener;
        this.reqIdPerformanceTracker = generateRequestId();
        HashMap hashMap = new HashMap();
        String userId = LocalStorage.getInstance().getStringData(context, "user_id");

        hashMap.put("user_id", userId);
        hashMap.put("alphabet", alphabet);
        hashMap.put("success", success);

        Log.e("ApiManager", "performanceTracking HashMAp::" + hashMap.toString());
        apiHandler.httpRequest("https://pattern-triggers-backend.herokuapp.com/", "performance-tracking", "post", reqIdPerformanceTracker, hashMap);
        return reqIdPerformanceTracker;
    }

    public String featureMapping(String alphabet, String feature, FeatureMappingListener featureMappingListener) {
        this.featureMappingListener = featureMappingListener;
        this.reqIdFeatureMapping = generateRequestId();
        HashMap hashMap = new HashMap();
        String userId = LocalStorage.getInstance().getStringData(context, "user_id");

        hashMap.put("user_id", userId);
        hashMap.put("alphabet", alphabet);
        hashMap.put("feature", feature);

        Log.e("ApiManager", "featureMapping HashMAp::" + hashMap.toString());
        apiHandler.httpRequest("https://pattern-triggers-backend.herokuapp.com/", "feature-mapping", "post", reqIdFeatureMapping, hashMap);
        return reqIdFeatureMapping;
    }

    public String generateRequestId() {
        Random rand = new Random();
        return System.currentTimeMillis() + "" + rand.nextInt(100000);
    }

}
