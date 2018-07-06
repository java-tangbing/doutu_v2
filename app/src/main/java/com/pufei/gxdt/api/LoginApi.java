package com.pufei.gxdt.api;

import com.pufei.gxdt.base.BaseBean;
import com.pufei.gxdt.module.home.model.LoginNewBean;
import com.pufei.gxdt.module.login.model.LoginResultBean;
import com.pufei.gxdt.module.login.model.SendCodeBean;
import com.pufei.gxdt.module.user.bean.BindAccountBean;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;
import com.pufei.gxdt.module.user.bean.MyImagesBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 创建者： wangwenzhang 时间2018/3/9.
 */

public interface LoginApi {
    @POST("User/sendCode")
    Observable<SendCodeBean> getTestContent(@Body RequestBody body);

    @POST("User/loignMobile")
    Observable<LoginResultBean> getCodeResult(@Body RequestBody body);

    @POST("User/login")
    Observable<LoginResultBean> thirdLogin(@Body RequestBody body);

    @POST("User/loginNew")
    Observable<LoginNewBean> loginNew(@Body RequestBody body);

    @POST("User/bindAccount")
    Observable<BindAccountBean> bindAccount(@Body RequestBody body);

    @POST("User/unbind")
    Observable<BindAccountBean> unbindAccount(@Body RequestBody body);

    @POST("login/qq")
    Observable<LoginResultBean> loginWithQQ(@Body RequestBody body);

    @POST("login/wx")
    Observable<LoginResultBean> loginWithWX(@Body RequestBody body);

    @POST("User/mobileBind")
    Observable<SendCodeBean> bindPhone(@Body RequestBody body);

    @POST("User/mobileBindNew")
    Observable<SendCodeBean> bindPhoneNew(@Body RequestBody body);

    @POST("login/pwd")
    Observable<LoginResultBean> loginWithPwd(@Body RequestBody request);

    @POST("login/retrieve_password")
    Observable<ModifyResultBean> retrievePwd(@Body RequestBody body);

}
