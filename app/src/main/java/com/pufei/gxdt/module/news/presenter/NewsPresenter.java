package com.pufei.gxdt.module.news.presenter;

import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;

import com.pufei.gxdt.module.news.bean.NoticeBean;
import com.pufei.gxdt.module.news.view.NewsView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class NewsPresenter extends BasePresenter<NewsView> {
    public void newsNoticeList(RequestBody body) {
        Disposable disposable = ApiService.getNewsApi().getNoticeList(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NoticeBean>() {
                    @Override
                    public void accept(NoticeBean noticeBean) throws Exception {
                        baseview.getNoticeList(noticeBean);
                    }
                });
        addSubscription(disposable);
    }
}
