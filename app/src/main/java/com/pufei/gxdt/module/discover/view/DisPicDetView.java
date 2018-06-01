package com.pufei.gxdt.module.discover.view;

import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.discover.bean.DisPicDetBean;
import com.pufei.gxdt.module.discover.presenter.DisPicDetPresenter;
import com.pufei.gxdt.module.home.model.PictureDetailBean;


public interface DisPicDetView extends BaseView<DisPicDetPresenter> {
    void getImageDetail(DisPicDetBean bean);
    void resultImageDetail(PictureDetailBean bean);

}
