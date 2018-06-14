package com.pufei.gxdt.module.user.view;


import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.login.model.LoginResultBean;
import com.pufei.gxdt.module.user.bean.BindAccountBean;
import com.pufei.gxdt.module.user.bean.SetsBean;
import com.pufei.gxdt.module.user.presenter.SettingPresenter;

public interface SettingView extends BaseView<SettingPresenter> {
    void resultSets(SetsBean bean);
    void sendRusult(BindAccountBean sendCodeBean);
    void requestErrResult(String msg);
}
