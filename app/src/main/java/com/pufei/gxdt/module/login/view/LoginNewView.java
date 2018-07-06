package com.pufei.gxdt.module.login.view;

import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.home.model.LoginNewBean;
import com.pufei.gxdt.module.login.model.LoginResultBean;
import com.pufei.gxdt.module.login.model.SendCodeBean;
import com.pufei.gxdt.module.login.presenter.LoginNewPresenter;
import com.pufei.gxdt.module.login.presenter.LoginPresenter;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;

public interface LoginNewView extends BaseView<LoginNewPresenter> {
    void sendCode(SendCodeBean sendCodeBean);
    void LoginNewResult(LoginNewBean loginNewBean);
    void bindNewResult(SendCodeBean bean);
    void requestErrResult(String msg);
}
