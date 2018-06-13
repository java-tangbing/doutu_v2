package com.pufei.gxdt.module.home.view;


import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.HomeResultBean;
import com.pufei.gxdt.module.home.model.HomeTypeBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.presenter.HomeListPresenter;


public interface HomeListView extends BaseView<HomeListPresenter> {
    void resultHomeList(HomeResultBean bean);
    void resultHomeDetailList(PictureResultBean bean);
    void resultHomeTypeList(HomeTypeBean bean);
    void resultCountView(FavoriteBean bean);
}
