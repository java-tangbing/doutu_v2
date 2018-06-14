package com.pufei.gxdt.module.user.presenter;


import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.home.model.JokeDetailBean;
import com.pufei.gxdt.module.home.model.JokeResultBean;
import com.pufei.gxdt.module.user.bean.MyImagesBean;
import com.pufei.gxdt.module.user.view.FavoriteView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class FavoritePresenter extends BasePresenter<FavoriteView> {

    public void getFavoritePkgList(RequestBody body) {
        Disposable disposable = ApiService.getPersonalApi().getMyImages(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MyImagesBean>() {
                    @Override
                    public void accept(MyImagesBean result) throws Exception {
                        baseview.resultJokeList(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        baseview.requestErrResult(throwable.getMessage() + "");
                    }
                });
        addSubscription(disposable);
    }

    public void getFavoriteJokeList(RequestBody body) {
        Disposable disposable = ApiService.getPersonalApi().getMyImages(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MyImagesBean>() {
                    @Override
                    public void accept(MyImagesBean result) throws Exception {
                        baseview.resultJokeList(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        baseview.requestErrResult(throwable.getMessage() + "");
                    }
                });
        addSubscription(disposable);
    }

    public void getJokDetail(RequestBody body) {
        Disposable disposable = ApiService.getPersonalApi().getMyImages(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MyImagesBean>() {
                    @Override
                    public void accept(MyImagesBean result) throws Exception {
                        baseview.resultJokeDetail(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        baseview.requestErrResult(throwable.getMessage() + "");
                    }
                });
        addSubscription(disposable);
    }
}
