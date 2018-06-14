package com.pufei.gxdt.module.login.view;

import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.login.presenter.SettingPresenter;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;

public interface SettingView extends BaseView<SettingPresenter> {
    void setNewPwd(ModifyResultBean result);
    void updatePwd(ModifyResultBean result);
    void requestErrResult(String msg);
}
