package com.pufei.gxdt.module.user.view;

import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.user.bean.SetAvatarResultBean;
import com.pufei.gxdt.module.user.bean.SetPersonalResultBean;
import com.pufei.gxdt.module.user.presenter.SetPersonalPresenter;

public interface SetPersonalView extends BaseView<SetPersonalPresenter> {
    void setPersonalInfo(SetPersonalResultBean bean);

    void setPersonAvatar(SetAvatarResultBean bean);
}
