package com.pufei.gxdt.module.home.presenter;


import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.home.model.HomeResultBean;
import com.pufei.gxdt.module.home.model.HomeTypeBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.view.HomeListView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class HomeListPresenter extends BasePresenter<HomeListView> {

    public void getHomeList(RequestBody body) {
            Disposable disposable = ApiService.getImageTypeAoi().getHomeList(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<HomeResultBean>() {
                    @Override
                    public void accept(HomeResultBean result) throws Exception {
                        baseview.resultHomeList(result);
                    }
                });
        addSubscription(disposable);
    }
    public void getHomeDetailList(RequestBody body) {
        Disposable disposable = ApiService.getImageTypeAoi().getHomeDetailList(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PictureResultBean>() {
                    @Override
                    public void accept(PictureResultBean result) throws Exception {
                        baseview.resultHomeDetailList(result);
                    }
                });
        addSubscription(disposable);
    }
    public void getHomeTypeList(RequestBody body) {
        Disposable disposable = ApiService.getImageTypeAoi().getHomeType(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HomeTypeBean>() {
                    @Override
                    public void accept(HomeTypeBean result) throws Exception {
                        baseview.resultHomeTypeList(result);
                    }
                });
        addSubscription(disposable);
    }
}
