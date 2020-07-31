package com.pufei.gxdt.api;

import com.pufei.gxdt.module.discover.bean.DisPicDetBean;
import com.pufei.gxdt.module.discover.bean.DisWorksBean;
import com.pufei.gxdt.module.discover.bean.DiscoverEditImageBean;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;


/**
 * 创建者： caozw 时间： 2018/5/25.
 */

public interface DiscoverApi {
    @POST("User/getFindList")
    Observable<DiscoverListBean> getFindList(@Body RequestBody body);

    @POST("AmuseImages/getEditList")
    Observable<DiscoverEditImageBean> getEditImage(@Body RequestBody body);


    @POST("AmuseImages/getImageDetail")
    Observable<DisPicDetBean> getImageDetail(@Body RequestBody body);


    @POST("User/userProfile")
    Observable<DisWorksBean> getUserProfile(@Body RequestBody body);
}
