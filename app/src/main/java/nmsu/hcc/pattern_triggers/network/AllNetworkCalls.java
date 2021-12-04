package nmsu.hcc.pattern_triggers.network;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface AllNetworkCalls {

    @GET("{url}")
    Call<ResponseBody> getRequest(
            @Path(value = "url", encoded = true) String path,
            @QueryMap Map<String, String> hashMap
    );

    /*@FormUrlEncoded
    @POST("{url}")
    Call<ResponseBody> postRequest(
            @Path(value = "url", encoded = true) String path,
            @FieldMap Map<String, String> hashMap
    );*/

    @GET("{url}")
    Call<ResponseBody> getRequest(
            @Path(value = "url", encoded = true) String path
    );

    @Headers("Content-Type: application/json")
    @POST("{url}")
    Call<ResponseBody> postRequest(
            @Path(value = "url", encoded = true) String path,
            @Body HashMap body
    );

    @DELETE("{url}")
    Call<ResponseBody> deleteRequest(@Path(value = "url", encoded = true) String path);

    @DELETE("{url}")
    Call<ResponseBody> deleteQueryRequest(
            @Path(value = "url", encoded = true) String path,
            @QueryMap Map<String, String> hashMap);
}
