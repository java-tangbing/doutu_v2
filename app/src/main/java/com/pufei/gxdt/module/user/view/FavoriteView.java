package com.pufei.gxdt.module.user.view;


import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.home.model.JokeDetailBean;
import com.pufei.gxdt.module.home.model.JokeResultBean;
import com.pufei.gxdt.module.user.bean.MyImagesBean;
import com.pufei.gxdt.module.user.presenter.FavoritePresenter;

public interface FavoriteView extends BaseView<FavoritePresenter> {
    void resultJokeList(MyImagesBean bean);
    void resultJokeDetail(MyImagesBean bean);
}
