package com.pufei.gxdt.module.user.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.module.news.activity.NewsFeedBackActivity;
import com.pufei.gxdt.utils.AppManager;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutProductActivity extends BaseActivity {
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_title_right)
    LinearLayout llTitleRight;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;

    @Override
    public void initView() {
        llTitleLeft.setVisibility(View.VISIBLE);
        llTitleRight.setVisibility(View.INVISIBLE);
        tvTitle.setText("关于产品");
    }

    @Override
    public void getData() {
        PackageManager packageManager = getPackageManager();
        String packageName = getPackageName();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            if (packageInfo != null) {
                tvVersion.setText("斗图大师 V" + packageInfo.versionName);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_about_product;
    }

    @OnClick({R.id.ll_title_left, R.id.login_about, R.id.tv_agreement, R.id.tv_user_feedback, R.id.tv_user_manage, R.id.tv_user_score})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.login_about:
                break;
            case R.id.tv_agreement:
                startActivity(new Intent(this, AgreeementActivity.class));
                break;
            case R.id.tv_user_feedback:
                startActivity(new Intent(this, NewsFeedBackActivity.class));
                break;
            case R.id.tv_user_manage:
                startActivity(new Intent(this, ManagerRulesActivity.class));
                break;
            case R.id.tv_user_score:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "您的系统中没有安装应用市场", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

}
