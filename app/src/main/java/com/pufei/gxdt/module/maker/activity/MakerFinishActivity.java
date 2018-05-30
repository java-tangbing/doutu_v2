package com.pufei.gxdt.module.maker.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.widgets.GlideApp;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class MakerFinishActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_face_image)
    ImageView ivFaceImage;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    public static final String IMAGE_PATH = "image_path";

    @Override
    public void initView() {
        llTitleLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);
    }

    @Override
    public void getData() {
        Intent intent = getIntent();
        String path = intent.getStringExtra(IMAGE_PATH);
        GlideApp.with(this).load(new File(path)).into(ivFaceImage);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_maker_finish;
    }

    @OnClick({R.id.ll_title_left, R.id.btn_publish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.btn_publish:
                break;
        }
    }
}
