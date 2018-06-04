package com.pufei.gxdt.module.discover.presenter;

import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.discover.bean.DisPicDetBean;
import com.pufei.gxdt.module.discover.view.DisPicDetView;
import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.PictureDetailBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class DisPicDetPresenter extends BasePresenter<DisPicDetView> {
    public void imageDetail(RequestBody body) {
        Disposable disposable = ApiService.getDiscoverApi().getImageDetail(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DisPicDetBean>() {
                    @Override
                    public void accept(DisPicDetBean noticeBean) throws Exception {
                        baseview.getImageDetail(noticeBean);
                    }
                });
        addSubscription(disposable);
    }

    public void getImageDetail(RequestBody body) {
        Disposable disposable = ApiService.getImageTypeAoi().getImageDetailList(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PictureDetailBean>() {
                    @Override
                    public void accept(PictureDetailBean result) throws Exception {
                        baseview.resultImageDetail(result);
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
                });
        addSubscription(disposable);
    }

}
