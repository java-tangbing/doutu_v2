package com.pufei.gxdt.api;

import com.pufei.gxdt.module.user.bean.ModifyResultBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SettingApi {

    @POST("member/set_password")
    Observable<ModifyResultBean> setNewPwd(@Body RequestBody request);

    @POST("member/update_password")
    Observable<ModifyResultBean> updatePwd(@Body RequestBody request);
}
