package com.pufei.gxdt.module.discover.view;

import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.discover.presenter.DisWorksPresenter;

public interface DisWorksView extends BaseView<DisWorksPresenter> {
    void requestErrResult(String msg);
}
