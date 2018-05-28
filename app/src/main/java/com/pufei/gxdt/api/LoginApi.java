package com.pufei.gxdt.api;

import com.pufei.gxdt.module.login.model.LoginResultBean;
import com.pufei.gxdt.module.login.model.SendCodeBean;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 创建者： wangwenzhang 时间： 2018/3/9.
 */

public interface LoginApi {
    @POST("login/get_vcode")
    Observable<SendCodeBean> getTestContent(@Body RequestBody body);
    @POST("login/vcode")
    Observable<LoginResultBean> getCodeResult(@Body RequestBody body);
    @POST("login/qq")
    Observable<LoginResultBean> loginWithQQ(@Body RequestBody body);
    @POST("login/wx")
    Observable<LoginResultBean> loginWithWX(@Body RequestBody body);
    @POST("login/regist")
    Observable<LoginResultBean> bindPhone(@Body RequestBody body);
    @POST("login/pwd")
    Observable<LoginResultBean> loginWithPwd(@Body RequestBody request);
    @POST("login/retrieve_password")
    Observable<ModifyResultBean> retrievePwd(@Body RequestBody body);

}
