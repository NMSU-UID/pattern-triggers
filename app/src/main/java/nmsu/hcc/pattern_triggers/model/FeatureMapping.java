package nmsu.hcc.pattern_triggers.model;

public class FeatureMapping {
    String featureId;
    String featureName;
    String alphabet;

    public FeatureMapping(String featureId, String featureName, String alphabet) {
        this.featureId = featureId;
        this.featureName = featureName;
        this.alphabet = alphabet;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }
}
