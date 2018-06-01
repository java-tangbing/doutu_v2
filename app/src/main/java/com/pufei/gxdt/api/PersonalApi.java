package com.pufei.gxdt.api;

import com.pufei.gxdt.module.user.bean.MyImagesBean;
import com.pufei.gxdt.module.user.bean.SetAvatarResultBean;
import com.pufei.gxdt.module.user.bean.SetPersonalResultBean;
import com.pufei.gxdt.module.user.bean.SetsBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PersonalApi {
    @POST("member/set_personal")
    Observable<SetPersonalResultBean> setPresonal(@Body RequestBody body);

    @POST("member/set_avatar")
    Observable<SetAvatarResultBean> setAvatar(@Body RequestBody body);

    @POST("User/myImages")
    Observable<MyImagesBean> getMyImages(@Body RequestBody body);

    @POST("User/myDesignImages")
    Observable<MyImagesBean> getMymyDesignImages(@Body RequestBody body);

    @POST("User/changeProfile")
    Observable<SetPersonalResultBean> setProfile(@Body RequestBody body);

    @POST("User/getSets")
    Observable<SetsBean> getSets(@Body RequestBody body);

}