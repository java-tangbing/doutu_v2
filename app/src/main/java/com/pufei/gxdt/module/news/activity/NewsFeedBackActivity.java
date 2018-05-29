package com.pufei.gxdt.module.news.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.news.adapter.NewsFeedBackAdapter;
import com.pufei.gxdt.module.news.adapter.NewsSystemAdapter;
import com.pufei.gxdt.module.news.bean.NewsSystemBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NewsFeedBackActivity extends BaseMvpActivity {
    @BindView(R.id.news_feedback_rv)
    RecyclerView recyclerView;
    @BindView(R.id.ll_title_left)
    LinearLayout backlinearLayout;
    NewsFeedBackAdapter newsFeedBackAdapter;
    private List<NewsSystemBean> mlist;

    @Override
    public void initView() {
        backlinearLayout.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManage = new LinearLayoutManager(this);
        layoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManage);
    }

    @Override
    public void getData() {
        mlist = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            NewsSystemBean bean = new NewsSystemBean();
            mlist.add(bean);
        }
        newsFeedBackAdapter = new NewsFeedBackAdapter(mlist);
//        newsSystemAdapter.setOnItemClickListener(this);
//        discoverAdapter.addHeaderView(videoHeaderView);
        recyclerView.setAdapter(newsFeedBackAdapter);
//        setAdapter();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_news_feedback;
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
//        this.presenter = presenter;

    }
}
