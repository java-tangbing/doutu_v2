package com.pufei.gxdt.module.user.presenter;


import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.login.model.LoginResultBean;
import com.pufei.gxdt.module.user.bean.BindAccountBean;
import com.pufei.gxdt.module.user.bean.SetsBean;
import com.pufei.gxdt.module.user.view.SettingView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class SettingPresenter extends BasePresenter<SettingView> {

    public void getPublish(RequestBody body) {
            Disposable disposable = ApiService.getPersonalApi().getSets(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<SetsBean>() {
                    @Override
                    public void accept(SetsBean result) throws Exception {
                        baseview.resultSets(result);
                    }
                });
        addSubscription(disposable);
    }

    public void bindAccount(RequestBody body) {
        Disposable disposable = ApiService.loginQQApi().bindAccount(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BindAccountBean>() {
                    @Override
                    public void accept(BindAccountBean resultBean) throws Exception {
                        baseview.sendRusult(resultBean);
                    }
                });
        addSubscription(disposable);
    }
}
