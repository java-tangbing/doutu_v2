package com.pufei.gxdt.api;

import com.pufei.gxdt.module.maker.bean.MaterialBean;
import com.pufei.gxdt.module.maker.bean.RecommendTextBean;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface EditImageApi {
    @POST("User/uploadImage")
    Observable<ModifyResultBean> upLoad(@Body RequestBody body);
    @GET
    Call <ResponseBody> getImage(@Url String url);

    @POST("Piece/getRecommendWord")
    Observable<RecommendTextBean> getRecommendText(@Body RequestBody body);
    @POST("Piece/materialList")
    Observable<MaterialBean> getMaterialList(@Body RequestBody body);
}
