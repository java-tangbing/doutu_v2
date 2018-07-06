package com.pufei.gxdt.module.login.presenter;

import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.home.model.LoginNewBean;
import com.pufei.gxdt.module.login.model.SendCodeBean;
import com.pufei.gxdt.module.login.view.LoginNewView;
import com.pufei.gxdt.module.login.view.LoginView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;


public class LoginNewPresenter extends BasePresenter<LoginNewView> {

    public void sendCode(RequestBody body) {
        Disposable disposable = ApiService.getLoginApi().getTestContent(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SendCodeBean>() {
                    @Override
                    public void accept(SendCodeBean sendCodeBean) throws Exception {
                        baseview.sendCode(sendCodeBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        baseview.requestErrResult(throwable.getMessage() + "");
                    }
                });
        addSubscription(disposable);
    }

    public void loginNew(RequestBody body) {
        Disposable disposable = ApiService.getLoginApi().loginNew(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginNewBean>() {
                    @Override
                    public void accept(LoginNewBean resultBean) throws Exception {
                        if (baseview != null)
                            baseview.LoginNewResult(resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (baseview != null)
                            baseview.requestErrResult(throwable.getMessage() + "");
                    }
                });
        addSubscription(disposable);
    }

    public void bindPhoneNew(RequestBody body) {
        Disposable disposable = ApiService.getLoginApi().bindPhoneNew(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SendCodeBean>() {
                    @Override
                    public void accept(SendCodeBean resultBean) throws Exception {
                        baseview.bindNewResult(resultBean);
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
