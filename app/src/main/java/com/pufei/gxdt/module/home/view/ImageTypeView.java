package com.pufei.gxdt.module.home.view;


import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.home.model.JokeDetailBean;
import com.pufei.gxdt.module.home.model.JokeResultBean;
import com.pufei.gxdt.module.home.presenter.JokePresenter;

public interface JokeView extends BaseView<JokePresenter> {
    void resultJokeList(JokeResultBean bean);
    void resultJokeDetail(JokeDetailBean bean);
}
