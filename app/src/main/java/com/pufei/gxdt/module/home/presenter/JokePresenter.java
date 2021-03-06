package com.pufei.gxdt.module.home.presenter;


import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.JokeDetailBean;
import com.pufei.gxdt.module.home.model.JokeResultBean;
import com.pufei.gxdt.module.home.view.JokeView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class JokePresenter extends BasePresenter<JokeView> {

    public void getJokeList(RequestBody body) {
            Disposable disposable = ApiService.getJokeApi().getJokelist(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<JokeResultBean>() {
                    @Override
                    public void accept(JokeResultBean result) throws Exception {
                        baseview.resultJokeList(result);
                    }
                },new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            baseview.requestErrResult(throwable.getMessage()+"");
                        }
                    });
        addSubscription(disposable);
    }
    public void getJokDetail(RequestBody body) {
        Disposable disposable = ApiService.getJokeApi().getJokeDetail(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JokeDetailBean>() {
                    @Override
                    public void accept(JokeDetailBean result) throws Exception {
                        baseview.resultJokeDetail(result);
                    }
                },new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        baseview.requestErrResult(throwable.getMessage()+"");
                    }
                });
        addSubscription(disposable);
    }
    public void getCountView(RequestBody body) {
        Disposable disposable = ApiService.getImageTypeAoi().getConutView(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FavoriteBean>() {
                    @Override
                    public void accept(FavoriteBean result) throws Exception {
                        baseview.resultCountView(result);
                    }
                },new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        baseview.requestErrResult(throwable.getMessage()+"");
                    }
                });
        addSubscription(disposable);
    }
}
