package com.pufei.gxdt.module.news.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

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


    @OnClick({R.id.news_system_message, R.id.news_picture_message, R.id.news_feedback_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.news_system_message:
                Intent intent = new Intent(this, NewsSystemActivity.class);
                startActivity(intent);
                break;
            case R.id.news_picture_message:
                Intent intent1 = new Intent(this, NewsPictureActivity.class);
                startActivity(intent1);
                break;
            case R.id.news_feedback_message:
                break;
        }
    }
}
