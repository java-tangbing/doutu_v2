package com.pufei.gxdt.module.login.activity;

import android.content.Intent;
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
import com.pufei.gxdt.MainActivity;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.contents.MsgType;
import com.pufei.gxdt.module.login.model.LoginResultBean;
import com.pufei.gxdt.module.login.model.SendCodeBean;
import com.pufei.gxdt.module.login.presenter.LoginPresenter;
import com.pufei.gxdt.module.login.view.LoginView;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;
import com.pufei.gxdt.module.user.bean.UserBean;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.EvenMsg;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;

public class BindPhoneActivity extends BaseMvpActivity<LoginPresenter> implements LoginView {
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
    private String openid;
    private String nickName;
    private int gender;
    private String iconUrl;
    private int type;

    @Override
    public void initView() {
        loginIphone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        loginCode.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        myCountDown = new MyCountDown(60000, 1000);

    }

    @Override
    public void getData() {
        if (TextUtils.isEmpty(getIntent().getStringExtra("sysMsg"))) {

        } else {
            openid = getIntent().getStringExtra("openId");
            nickName = getIntent().getStringExtra("nickName");
            gender = getIntent().getIntExtra("gender", 0);
            iconUrl = getIntent().getStringExtra("iconUrl");
            type = getIntent().getIntExtra("type", 1);
        }
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
            Toast.makeText(BindPhoneActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(BindPhoneActivity.this, sendCodeBean.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void sendRusult(LoginResultBean resultBean) {
        if (resultBean.getCode().equals(Contents.CODE_ZERO)) {
            LoginResultBean.ResultBean bean = resultBean.getResult();
            String name = "";
            String header = "";
            String gender = "";
            String address = "";
//            if (!TextUtils.isEmpty(bean.get())) {
//                name = bean.getNickname();
//            } else {
//                name = "萌新上路";
//            }
//            if (!TextUtils.isEmpty(bean.getAvatar())) {
//                header = bean.getAvatar();
//            }
            if (!TextUtils.isEmpty(bean.getGender())) {
                gender = bean.getGender();
            } else {
                gender = "保密";
            }
            if (!TextUtils.isEmpty(bean.getCity())) {
                address = bean.getCity();
            } else {
                address = "未知";
            }
            SharedPreferencesUtil.getInstance().putString(Contents.STRING_AUTH, bean.getAuth());
            App.userBean = new UserBean(name, header, gender, address, bean.getAuth(), "");
            EventBus.getDefault().post(new EvenMsg(MsgType.LOGIN_SUCCESS));
            SharedPreferencesUtil.getInstance().putString(Contents.USER_DETAIL, UserUtils.getUser(App.userBean));
            Intent intent = new Intent(BindPhoneActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            Toast.makeText(BindPhoneActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

        } else if (resultBean.getCode().equals(Contents.CODE_ONE)) {
            ToastUtils.showShort(this, resultBean.getMsg());
        } else {
            Toast.makeText(BindPhoneActivity.this, resultBean.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void retrievePwdResult(ModifyResultBean bean) {

    }

    @Override
    public void setPresenter(LoginPresenter presenter) {
        if (presenter == null) {
            this.presenter = new LoginPresenter();
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
                    Map<String, String> map = new HashMap<>();
                    map.put("mobile", loginIphone.getText().toString());
                    if (NetWorkUtil.isNetworkConnected(this)) {
                        presenter.sendCode(RetrofitFactory.getRequestBody(new Gson().toJson(map)));
                    } else {
                        ToastUtils.showShort(this, "请检查网络设置");

                    }
                }
                break;
            case R.id.btn_login:
                Map<String, String> map = new HashMap<>();
                map.put("openid", openid);
                map.put("type", type + "");
                map.put("mobile", loginIphone.getText().toString());
                map.put("vcode", loginCode.getText().toString());
                map.put("sex", gender + "");
                map.put("nickname", nickName);
                map.put("headimgurl", iconUrl);
                if (NetWorkUtil.isNetworkConnected(this)) {
                    presenter.bindPhone(RetrofitFactory.getRequestBody(new Gson().toJson(map)));
                } else {
                    ToastUtils.showShort(this, "请检查网络设置");

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
