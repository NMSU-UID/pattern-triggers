package nmsu.hcc.pattern_triggers.network.listeners;


import nmsu.hcc.pattern_triggers.network.response.FeatureMappingResponse;

public interface FeatureMappingListener extends BaseApiCallListener {
    void onSuccess(FeatureMappingResponse featureMappingResponse);

    void onFailed(String message, int responseCode);
}
