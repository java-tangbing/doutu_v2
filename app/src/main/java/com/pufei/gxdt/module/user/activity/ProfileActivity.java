package com.pufei.gxdt.module.user.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.module.login.activity.LoginActivity;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.DialogUtil;
import com.pufei.gxdt.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity {
    @BindView(R.id.setting_return)
    ImageView settingReturn;
    @BindView(R.id.setting_data_editor)
    LinearLayout settingDataEditor;
    @BindView(R.id.setting_version_checking)
    LinearLayout settingVersionChecking;
    @BindView(R.id.setting_about_product)
    LinearLayout settingAboutProduct;
    @BindView(R.id.setting_log_out)
    Button settingLogOut;

    @Override
    public void initView() {
        if (App.userBean != null) {
            settingLogOut.setText(R.string.log_out);
        }
    }

    @Override
    public void getData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_profile;
    }

    @OnClick({R.id.setting_return, R.id.setting_data_editor, R.id.setting_version_checking, R.id.setting_about_product, R.id.setting_log_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting_return:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.setting_data_editor:
                if (App.userBean != null) {
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                }
                break;
            case R.id.setting_version_checking:
                DialogUtil.getInstance().showVersionDialog(this);
                break;
            case R.id.setting_about_product:
                break;
            case R.id.setting_log_out:
                if (App.userBean != null) {
                    DialogUtil.getInstance().canceDialog(this);
                } else {
                    ToastUtils.showLong(this, "请先登录");
                }
                break;
        }
    }

}
