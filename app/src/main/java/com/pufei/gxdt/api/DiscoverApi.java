package com.pufei.gxdt.api;

import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
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

public interface DiscoverApi {

    @POST("v5/User/getFindList")
    Observable<DiscoverListBean> getFindList(@Body RequestBody body);

}
