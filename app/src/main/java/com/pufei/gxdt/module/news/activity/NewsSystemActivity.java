package com.pufei.gxdt.module.news.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.base.BaseView;
import com.pufei.gxdt.module.discover.presenter.DiscoverPresenter;
import com.pufei.gxdt.module.news.activity.adapter.NewsSystemAdapter;
import com.pufei.gxdt.module.view.DiscoverView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NewsSystemActivity extends BaseMvpActivity {
    @BindView(R.id.news_system_rv)
    RecyclerView recyclerView;
    NewsSystemAdapter newsSystemAdapter;
    private List<String> mlist;

    @Override
    public void initView() {
        LinearLayoutManager layoutManage = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManage);
    }

    @Override
    public void getData() {
        mlist = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String str = "";
            mlist.add(str);
        }
        newsSystemAdapter = new NewsSystemAdapter(mlist);
//        newsSystemAdapter.setOnItemClickListener(this);
//        discoverAdapter.addHeaderView(videoHeaderView);
        recyclerView.setAdapter(newsSystemAdapter);
//        setAdapter();
    }

    @Override
    public int getLayout() {
        return 0;
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        this.presenter = presenter;

    }
}
