package com.pufei.gxdt.module.news.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.news.adapter.NewsFeedBackAdapter;
import com.pufei.gxdt.module.news.bean.NewsBean;
import com.pufei.gxdt.module.news.bean.NoticeBean;
import com.pufei.gxdt.module.news.bean.SendBean;
import com.pufei.gxdt.module.news.presenter.NewsPresenter;
import com.pufei.gxdt.module.news.view.NewsView;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.SystemInfoUtils;
import com.pufei.gxdt.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class NewsFeedBackActivity extends BaseMvpActivity<NewsPresenter> implements NewsView {
    @BindView(R.id.news_feedback_rv)
    RecyclerView recyclerView;
    @BindView(R.id.ll_title_left)
    LinearLayout backlinearLayout;
    @BindView(R.id.tv_title)
    TextView textViewTitle;
    @BindView(R.id.news_feedback_user_message_et)
    EditText editTextMessage;

    NewsFeedBackAdapter newsFeedBackAdapter;
    private List<NewsBean.ResultBean> mlist;
    private String auth = "";
    private String advice = "";

    @Override
    public void initView() {
        auth = SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH);
        textViewTitle.setText(getResources().getString(R.string.news_feedback));
        backlinearLayout.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManage = new LinearLayoutManager(this);
        layoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManage);
    }

    @Override
    public void getData() {
        mlist = new ArrayList<>();
        newsFeedBackAdapter = new NewsFeedBackAdapter(mlist);
        recyclerView.setAdapter(newsFeedBackAdapter);
        if (App.userBean != null) {
            setMyAdapter();
        }
    }

    public void setMyAdapter() {

        JSONObject jsonObject = KeyUtil.getJson(this);
        try {
            jsonObject.put("auth", auth);
            jsonObject.put("type", 3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.newsNoticeContent(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } else {
            ToastUtils.showShort(this,  getResources().getString(R.string.check_the_network_please));
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_news_feedback;
    }


    @OnClick({R.id.ll_title_left, R.id.news_feedback_send_message_bt})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.news_feedback_send_message_bt:
                sendMessage();
                break;
        }
    }

    public void sendMessage() {
        advice = editTextMessage.getText().toString();
        JSONObject jsonObject = KeyUtil.getJson(this);
        try {
            jsonObject.put("auth", auth);
            jsonObject.put("advice", advice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.sendAdvice(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } else {
            ToastUtils.showShort(this,  getResources().getString(R.string.check_the_network_please));
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
        if (bean.getResult() != null) {
            mlist.addAll(bean.getResult());
            newsFeedBackAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getAdviceResult(SendBean bean) {
        if (bean != null) {
//            if (bean.getCode() == "0") {
                NewsBean.ResultBean resultBean = new NewsBean.ResultBean();
                resultBean.setContent(advice);
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String date = sDateFormat.format(new java.util.Date());
                resultBean.setDateline(date);
                resultBean.setUrl(App.userBean.getHead());
                resultBean.setOrgin("1");
                mlist.add(resultBean);
                newsFeedBackAdapter.notifyDataSetChanged();
//            }else {
//                ToastUtils.showShort(this, "发送失败");
//            }
        }

    }
}
