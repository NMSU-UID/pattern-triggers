package nmsu.hcc.pattern_triggers.model;

public class Alphabet {
    int alphabetId;
    String alphabetName;

    public Alphabet(int alphabetId, String alphabetName) {
        this.alphabetId = alphabetId;
        this.alphabetName = alphabetName;
    }

    public int getAlphabetId() {
        return alphabetId;
    }

    public void setAlphabetId(int alphabetId) {
        this.alphabetId = alphabetId;
    }

    public String getAlphabetName() {
        return alphabetName;
    }

    public void setAlphabetName(String alphabetName) {
        this.alphabetName = alphabetName;
    }
}
