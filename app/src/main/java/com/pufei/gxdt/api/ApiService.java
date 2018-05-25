package com.pufei.gxdt.api;



import com.pufei.gxdt.utils.RetrofitFactory;

import retrofit2.Retrofit;

public class ApiService {
    private static JokeApi jokeApi;
    private static ImageTypeApi imageTypeApi;
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
}
