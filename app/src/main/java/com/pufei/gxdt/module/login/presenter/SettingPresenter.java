package com.pufei.gxdt.module.login.presenter;

import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.login.view.SettingView;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class SettingPresenter extends BasePresenter<SettingView> {

    public void setNewPwd(RequestBody body) {
        Disposable disposable = ApiService.getSettingApi().setNewPwd(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModifyResultBean>() {
                    @Override
                    public void accept(ModifyResultBean result) throws Exception {
                        baseview.setNewPwd(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        baseview.requestErrResult(throwable.getMessage() + "");
                    }
                });
        addSubscription(disposable);
    }

    public void updatePwd(RequestBody body) {
        Disposable disposable = ApiService.getSettingApi().updatePwd(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ModifyResultBean>() {
                    @Override
                    public void accept(ModifyResultBean result) throws Exception {
                        baseview.updatePwd(result);
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
