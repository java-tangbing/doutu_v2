package com.pufei.gxdt.module.news.view;

import com.pufei.gxdt.base.BaseView;

import com.pufei.gxdt.module.news.bean.NewsBean;
import com.pufei.gxdt.module.news.bean.NewsTypeTwoBean;
import com.pufei.gxdt.module.news.bean.NoticeBean;
import com.pufei.gxdt.module.news.bean.SendBean;
import com.pufei.gxdt.module.news.presenter.NewsPresenter;

public interface NewsView extends BaseView<NewsPresenter> {
    void getNoticeList(NoticeBean bean);

    void getsNoticeContent(NewsBean bean);

    void getsNoticeContentTypeTwo(NewsTypeTwoBean bean);


    void getAdviceResult(SendBean bean);

}
