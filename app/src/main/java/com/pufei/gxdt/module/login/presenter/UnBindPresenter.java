package com.pufei.gxdt.module.login.presenter;

import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.login.view.LoginView;
import com.pufei.gxdt.module.login.view.UnBindView;
import com.pufei.gxdt.module.user.bean.BindAccountBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;


public class UnBindPresenter extends BasePresenter<UnBindView> {

    public void unbindAccount(RequestBody body) {
        Disposable disposable = ApiService.getLoginApi().unbindAccount(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BindAccountBean>() {
                    @Override
                    public void accept(BindAccountBean resultBean) throws Exception {
                        baseview.unBindResult(resultBean);
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
