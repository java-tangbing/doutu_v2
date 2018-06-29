package com.pufei.gxdt.module.user.presenter;


import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.user.view.PublishView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class PublishPresenter extends BasePresenter<PublishView> {

    public void getPublish(RequestBody body) {
            Disposable disposable = ApiService.getPersonalApi().getMymyDesignImages(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<PictureResultBean>() {
                    @Override
                    public void accept(PictureResultBean result) throws Exception {
                        baseview.resultPublish(result);
                    }
                }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            baseview.requestErrResult(throwable.getMessage()+"");
                        }
                    });
        addSubscription(disposable);
    }
    public void setMyDesignImages(RequestBody body) {
        Disposable disposable = ApiService.getPersonalApi().setMyDesignImages(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FavoriteBean>() {
                    @Override
                    public void accept(FavoriteBean result) throws Exception {
                        baseview.setMyDesignImagesResult(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        baseview.requestErrResult(throwable.getMessage()+"");
                    }
                });
        addSubscription(disposable);
    }
    public void delMyDesignImages(RequestBody body) {
        Disposable disposable = ApiService.getPersonalApi().delMyDesignImages(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FavoriteBean>() {
                    @Override
                    public void accept(FavoriteBean result) throws Exception {
                        baseview.delMyDesignImagesResult(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        baseview.requestErrResult(throwable.getMessage()+"");
                    }
                });
        addSubscription(disposable);
    }
}
