package nmsu.hcc.pattern_triggers.network;

import android.content.Context;
import com.google.gson.Gson;
import org.json.JSONObject;

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

    /*public String registration(String firstName, String lastName, String email, String password, String passwordConfirmation, int userRegistration, RegistrationListener registrationListener) {
        this.registrationListener = registrationListener;
        this.reqIdRegistration = ShareInfo.getInstance().getRequestId();
        HashMap hashMap = new HashMap();
        hashMap.put("first_name", firstName);
        hashMap.put("last_name", lastName);
        hashMap.put("email", email);
        hashMap.put("password", password);
        hashMap.put("password_confirmation", passwordConfirmation);
        hashMap.put("user_registration", 1);
        Log.e("SignUpActivity::", "HashMAp::" + hashMap.toString());
        apiHandler.httpRequest(ShareInfo.getInstance().getWasekaUrl(), "user-registration", "post", reqIdRegistration, hashMap);
        return reqIdRegistration;
    }*/

    /*public String login(String email, String password, LoginListener loginListener) {
        this.loginListener = loginListener;
        this.reqIdLogin = ShareInfo.getInstance().getRequestId();
        HashMap hashMap = new HashMap();
        hashMap.put("email", email);
        hashMap.put("password", password);
        apiHandler.httpRequest(ShareInfo.getInstance().getWasekaUrl(), "user-login", "post", reqIdLogin, hashMap);
        return reqIdLogin;
    }*/

}
