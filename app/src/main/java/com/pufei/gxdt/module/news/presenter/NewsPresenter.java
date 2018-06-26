package com.pufei.gxdt.module.news.presenter;

import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;

import com.pufei.gxdt.module.news.bean.NewsBean;
import com.pufei.gxdt.module.news.bean.NewsTypeTwoBean;
import com.pufei.gxdt.module.news.bean.NoticeBean;
import com.pufei.gxdt.module.news.bean.SendBean;
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
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        baseview.requestErrResult(throwable.getMessage()+"");
                    }
                });
        addSubscription(disposable);
    }

    public void newsNoticeContent(RequestBody body) {
        Disposable disposable = ApiService.getNewsApi().getNoticeContent(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NewsBean>() {
                    @Override
                    public void accept(NewsBean newsBean) throws Exception {
                        baseview.getsNoticeContent(newsBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        baseview.requestErrResult(throwable.getMessage()+"");
                    }
                });
        addSubscription(disposable);
    }
    public void newsNoticeContentTypeTwo(RequestBody body) {
        Disposable disposable = ApiService.getNewsApi().getNoticeContentTypeTwo(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NewsTypeTwoBean>() {
                    @Override
                    public void accept(NewsTypeTwoBean newsTypeTwoBean) throws Exception {
                        baseview.getsNoticeContentTypeTwo(newsTypeTwoBean);
                    }
                }
//                , new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
////                        baseview.requestErrResult(throwable.getMessage()+"");
//                    }
//                }
                );
        addSubscription(disposable);
    }

    public void sendAdvice(RequestBody body) {
        Disposable disposable = ApiService.getNewsApi().getAdvice(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SendBean>() {
                    @Override
                    public void accept(SendBean sendBean) throws Exception {
                        baseview.getAdviceResult(sendBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        baseview.requestErrResult(throwable.getMessage()+"");
                    }
                });
        addSubscription(disposable);
    }
}
