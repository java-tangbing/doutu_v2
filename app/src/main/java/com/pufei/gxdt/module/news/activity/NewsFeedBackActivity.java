package com.pufei.gxdt.module.news.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.news.adapter.NewsFeedBackAdapter;
import com.pufei.gxdt.module.news.bean.NewsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NewsFeedBackActivity extends BaseMvpActivity {
    @BindView(R.id.news_feedback_rv)
    RecyclerView recyclerView;
    @BindView(R.id.ll_title_left)
    LinearLayout backlinearLayout;
    @BindView(R.id.tv_title)
    TextView textViewTitle;

    NewsFeedBackAdapter newsFeedBackAdapter;
    private List<NewsBean.ResultBean> mlist;

    @Override
    public void initView() {
        textViewTitle.setText(getResources().getString(R.string.news_feedback));
        backlinearLayout.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.ll_title_left)
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
        }
    }
}
