package nmsu.hcc.pattern_triggers.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PerformanceTrackingResponse {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("code")
    @Expose
    private int result;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}





