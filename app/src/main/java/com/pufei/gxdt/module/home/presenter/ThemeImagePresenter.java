package com.pufei.gxdt.module.home.presenter;


import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.model.ThemeResultBean;
import com.pufei.gxdt.module.home.view.ImageTypeView;
import com.pufei.gxdt.module.home.view.ThemeImageView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class ThemeImagePresenter extends BasePresenter<ThemeImageView> {

    public void getThemeImage(RequestBody body) {
            Disposable disposable = ApiService.getImageTypeAoi().getThemeImageList(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ThemeResultBean>() {
                    @Override
                    public void accept(ThemeResultBean result) throws Exception {
                        baseview.resultThemeImage(result);
                    }
                });
        addSubscription(disposable);
    }
}
