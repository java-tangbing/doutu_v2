package com.pufei.gxdt.module.home.view;


import com.pufei.gxdt.base.BaseView;

import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.presenter.ImageTypePresenter;

public interface ImageTypeView extends BaseView<ImageTypePresenter> {
    void resultHotImage(PictureResultBean bean);
}
