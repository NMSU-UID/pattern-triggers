package nmsu.hcc.pattern_triggers.network.listeners;


import nmsu.hcc.pattern_triggers.network.response.PerformanceTrackingResponse;

public interface PerformanceTrackerListener extends BaseApiCallListener {
    void onSuccess(PerformanceTrackingResponse performanceTrackingResponse);

    void onFailed(String message, int responseCode);
}
