package com.pufei.gxdt.module.home.presenter;


import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.model.ThemeResultBean;
import com.pufei.gxdt.module.home.view.ThemeImageView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class ThemeImagePresenter extends BasePresenter<ThemeImageView> {

    public void getThemeImage(RequestBody body) {
            Disposable disposable = ApiService.getImageTypeAoi().getThemeImageList(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ThemeResultBean>() {
                    @Override
                    public void accept(ThemeResultBean result) throws Exception {
                        baseview.resultThemeImage(result);
                    }
                },new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            baseview.requestErrResult(throwable.getMessage()+"");
                        }
                    });
        addSubscription(disposable);
    }

    public void getThemeDetail(RequestBody body) {
        Disposable disposable = ApiService.getImageTypeAoi().getThemeImageDetailList(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PictureResultBean>() {
                    @Override
                    public void accept(PictureResultBean result) throws Exception {
                        baseview.resultThemeImageDetail(result);
                    }
                },new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        baseview.requestErrResult(throwable.getMessage()+"");
                    }
                });
        addSubscription(disposable);
    }
    public void addFavorite(RequestBody body) {
        Disposable disposable = ApiService.getImageTypeAoi().addFavarite(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FavoriteBean>() {
                    @Override
                    public void accept(FavoriteBean result) throws Exception {
                        baseview.resultAddFavorite(result);
                    }
                },new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        baseview.requestErrResult(throwable.getMessage()+"");
                    }
                });
        addSubscription(disposable);
    }
    public void cancleFavorite(RequestBody body) {
        Disposable disposable = ApiService.getImageTypeAoi().cancleFavarite(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FavoriteBean>() {
                    @Override
                    public void accept(FavoriteBean result) throws Exception {
                        baseview.resultCancleFavorite(result);
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
