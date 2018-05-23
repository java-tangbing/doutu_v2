package com.pufei.gxdt.api;



import com.pufei.gxdt.utils.RetrofitFactory;

import retrofit2.Retrofit;

public class ApiService {
    private static JokeApi loginApi;
    public static JokeApi getJokeApi() {
        if (loginApi == null) {
            Retrofit retrofit = RetrofitFactory.getRetrofit();
            loginApi = retrofit.create(JokeApi.class);
        }
        return loginApi;
    }

}
