package com.pufei.gxdt.module.discover.presenter;

import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.discover.bean.DisPicDetBean;
import com.pufei.gxdt.module.discover.bean.DisWorksBean;
import com.pufei.gxdt.module.discover.view.DisWorksView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class DisWorksPresenter extends BasePresenter<DisWorksView> {
    public void worksDetail(RequestBody body) {
        Disposable disposable = ApiService.getDiscoverApi().getUserProfile(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DisWorksBean>() {
                    @Override
                    public void accept(DisWorksBean bean) throws Exception {
                        baseview.getWorksDetail(bean);
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
