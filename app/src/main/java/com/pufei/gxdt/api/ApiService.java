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
    private static JokeApi jokeApi;
    private static ImageTypeApi imageTypeApi;
    private static DiscoverApi discoverApi;
    private static NewsApi newsApi;
    private static EditImageApi editImageApi;

    public static JokeApi getJokeApi() {
        if (jokeApi == null) {
            Retrofit retrofit = RetrofitFactory.getRetrofit();
            jokeApi = retrofit.create(JokeApi.class);
        }
        return jokeApi;
    }

    public static ImageTypeApi getImageTypeAoi() {
        if (imageTypeApi == null) {
            Retrofit retrofit = RetrofitFactory.getRetrofit();
            imageTypeApi = retrofit.create(ImageTypeApi.class);
        }
        return imageTypeApi;
    }


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

    public static DiscoverApi getDiscoverApi() {
        if (discoverApi == null) {
            Retrofit retrofit = RetrofitFactory.getRetrofit();
            discoverApi = retrofit.create(DiscoverApi.class);
        }
        return discoverApi;
    }

    public static NewsApi getNewsApi() {
        if (newsApi == null) {
            Retrofit retrofit = RetrofitFactory.getRetrofit();
            newsApi = retrofit.create(NewsApi.class);
        }
        return newsApi;
    }

    public static PersonalApi getPersonalApi() {
        if (personalApi == null) {
            Retrofit retrofit = RetrofitFactory.getRetrofit();
            personalApi = retrofit.create(PersonalApi.class);
        }
        return personalApi;
    }

    public static EditImageApi getEditImageApi() {
        if(editImageApi == null) {
            Retrofit retrofit = RetrofitFactory.getRetrofit();
            editImageApi = retrofit.create(EditImageApi.class);
        }
        return editImageApi;
    }

}
