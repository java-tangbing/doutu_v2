package com.pufei.gxdt.module.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.module.login.activity.LoginActivity;
import com.pufei.gxdt.module.news.adapter.NewsAdapter;
import com.pufei.gxdt.module.news.bean.NewsBean;
import com.pufei.gxdt.module.news.bean.NoticeBean;
import com.pufei.gxdt.module.news.bean.SendBean;
import com.pufei.gxdt.module.news.presenter.NewsPresenter;
import com.pufei.gxdt.module.news.view.NewsView;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SystemInfoUtils;
import com.pufei.gxdt.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsActivity extends BaseMvpActivity<NewsPresenter> implements NewsView, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.ll_title_left)
    LinearLayout backlinearLayout;
    @BindView(R.id.tv_title)
    TextView textViewtitle;

    @BindView(R.id.news_item_rv)
    RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<NoticeBean.ResultBean> mlist;


    @Override
    public void initView() {

        textViewtitle.setText(getResources().getString(R.string.news));
        backlinearLayout.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManage = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManage);
    }

    @Override
    public void getData() {
        mlist = new ArrayList<>();
        newsAdapter = new NewsAdapter(mlist);
        newsAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(newsAdapter);
        setMyadapter();
    }

    public void setMyadapter() {
        if (App.userBean == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("auth", App.userBean.getAuth());
            map.put("sign", "sign");
            map.put("key", "key");
            map.put("deviceid", SystemInfoUtils.deviced(this));
            map.put("version", SystemInfoUtils.versionName(this));
            map.put("os", "1");
            map.put("timestamp", System.currentTimeMillis() / 1000 + "");
            if (NetWorkUtil.isNetworkConnected(this)) {
                presenter.newsNoticeList(RetrofitFactory.getRequestBody(new Gson().toJson(map)));
            } else {
                ToastUtils.showShort(this, getResources().getString(R.string.check_the_network_please));
            }
        }

    }

    @Override
    public int getLayout() {
        return R.layout.activity_news;
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
        if (bean.getCode().equals("0")) {
            if (bean.getResult() != null) {
                if (bean.getResult().size() > 0) {
                    mlist.addAll(bean.getResult());
                    newsAdapter.notifyDataSetChanged();
                }
            }
        } else {
            ToastUtils.showShort(this, bean.getMsg());
        }


    }

    @Override
    public void getsNoticeContent(NewsBean bean) {

    }

    @Override
    public void getAdviceResult(SendBean bean) {

    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (mlist.get(position).getType()) {
            case "1":
                Intent intent = new Intent(this, NewsSystemActivity.class);
                startActivity(intent);
                break;
            case "2":
                Intent intent1 = new Intent(this, NewsPictureActivity.class);
                startActivity(intent1);
                break;
            case "3":
                Intent intent2 = new Intent(this, NewsFeedBackActivity.class);
                startActivity(intent2);
                break;
        }
    }


    @OnClick(R.id.ll_title_left)
    public void onViewClicked() {
        AppManager.getAppManager().finishActivity();
    }
}
