package nmsu.hcc.pattern_triggers.model;

public class FeatureMapping {
    int featureId;
    String featureName;
    Alphabet alphabet;

    public FeatureMapping(int featureId, String featureName, Alphabet alphabet) {
        this.featureId = featureId;
        this.featureName = featureName;
        this.alphabet = alphabet;
    }

    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Alphabet alphabet) {
        this.alphabet = alphabet;
    }
}
