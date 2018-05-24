package com.pufei.gxdt.module.news.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.news.adapter.NewsPictureAdapter;

import com.pufei.gxdt.module.news.bean.NewsSystemBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NewsPictureActivity extends BaseMvpActivity {
    @BindView(R.id.news_picture_rv)
    RecyclerView recyclerView;
    NewsPictureAdapter newsPictureAdapter;
    private List<NewsSystemBean> mlist;

    @Override
    public void initView() {
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
        newsPictureAdapter = new NewsPictureAdapter(mlist);
//        newsSystemAdapter.setOnItemClickListener(this);
//        discoverAdapter.addHeaderView(videoHeaderView);
        recyclerView.setAdapter(newsPictureAdapter);
//        setAdapter();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_news_picture;
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
//        this.presenter = presenter;

    }
}
