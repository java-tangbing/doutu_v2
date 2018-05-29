package com.pufei.gxdt.module.home.view;


import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.home.model.HomeDetailBean;
import com.pufei.gxdt.module.home.model.HomeResultBean;
import com.pufei.gxdt.module.home.presenter.HomeListPresenter;


public interface HomeListView extends BaseView<HomeListPresenter> {
    void resultHomeList(HomeResultBean bean);
    void resultHomeDetailList(HomeDetailBean bean);
}
