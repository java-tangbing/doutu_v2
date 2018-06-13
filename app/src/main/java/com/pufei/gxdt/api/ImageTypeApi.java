package com.pufei.gxdt.api;


import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.HomeResultBean;
import com.pufei.gxdt.module.home.model.HomeTypeBean;
import com.pufei.gxdt.module.home.model.PictureDetailBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.model.ThemeResultBean;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ImageTypeApi {
    @POST("IndexList/getList")
    Observable<HomeResultBean> getHomeList(@Body RequestBody body);
    @POST("Category/getCateIndex")
    Observable<HomeTypeBean> getHomeType(@Body RequestBody body);
    @POST("AmuseImages/getList")
    Observable<PictureResultBean> getHomeDetailList(@Body RequestBody body);
    @POST("AmuseImages/getNewList")
    Observable<PictureResultBean> getHotList(@Body RequestBody body);
    @POST("Category/getThemeList")
    Observable<ThemeResultBean> getThemeImageList(@Body RequestBody body);
    @POST("AmuseImages/getTheme")
    Observable<PictureResultBean> getThemeImageDetailList(@Body RequestBody body);
    @POST("AmuseImages/getImageDetail")
    Observable<PictureDetailBean> getImageDetailList(@Body RequestBody body);
    @POST("User/addImages")
    Observable<FavoriteBean> addFavarite(@Body RequestBody body);
    @POST("User/deleteImages")
    Observable<FavoriteBean> cancleFavarite(@Body RequestBody body);
    @POST("Piece/countView")
    Observable<FavoriteBean> getConutView(@Body RequestBody body);
}
