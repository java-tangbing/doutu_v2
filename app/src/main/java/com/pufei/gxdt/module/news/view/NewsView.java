package com.pufei.gxdt.module.news.view;

import com.pufei.gxdt.base.BaseView;

import com.pufei.gxdt.module.news.bean.NoticeBean;
import com.pufei.gxdt.module.news.presenter.NewsPresenter;

public interface NewsView extends BaseView<NewsPresenter> {
    void getNoticeList(NoticeBean bean);
}
