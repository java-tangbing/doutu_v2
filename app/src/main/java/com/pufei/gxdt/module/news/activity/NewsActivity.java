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
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.login.activity.LoginActivity;
import com.pufei.gxdt.module.news.adapter.NewsAdapter;
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

public class NewsActivity extends BaseMvpActivity<NewsPresenter> implements NewsView, BaseQuickAdapter.OnItemChildClickListener {
//    @BindView(R.id.news_system_message)
//    LinearLayout systemlinearLayout;
//    @BindView(R.id.news_picture_message)
//    LinearLayout picturelinearLayout;
//    @BindView(R.id.news_feedback_message)
//    LinearLayout feedbacklinearLayout;

    @BindView(R.id.ll_title_left)
    LinearLayout backlinearLayout;
    @BindView(R.id.tv_title)
    TextView textViewtitle;

    @BindView(R.id.news_item_rv)
    RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<NoticeBean.ResultBean> mlist;
    private String auth = "";


    @Override
    public void initView() {


        textViewtitle.setText("消息");
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
            auth = SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH);
            JSONObject jsonObject = KeyUtil.getJson(this);
            try {
                jsonObject.put("auth", auth);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (NetWorkUtil.isNetworkConnected(this)) {
                presenter.newsNoticeList(RetrofitFactory.getRequestBody(jsonObject.toString()));
            } else {
                ToastUtils.showShort(this, "请检查网络设置");
            }
        }

    }

    @Override
    public int getLayout() {
        return R.layout.activity_news;
    }


//    @OnClick({R.id.news_system_message, R.id.news_picture_message, R.id.news_feedback_message})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.news_system_message:
//                Intent intent = new Intent(this, NewsSystemActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.news_picture_message:
//                Intent intent1 = new Intent(this, NewsPictureActivity.class);
//                startActivity(intent1);
//                break;
//            case R.id.news_feedback_message:
//                Intent intent2 = new Intent(this, NewsFeedBackActivity.class);
//                startActivity(intent2);
//                break;
//        }
//    }

    @Override
    public void setPresenter(NewsPresenter presenter) {
        if (presenter == null) {
            this.presenter = new NewsPresenter();
            this.presenter.attachView(this);
        }

    }

    @Override
    public void getNoticeList(NoticeBean bean) {
        if (bean.getResult().size()>0) {
            mlist.addAll(bean.getResult());
            newsAdapter.notifyDataSetChanged();
        }
//        ToastUtils.showShort(this, bean.getMsg());
    }

    @Override
    public void getsNoticeContent(NewsBean bean) {

    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (mlist.get(position).getType()) {
            case "1":
                Intent intent = new Intent(this, NewsSystemActivity.class);
//                Bundle bundle = new Bundle();
////                bundle.putString("auth", auth);
//                bundle.putString("type", "1");
//                intent.putExtras(bundle);
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
}
