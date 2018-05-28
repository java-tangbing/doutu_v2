package com.pufei.gxdt.api;


import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.model.ThemeResultBean;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ImageTypeApi {
    @POST("AmuseImages/getNewList")
    Observable<PictureResultBean> getHotList(@Body RequestBody body);
    @POST("Category/getRandList")
    Observable<ThemeResultBean> getThemeImageList(@Body RequestBody body);
    @POST("AmuseImages/getList")
    Observable<PictureResultBean> getThemeImageDetailList(@Body RequestBody body);
}
