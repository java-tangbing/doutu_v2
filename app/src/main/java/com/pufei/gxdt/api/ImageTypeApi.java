package com.pufei.gxdt.api;


import com.pufei.gxdt.module.home.model.HomeDetailBean;
import com.pufei.gxdt.module.home.model.HomeResultBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.model.ThemeResultBean;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ImageTypeApi {
    @POST("IndexList/getList")
    Observable<HomeResultBean> getHomeList(@Body RequestBody body);
    @POST("AmuseImages/getList")
    Observable<HomeDetailBean> getHomeDetailList(@Body RequestBody body);
    @POST("AmuseImages/getNewList")
    Observable<PictureResultBean> getHotList(@Body RequestBody body);
    @POST("Category/getThemeList")
    Observable<ThemeResultBean> getThemeImageList(@Body RequestBody body);
    @POST("AmuseImages/getTheme")
    Observable<PictureResultBean> getThemeImageDetailList(@Body RequestBody body);
}
