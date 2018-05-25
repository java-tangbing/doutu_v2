package com.pufei.gxdt.module.user.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.utils.AppManager;

import butterknife.BindView;
import butterknife.OnClick;

public class AgreeementActivity extends BaseActivity {
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_title_right)
    LinearLayout llTitleRight;
    @BindView(R.id.web_agreement)
    WebView webAgreement;

    @Override
    public void initView() {
        tvTitle.setText("用户协议");
        llTitleLeft.setVisibility(View.VISIBLE);
        llTitleRight.setVisibility(View.GONE);
        WebSettings webSettings = webAgreement.getSettings();

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("utf-8");
    }

    @Override
    public void getData() {
        webAgreement.loadUrl("https://jiakao.xianwan.com/driver/index/agreement");
    }

    @Override
    public int getLayout() {
        return R.layout.activity_agreement;
    }


    @OnClick(R.id.ll_title_left)
    public void onViewClicked() {
        AppManager.getAppManager().finishActivity();
    }
}
