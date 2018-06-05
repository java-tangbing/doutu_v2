package com.pufei.gxdt.module.discover.presenter;

import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.discover.bean.DiscoverEditImageBean;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import com.pufei.gxdt.module.discover.view.DiscoverView;
import com.pufei.gxdt.utils.ToastUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class DiscoverPresenter extends BasePresenter<DiscoverView> {
    public void discoverHotList(RequestBody body) {
        Disposable disposable = ApiService.getDiscoverApi().getFindList(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DiscoverListBean>() {
                    @Override
                    public void accept(DiscoverListBean discoverListBean) throws Exception {
                        baseview.getDiscoverHotList(discoverListBean);
                    }
                });
        addSubscription(disposable);
    }

    public void discoverEditImage(RequestBody body) {
        Disposable disposable = ApiService.getDiscoverApi().getEditImage(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DiscoverEditImageBean>() {
                    @Override
                    public void accept(DiscoverEditImageBean discoverEditImageBean) throws Exception {
                        baseview.getDiscoverDetailed(discoverEditImageBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        return;
                    }
                });
        addSubscription(disposable);
    }
}
