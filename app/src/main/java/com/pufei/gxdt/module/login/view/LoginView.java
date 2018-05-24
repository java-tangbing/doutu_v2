package com.pufei.gxdt.module.login.view;

import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.login.model.LoginResultBean;
import com.pufei.gxdt.module.login.model.SendCodeBean;
import com.pufei.gxdt.module.login.presenter.LoginPresenter;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;

public interface LoginView extends BaseView<LoginPresenter> {
    void sendCode(SendCodeBean sendCodeBean);

    void sendRusult(LoginResultBean sendCodeBean);

    void retrievePwdResult(ModifyResultBean bean);
}
