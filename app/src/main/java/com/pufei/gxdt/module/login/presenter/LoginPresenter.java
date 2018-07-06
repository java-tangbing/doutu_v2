package com.pufei.gxdt.module.login.presenter;

import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.home.model.LoginNewBean;
import com.pufei.gxdt.module.login.model.LoginResultBean;
import com.pufei.gxdt.module.login.model.SendCodeBean;
import com.pufei.gxdt.module.login.view.LoginView;
import com.pufei.gxdt.module.user.bean.BindAccountBean;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;
import com.pufei.gxdt.module.user.bean.MyImagesBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;


public class LoginPresenter extends BasePresenter<LoginView> {


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

    public void validationCode(RequestBody body) {
        Disposable disposable = ApiService.getLoginApi().getCodeResult(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResultBean>() {
                    @Override
                    public void accept(LoginResultBean sendCodeBean) throws Exception {
                        baseview.sendRusult(sendCodeBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        baseview.requestErrResult(throwable.getMessage() + "");
                    }
                });
        addSubscription(disposable);
    }

    public void loginWithPwd(RequestBody body) {
        Disposable disposable = ApiService.getLoginApi().loginWithPwd(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResultBean>() {
                    @Override
                    public void accept(LoginResultBean sendCodeBean) throws Exception {
                        baseview.sendRusult(sendCodeBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        baseview.requestErrResult(throwable.getMessage() + "");
                    }
                });
        addSubscription(disposable);
    }

    public void thirdLogin(RequestBody body) {
        Disposable disposable = ApiService.getLoginApi().thirdLogin(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResultBean>() {
                    @Override
                    public void accept(LoginResultBean resultBean) throws Exception {
                        if (baseview != null)
                            baseview.sendRusult(resultBean);
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

    public void thirdLoginQQ(RequestBody body) {
        Disposable disposable = ApiService.getLoginApi().loginWithQQ(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResultBean>() {
                    @Override
                    public void accept(LoginResultBean resultBean) throws Exception {
                        baseview.sendRusult(resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        baseview.requestErrResult(throwable.getMessage() + "");
                    }
                });
        addSubscription(disposable);
    }

    public void thirdLoginWX(RequestBody body) {
        Disposable disposable = ApiService.getLoginApi().loginWithWX(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResultBean>() {
                    @Override
                    public void accept(LoginResultBean resultBean) throws Exception {
                        baseview.sendRusult(resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        baseview.requestErrResult(throwable.getMessage() + "");
                    }
                });
        addSubscription(disposable);
    }


    public void bindPhone(RequestBody body) {
        Disposable disposable = ApiService.getLoginApi().bindPhone(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SendCodeBean>() {
                    @Override
                    public void accept(SendCodeBean resultBean) throws Exception {
                        baseview.bindResult(resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
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
                        baseview.bindResult(resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        baseview.requestErrResult(throwable.getMessage() + "");
                    }
                });
        addSubscription(disposable);
    }

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

    public void retrievePwd(RequestBody body) {
        Disposable disposable = ApiService.getLoginApi().retrievePwd(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModifyResultBean>() {
                    @Override
                    public void accept(ModifyResultBean resultBean) throws Exception {
                        baseview.retrievePwdResult(resultBean);
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
