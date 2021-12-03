package nmsu.hcc.pattern_triggers.network.listeners;

public interface BaseApiCallListener {
    void startLoading(String requestId);

    void endLoading(String requestId);
}
