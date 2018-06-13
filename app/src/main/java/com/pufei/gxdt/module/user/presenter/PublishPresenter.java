package com.pufei.gxdt.module.user.presenter;


import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.user.view.PublishView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class PublishPresenter extends BasePresenter<PublishView> {

    public void getPublish(RequestBody body) {
            Disposable disposable = ApiService.getPersonalApi().getMymyDesignImages(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<PictureResultBean>() {
                    @Override
                    public void accept(PictureResultBean result) throws Exception {
                        baseview.resultPublish(result);
                    }
                });
        addSubscription(disposable);
    }
}
