package com.pufei.gxdt.module.news.activity;

import android.widget.LinearLayout;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseActivity;

import butterknife.BindView;

public class NewsActivity extends BaseActivity {
    @BindView(R.id.news_system_message)
    LinearLayout systemlinearLayout;
    @BindView(R.id.news_picture_message)
    LinearLayout picturelinearLayout;
    @BindView(R.id.news_feedback_message)
    LinearLayout feedbacklinearLayout;
    @Override
    public void initView() {

    }

    @Override
    public void getData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_news;
    }
}
