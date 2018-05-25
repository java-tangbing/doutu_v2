package com.pufei.gxdt.module.home.view;


import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.model.ThemeResultBean;
import com.pufei.gxdt.module.home.presenter.ImageTypePresenter;
import com.pufei.gxdt.module.home.presenter.ThemeImagePresenter;

public interface ThemeImageView extends BaseView<ThemeImagePresenter> {
    void resultThemeImage(ThemeResultBean bean);
}
