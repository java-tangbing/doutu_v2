package com.pufei.gxdt.module.user.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.jaeger.library.StatusBarUtil;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.contents.EventMsg;
import com.pufei.gxdt.utils.AppManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class AccountSafetyActivity extends BaseActivity {
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_title_right)
    LinearLayout llTitleRight;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_set_password)
    TextView tvSetPassword;


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventMsg type) {
        if (type.getTYPE() == 11) {
            tvSetPassword.setText("修改密码");
        }
    }


    @Override
    public void initView() {
        llTitleLeft.setVisibility(View.VISIBLE);
        llTitleRight.setVisibility(View.GONE);
        tvTitle.setText("帐号安全");
    }

    @Override
    public void getData() {
        if (!TextUtils.isEmpty(App.userBean.getPhone())) {
            String phone = App.userBean.getPhone();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < phone.length(); i++) {
                char c = phone.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            tvPhone.setText(sb);
        }
        Log.e("LoginActivity", App.userBean.isPwd()+"");

        if (App.userBean.isPwd()) {
            tvSetPassword.setText("修改密码");
        } else {
            tvSetPassword.setText("设置密码");
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_account_safety;
    }



    @OnClick({R.id.ll_title_left, R.id.tv_set_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.tv_set_password:
//                startActivity(new Intent(this, SettingPwdActivity.class));
                break;
        }
    }

}
