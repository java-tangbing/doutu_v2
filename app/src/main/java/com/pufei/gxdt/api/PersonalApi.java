package com.pufei.gxdt.api;

import com.pufei.gxdt.module.user.bean.SetAvatarResultBean;
import com.pufei.gxdt.module.user.bean.SetPersonalResultBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PersonalApi {
    @POST("member/set_personal")
    Observable<SetPersonalResultBean> setPresonal(@Body RequestBody body);

    @POST("member/set_avatar")
    Observable<SetAvatarResultBean> setAvatar(@Body RequestBody body);
//    @POST("v2/member/ischeck")
//    Observable<CheckinBean> isCheckin(@Body RequestBody body);
}