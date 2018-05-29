package com.pufei.gxdt.module.user.presenter;


import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.user.view.DraftView;
import com.pufei.gxdt.module.user.view.PublishView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class DraftPresenter extends BasePresenter<DraftView> {

    public void getHotImage(RequestBody body) {
        Disposable disposable = ApiService.getImageTypeAoi().getHotList(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PictureResultBean>() {
                    @Override
                    public void accept(PictureResultBean result) throws Exception {
                        baseview.resultDraft(result);
                    }
                });
        addSubscription(disposable);
    }
}
