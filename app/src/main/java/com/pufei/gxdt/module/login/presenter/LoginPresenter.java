package com.pufei.gxdt.module.login.presenter;

import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.login.model.LoginResultBean;
import com.pufei.gxdt.module.login.model.SendCodeBean;
import com.pufei.gxdt.module.login.view.LoginView;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;
import com.pufei.gxdt.module.user.bean.MyImagesBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;


public class LoginPresenter extends BasePresenter<LoginView> {


    public void sendCode(RequestBody body) {
        Disposable disposable = ApiService.getSendCode().getTestContent(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SendCodeBean>() {
                    @Override
                    public void accept(SendCodeBean sendCodeBean) throws Exception {
                        baseview.sendCode(sendCodeBean);
                    }
                });
        addSubscription(disposable);
    }

    public void validationCode(RequestBody body) {
        Disposable disposable = ApiService.getSendCode().getCodeResult(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResultBean>() {
                    @Override
                    public void accept(LoginResultBean sendCodeBean) throws Exception {
                        baseview.sendRusult(sendCodeBean);
                    }
                });
        addSubscription(disposable);
    }

    public void loginWithPwd(RequestBody body) {
        Disposable disposable = ApiService.loginQQApi().loginWithPwd(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResultBean>() {
                    @Override
                    public void accept(LoginResultBean sendCodeBean) throws Exception {
                        baseview.sendRusult(sendCodeBean);
                    }
                });
        addSubscription(disposable);
    }

    public void thirdLogin(RequestBody body) {
        Disposable disposable = ApiService.loginQQApi().loginWithQQ(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResultBean>() {
                    @Override
                    public void accept(LoginResultBean resultBean) throws Exception {
                        baseview.sendRusult(resultBean);
                    }
                });
        addSubscription(disposable);
    }

    public void thirdLoginWX(RequestBody body) {
        Disposable disposable = ApiService.loginQQApi().loginWithWX(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResultBean>() {
                    @Override
                    public void accept(LoginResultBean resultBean) throws Exception {
                        baseview.sendRusult(resultBean);
                    }
                });
        addSubscription(disposable);
    }


    public void bindPhone(RequestBody body) {
        Disposable disposable = ApiService.bindPhone().bindPhone(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResultBean>() {
                    @Override
                    public void accept(LoginResultBean resultBean) throws Exception {
                        baseview.sendRusult(resultBean);
                    }
                });
        addSubscription(disposable);
    }

    public void retrievePwd(RequestBody body) {
        Disposable disposable = ApiService.loginQQApi().retrievePwd(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModifyResultBean>() {
                    @Override
                    public void accept(ModifyResultBean resultBean) throws Exception {
                        baseview.retrievePwdResult(resultBean);
                    }
                });
        addSubscription(disposable);
    }


}
