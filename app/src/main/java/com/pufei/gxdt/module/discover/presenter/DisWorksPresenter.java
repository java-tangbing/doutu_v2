package com.pufei.gxdt.module.discover.presenter;

import com.pufei.gxdt.api.ApiService;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.discover.bean.DisPicDetBean;
import com.pufei.gxdt.module.discover.view.DisWorksView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class DisWorksPresenter extends BasePresenter<DisWorksView> {
//    public void worksDetail(RequestBody body) {
//        Disposable disposable = ApiService.getDiscoverApi().getImageDetail(body)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<DisPicDetBean>() {
//                    @Override
//                    public void accept(DisPicDetBean noticeBean) throws Exception {
//                        baseview.getImageDetail(noticeBean);
//                    }
//                });
//        addSubscription(disposable);
//    }
}
