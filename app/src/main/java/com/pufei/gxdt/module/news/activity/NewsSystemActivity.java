package com.pufei.gxdt.module.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;

import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.login.activity.BindPhoneActivity;
import com.pufei.gxdt.module.news.adapter.NewsSystemAdapter;
import com.pufei.gxdt.module.news.bean.NewsBean;
import com.pufei.gxdt.module.news.bean.NoticeBean;
import com.pufei.gxdt.module.news.bean.SendBean;
import com.pufei.gxdt.module.news.presenter.NewsPresenter;
import com.pufei.gxdt.module.news.view.NewsView;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NewsSystemActivity extends BaseMvpActivity<NewsPresenter> implements NewsView, BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.tv_title)
    TextView textViewTitle;
    @BindView(R.id.ll_title_left)
    LinearLayout linearLayoutBack;

    @BindView(R.id.news_system_rv)
    RecyclerView recyclerView;
    private TextView textViewBD;
    NewsSystemAdapter newsSystemAdapter;
    private List<NewsBean.ResultBean> mlist;
    private View viewHeader;
    private String auth = "";
    private String type = "";
    private boolean isbdphone = false;
//    private HeaderViewBind headerViewBind;

    @Override
    public void initView() {
        if (App.userBean.getPhone().length() > 0) {
            isbdphone = true;
        }
        textViewTitle.setText(getResources().getString(R.string.news_system));
        linearLayoutBack.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManage = new LinearLayoutManager(this);
        layoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManage);
    }

    @Override
    public void getData() {


        mlist = new ArrayList<>();
        newsSystemAdapter = new NewsSystemAdapter(mlist, isbdphone);
        newsSystemAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(newsSystemAdapter);
        if (App.userBean != null) {
            setMyAdapter();
        }
    }


    public void setMyAdapter() {
        auth = SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH);
        JSONObject jsonObject = KeyUtil.getJson(this);
        try {
            jsonObject.put("auth", auth);
            jsonObject.put("type", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.newsNoticeContent(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } else {
            ToastUtils.showShort(this, getResources().getString(R.string.check_the_network_please));
        }
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

    @Override
    public void getAdviceResult(SendBean bean) {

    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.new_system_item_bd:
                Intent intent = new Intent(this, BindPhoneActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("sysMsg", "sysMsg");
                intent.putExtras(bundle);
                startActivity(intent);
                ToastUtils.showLong(NewsSystemActivity.this, getResources().getString(R.string.news_sys_bind_phone));
                break;
        }
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
