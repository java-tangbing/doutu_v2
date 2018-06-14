package com.pufei.gxdt.module.user.view;


import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.user.presenter.DraftPresenter;
import com.pufei.gxdt.module.user.presenter.PublishPresenter;

public interface DraftView extends BaseView<DraftPresenter> {
    void resultDraft(PictureResultBean bean);
    void requestErrResult(String msg);
}
