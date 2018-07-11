package com.pufei.gxdt.module.login.view;

import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.login.presenter.UnBindPresenter;
import com.pufei.gxdt.module.user.bean.BindAccountBean;

public interface UnBindView extends BaseView<UnBindPresenter> {
    void unBindResult(BindAccountBean sendCodeBean);
    void requestErrResult(String msg);
}
