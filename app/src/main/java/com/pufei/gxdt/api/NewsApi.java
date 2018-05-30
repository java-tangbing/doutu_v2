package com.pufei.gxdt.api;

import com.pufei.gxdt.module.discover.bean.DiscoverEditImageBean;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import com.pufei.gxdt.module.news.bean.NoticeBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 创建者： caozw 时间： 2018/5/25.
 */

public interface NewsApi {
    @POST("Piece/getNoticeList")
    Observable<NoticeBean> getNoticeList(@Body RequestBody body);

}