package com.pufei.gxdt.module.news.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.news.adapter.NewsSystemAdapter;
import com.pufei.gxdt.module.news.bean.NewsBean;
import com.pufei.gxdt.module.news.bean.NoticeBean;
import com.pufei.gxdt.module.news.presenter.NewsPresenter;
import com.pufei.gxdt.module.news.view.NewsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NewsSystemActivity extends BaseMvpActivity<NewsPresenter> implements NewsView {
    @BindView(R.id.news_system_rv)
    RecyclerView recyclerView;
    NewsSystemAdapter newsSystemAdapter;
    private List<NewsBean.ResultBean> mlist;
    private View viewHeader;

    @Override
    public void initView() {
        Intent intent = getIntent();
        intent.getStringExtra("auth");
        intent.getStringExtra("type");
        LinearLayoutManager layoutManage = new LinearLayoutManager(this);
        layoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManage);
    }

    @Override
    public void getData() {
        mlist = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            NewsBean bean = new NewsBean();
//            mlist.add(bean);
//        }
        newsSystemAdapter = new NewsSystemAdapter(mlist);
//        newsSystemAdapter.setOnItemClickListener(this);
        if (App.userBean.getPhone().length() < 5) {
            viewHeader = getLayoutInflater().inflate(R.layout.activity_news_item_unlanded, (ViewGroup) recyclerView.getParent(), false);
            newsSystemAdapter.addHeaderView(viewHeader);
        }

        recyclerView.setAdapter(newsSystemAdapter);
//        setAdapter();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_news_system;
    }


    @Override
    public void setPresenter(NewsPresenter presenter) {
        if (presenter == null) {
            this.presenter = new NewsPresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void getNoticeList(NoticeBean bean) {

    }

    @Override
    public void getsNoticeContent(NewsBean bean) {
        if (bean.getResult().size() > 0) {
            mlist.addAll(bean.getResult());
            newsSystemAdapter.notifyDataSetChanged();
        }
    }


}
