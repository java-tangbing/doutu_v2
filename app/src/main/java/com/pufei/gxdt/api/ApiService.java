package com.pufei.gxdt.api;


import com.pufei.gxdt.utils.RetrofitFactory;

import retrofit2.Retrofit;


/**
 * 该类用于调用网络请求并且返回相应的数据类
 */

public class ApiService {
    private static LoginApi loginApi;
    private static PersonalApi personalApi;
    private static SettingApi settingApi;



    public static LoginApi getSendCode() {
        if (loginApi == null) {
            Retrofit retrofit = RetrofitFactory.getRetrofit();
            loginApi = retrofit.create(LoginApi.class);
        }
        return loginApi;
    }

    public static LoginApi loginQQApi() {
        if (loginApi == null) {
            Retrofit retrofit = RetrofitFactory.getRetrofit();
            loginApi = retrofit.create(LoginApi.class);
        }
        return loginApi;
    }

    public static LoginApi bindPhone() {
        if (loginApi == null) {
            Retrofit retrofit = RetrofitFactory.getRetrofit();
            loginApi = retrofit.create(LoginApi.class);
        }
        return loginApi;
    }



    public static PersonalApi setPersonal() {

        if (personalApi == null) {
            Retrofit retrofit = RetrofitFactory.getRetrofit();
            personalApi = retrofit.create(PersonalApi.class);
        }

        return personalApi;
    }

    public static SettingApi getSettingApi() {
        if (settingApi == null) {
            Retrofit retrofit = RetrofitFactory.getRetrofit();
            settingApi = retrofit.create(SettingApi.class);
        }
        return settingApi;
    }

}
