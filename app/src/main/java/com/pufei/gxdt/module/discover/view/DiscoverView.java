package com.pufei.gxdt.module.discover.view;

import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.discover.bean.DiscoverEditImageBean;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import com.pufei.gxdt.module.discover.presenter.DiscoverPresenter;

public interface DiscoverView extends BaseView<DiscoverPresenter> {
    void getDiscoverHotList(DiscoverListBean bean);

    void getDiscoverDetailed(DiscoverEditImageBean bean);

}
