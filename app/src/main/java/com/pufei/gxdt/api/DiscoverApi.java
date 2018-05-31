package com.pufei.gxdt.api;

import com.pufei.gxdt.module.discover.bean.DisPicDetBean;
import com.pufei.gxdt.module.discover.bean.DiscoverEditImageBean;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import com.pufei.gxdt.module.login.model.LoginResultBean;
import com.pufei.gxdt.module.login.model.SendCodeBean;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 创建者： caozw 时间： 2018/5/25.
 */

public interface DiscoverApi {
    @POST("User/getFindList")
    Observable<DiscoverListBean> getFindList(@Body RequestBody body);

    @POST("User/getEditImage")
    Observable<DiscoverEditImageBean> getEditImage(@Body RequestBody body);


    @POST("AmuseImages/getImageDetail")
    Observable<DisPicDetBean> getImageDetail(@Body RequestBody body);
}
