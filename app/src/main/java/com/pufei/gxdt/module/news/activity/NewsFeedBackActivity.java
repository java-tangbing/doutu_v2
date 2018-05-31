package com.pufei.gxdt.module.news.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.news.adapter.NewsFeedBackAdapter;
import com.pufei.gxdt.module.news.bean.NewsBean;
import com.pufei.gxdt.module.news.bean.NoticeBean;
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

public class NewsFeedBackActivity extends BaseMvpActivity<NewsPresenter> implements NewsView {
    @BindView(R.id.news_feedback_rv)
    RecyclerView recyclerView;
    @BindView(R.id.ll_title_left)
    LinearLayout backlinearLayout;
    @BindView(R.id.tv_title)
    TextView textViewTitle;

    NewsFeedBackAdapter newsFeedBackAdapter;
    private List<NewsBean.ResultBean> mlist;
    private String auth = "";

    @Override
    public void initView() {
        textViewTitle.setText(getResources().getString(R.string.news_feedback));
        backlinearLayout.setVisibility(View.VISIBLE);
        auth = SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH);

        LinearLayoutManager layoutManage = new LinearLayoutManager(this);
        layoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManage);
    }

    @Override
    public void getData() {
        mlist = new ArrayList<>();
        newsFeedBackAdapter = new NewsFeedBackAdapter(mlist);
        recyclerView.setAdapter(newsFeedBackAdapter);
        setMyAdapter();
    }

    public void setMyAdapter() {
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
            ToastUtils.showShort(this, "请检查网络设置");
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_news_feedback;
    }


    @OnClick(R.id.ll_title_left)
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
        }
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
            newsFeedBackAdapter.notifyDataSetChanged();
        }
    }
}
