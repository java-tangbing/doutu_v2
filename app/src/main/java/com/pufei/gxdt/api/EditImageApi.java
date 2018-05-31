package com.pufei.gxdt.api;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface EditImageApi {
    @POST("User/uploadImage")
    Observable<String> upLoad(@Body RequestBody body);
    @GET
    Call <ResponseBody> getImage(@Url String url);
}
