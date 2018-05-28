package com.pufei.gxdt.module.discover.presenter;

import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import com.pufei.gxdt.module.view.DiscoverView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class DiscoverPresenter extends BasePresenter<DiscoverView> {
    public void discoverHotList(RequestBody body) {
        final Disposable disposable = ApiService.getDiscoverApi().getFindList(body)
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
}
