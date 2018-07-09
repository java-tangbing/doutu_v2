package com.pufei.gxdt.module.login.activity;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.contents.EventMsg;
import com.pufei.gxdt.contents.MsgType;
import com.pufei.gxdt.module.home.model.LoginNewBean;
import com.pufei.gxdt.module.login.model.SendCodeBean;
import com.pufei.gxdt.module.login.presenter.LoginNewPresenter;
import com.pufei.gxdt.module.login.view.LoginNewView;
import com.pufei.gxdt.module.user.bean.UserBean;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class BindPhoneNewActivity extends BaseMvpActivity<LoginNewPresenter> implements LoginNewView {
    @BindView(R.id.login_finish)
    ImageView loginFinish;
    @BindView(R.id.login_iphone)
    EditText loginIphone;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.login_code)
    EditText loginCode;
    @BindView(R.id.login_sendcode)
    TextView loginSendcode;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private boolean isSendingCode = false;
    private MyCountDown myCountDown;

    @Override
    public void initView() {
        loginIphone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        loginCode.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        myCountDown = new MyCountDown(60000, 1000);
    }

    @Override
    public void getData() {
    }

    @Override
    public int getLayout() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public void sendCode(SendCodeBean sendCodeBean) {
        if (sendCodeBean.getCode().equals(Contents.CODE_ZERO)) {
            isSendingCode = true;
            myCountDown.start();
            loginSendcode.setTextColor(getResources().getColor(R.color.circle_color));
            loginSendcode.setFocusable(false);
            Toast.makeText(BindPhoneNewActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(BindPhoneNewActivity.this, sendCodeBean.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void LoginNewResult(LoginNewBean loginNewBean) {

    }

    @Override
    public void bindNewResult(SendCodeBean sendCodeBean) {
        if (sendCodeBean.getCode().equals(Contents.CODE_ZERO)) {
            UserBean bean = App.userBean;
            bean.setPhone(loginIphone.getText().toString());
            Toast.makeText(BindPhoneNewActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
//            if (AppManager.getAppManager().activityStackCount() == 1 || AppManager.getAppManager().activityStackCount() == 2) {
//                Intent intent = new Intent(BindPhoneNewActivity.this, MainActivity.class);
//                startActivity(intent);
//            } else {
            String user_detail = SharedPreferencesUtil.getInstance().getString(Contents.USER_DETAIL, null);
            if (user_detail != null) {
                EventBus.getDefault().postSticky(new EventMsg(MsgType.BIND_NEW));
                App.userBean = new Gson().fromJson(user_detail, UserBean.class);
//                }
            }
            AppManager.getAppManager().finishActivity();
        } else {
            ToastUtils.showShort(this, sendCodeBean.getMsg());
        }
    }

    @Override
    public void requestErrResult(String msg) {
        ToastUtils.showShort(this, msg);
    }

    @Override
    public void setPresenter(LoginNewPresenter presenter) {
        if (presenter == null) {
            this.presenter = new LoginNewPresenter();
            this.presenter.attachView(this);
        }
    }

    @OnClick({R.id.login_finish, R.id.iv_clear, R.id.login_sendcode, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_finish:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.iv_clear:
                loginIphone.setText("");
                break;
            case R.id.login_sendcode:
                if (!isSendingCode) {
                    try {
                        if (TextUtils.isEmpty(loginIphone.getText().toString())) {
                            ToastUtils.showLong(this, "请输入常用手机号");
                            break;
                        }
                        if (NetWorkUtil.isNetworkConnected(BindPhoneNewActivity.this)) {
                            JSONObject jsonObject = KeyUtil.getJson(BindPhoneNewActivity.this);
                            jsonObject.put("mobile", loginIphone.getText().toString());
                            presenter.sendCode(RetrofitFactory.getRequestBody(jsonObject.toString()));
                        } else {
                            ToastUtils.showShort(BindPhoneNewActivity.this, "请检查网络设置");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_login:
                try {
                    if (TextUtils.isEmpty(loginCode.getText().toString())) {
                        ToastUtils.showLong(this, "请输入验证码");
                        break;
                    }
                    if (NetWorkUtil.isNetworkConnected(BindPhoneNewActivity.this)) {
                        JSONObject jsonObject = KeyUtil.getJson(BindPhoneNewActivity.this);
                        jsonObject.put("mobile", loginIphone.getText().toString());
                        jsonObject.put("code", loginCode.getText().toString());
                        presenter.bindPhoneNew(RetrofitFactory.getRequestBody(jsonObject.toString()));
                    } else {
                        ToastUtils.showShort(BindPhoneNewActivity.this, "请检查网络设置");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public class MyCountDown extends CountDownTimer {

        public MyCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            loginSendcode.setText(millisUntilFinished / 1000 + "秒后重发");
        }

        @Override
        public void onFinish() {
            isSendingCode = false;
            loginSendcode.setText("获取验证码");
            loginSendcode.setFocusable(true);
            loginSendcode.setTextColor(getResources().getColor(R.color.blank));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myCountDown.onFinish();
    }
}
