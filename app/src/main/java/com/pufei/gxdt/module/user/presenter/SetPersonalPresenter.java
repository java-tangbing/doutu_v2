package com.pufei.gxdt.module.user.presenter;

import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.user.bean.SetAvatarResultBean;
import com.pufei.gxdt.module.user.bean.SetPersonalResultBean;
import com.pufei.gxdt.module.user.view.SetPersonalView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class SetPersonalPresenter extends BasePresenter<SetPersonalView> {
    public void setPersonal(RequestBody value) {
        Disposable disposable = ApiService.setPersonal().setProfile(value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SetPersonalResultBean>() {
                    @Override
                    public void accept(SetPersonalResultBean result) throws Exception {
                        baseview.setPersonalInfo(result);
                    }
                });
        addSubscription(disposable);
    }

    public void setPersonalAvatar(RequestBody value) {
        Disposable disposable = ApiService.setPersonal().setAvatar(value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SetAvatarResultBean>() {
                    @Override
                    public void accept(SetAvatarResultBean result) throws Exception {
                        baseview.setPersonAvatar(result);
                    }
                });
        addSubscription(disposable);
    }
}