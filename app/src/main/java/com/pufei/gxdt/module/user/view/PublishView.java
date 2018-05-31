package com.pufei.gxdt.module.user.view;


import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.user.bean.MyImagesBean;
import com.pufei.gxdt.module.user.presenter.PublishPresenter;

public interface PublishView extends BaseView<PublishPresenter> {
    void resultPublish(MyImagesBean bean);
}
