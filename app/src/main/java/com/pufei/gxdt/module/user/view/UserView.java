package com.pufei.gxdt.module.user.view;


import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.user.bean.CheckinBean;
import com.pufei.gxdt.module.user.presenter.UserPresenter;

public interface UserView extends BaseView<UserPresenter> {
    void wrongStatistical(int wrong);
    void answerTotal(int total);
    void examAverage(int average);
    void showCheckinState(CheckinBean bean);
    void requestErrResult(String msg);
}
