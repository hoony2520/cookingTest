package videofactory.net.cookingclass.utils;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by ijun on 6/28/17.
 */
public interface iNetwork {
    @Headers("Content-type: application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("{version}/{username}/{api}?")
    Call<Map> getAuthData(
            @Path(value = "version", encoded = true) String version, @Path(value = "username", encoded = true) String username, @Path(value = "api", encoded = true) String api,
            @QueryMap Map<String, String> queries, @FieldMap Map<String, String> maps);

    @Headers("Content-type: application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("{version}/{api}?")
    Call<Map> getNonAuthData(
            @Path(value = "version", encoded = true) String version, @Path(value = "api", encoded = true) String api,
            @QueryMap Map<String, String> queries, @FieldMap Map<String, String> maps);
}
