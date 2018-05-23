package com.pufei.gxdt.api;

import com.pufei.gxdt.module.home.model.JokeResultBean;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JokeApi {
    @POST("api/v3/Article/getList/")
    Observable<JokeResultBean> getJokelist(@Body RequestBody body);

}
