package com.pufei.gxdt.module.news.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.discover.activity.DiscoverDetailedActivity;
import com.pufei.gxdt.module.news.adapter.NewsPictureAdapter;
import com.pufei.gxdt.module.news.bean.NewsBean;
import com.pufei.gxdt.module.news.bean.NoticeBean;
import com.pufei.gxdt.module.news.bean.SendBean;
import com.pufei.gxdt.module.news.presenter.NewsPresenter;
import com.pufei.gxdt.module.news.view.NewsView;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.SystemInfoUtils;
import com.pufei.gxdt.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsPictureActivity extends BaseMvpActivity<NewsPresenter> implements NewsView, BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.news_picture_rv)
    RecyclerView recyclerView;
    NewsPictureAdapter newsPictureAdapter;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private List<NewsBean.ResultBean> mlist;
    private String auth;
    private String orginid, orgintable, id, uid, mcount;

    @Override
    public void initView() {
        llTitleLeft.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.news_pic));
        LinearLayoutManager layoutManage = new LinearLayoutManager(this);
        layoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManage);
    }

    @Override
    public void getData() {
        mlist = new ArrayList<>();
        newsPictureAdapter = new NewsPictureAdapter(mlist);
        newsPictureAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(newsPictureAdapter);
        if (App.userBean != null) {
            getContent();
        }
    }

    private void getContent() {
        auth = SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH);
        JSONObject jsonObject = KeyUtil.getJson(this);
        try {
            jsonObject.put("auth", auth);
            jsonObject.put("type", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.newsNoticeContent(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } else {
            ToastUtils.showShort(this, getResources().getString(R.string.check_the_network_please));
        }
//        Map<String, String> map = new HashMap<>();
//        map.put("auth", App.userBean.getAuth());
//        map.put("sign", "sign");
//        map.put("key", "key");
//        map.put("deviceid", SystemInfoUtils.deviced(this));
//        map.put("version", SystemInfoUtils.versionName(this));
//        map.put("os", "1");
//        map.put("timestamp", System.currentTimeMillis() / 1000 + "");
//        map.put("type", "2");
//        if (NetWorkUtil.isNetworkConnected(this)) {
//            presenter.newsNoticeContent(RetrofitFactory.getRequestBody(new Gson().toJson(map)));
//        } else {
//            ToastUtils.showShort(this, "请检查网络设置");
//        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_news_picture;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.news_picture_item:
                Intent intent01 = new Intent(this, DiscoverDetailedActivity.class);
                Bundle bundle01 = new Bundle();
                bundle01.putString("id", mlist.get(position).getImages().getId());
                bundle01.putString("orginid", mlist.get(position).getImages().getOrginid());
                bundle01.putString("orgintable", mlist.get(position).getImages().getOrgintable());
                bundle01.putString("uid", mlist.get(position).getImages().getUid());
                intent01.putExtras(bundle01);
                startActivity(intent01);
                break;
        }
    }

    @Override
    public void getNoticeList(NoticeBean bean) {

    }

    @Override
    public void getsNoticeContent(NewsBean bean) {
        if (bean.getResult() != null) {
            mlist.addAll(bean.getResult());
            newsPictureAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getAdviceResult(SendBean bean) {

    }

    @Override
    public void setPresenter(NewsPresenter presenter) {
        if (presenter == null) {
            this.presenter = new NewsPresenter();
            this.presenter.attachView(this);
        }
    }


    @OnClick(R.id.ll_title_left)
    public void onViewClicked() {
        AppManager.getAppManager().finishActivity();
    }
}
