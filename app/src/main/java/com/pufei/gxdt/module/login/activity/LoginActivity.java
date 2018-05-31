package com.pufei.gxdt.module.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.pufei.gxdt.module.user.activity.AgreeementActivity;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;
import com.pufei.gxdt.module.user.bean.UserBean;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.EvenMsg;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.utils.UmengStatisticsUtil;
import com.pufei.gxdt.utils.UserUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginView {
    @BindView(R.id.login_iphone)
    EditText loginIphone;
    @BindView(R.id.login_sendcode)
    TextView loginSendcode;
    @BindView(R.id.login_login_btn)
    Button loginLoginBtn;
    @BindView(R.id.login_code)
    EditText loginCode;
    @BindView(R.id.login_finish)
    ImageView loginFinish;
    @BindView(R.id.user_spinner)
    Spinner userSpinner;
    @BindView(R.id.cx_agreement)
    CheckBox cxAgreement;
    @BindView(R.id.tv_change_login_type)
    TextView tvChangeLoginType;
    private List<String> list = new ArrayList<>();
    private MyCountDown myCountDown;
    private boolean isSendingCode = false;
    private String nickName;
    private String openid;
    private String province;
    private String city;
    private String gender;
    private String header;
    private String orgin;
    private int type;
    private int loginFrom = -1;//判断是否为资讯详情跳转

    @Override
    public void setPresenter(LoginPresenter presenter) {
        if (presenter == null) {
            this.presenter = new LoginPresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void initView() {
        loginIphone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        loginCode.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        myCountDown = new MyCountDown(60000, 1000);
        list.add("+0086");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(adapter);
        userSpinner.setOnItemSelectedListener(new SpinnerListener());
    }

    @Override
    public void getData() {
        Intent intent = getIntent();
        loginFrom = intent.getIntExtra("LOGIN_NEWS_COMMENT", -1);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void sendCode(SendCodeBean sendCodeBean) {
        if (sendCodeBean.getCode().equals(Contents.CODE_ZERO)) {
            isSendingCode = true;
            myCountDown.start();
            loginSendcode.setTextColor(getResources().getColor(R.color.circle_color));
            loginSendcode.setFocusable(false);
            Toast.makeText(LoginActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, sendCodeBean.getMsg(), Toast.LENGTH_SHORT).show();
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
            App.userBean = new UserBean(name, header, gender, address, bean.getAuth(), bean.getMobile());
//            App.userBean.setPwd(bean.isPwd_set());
            Log.e("LoginActivity", App.userBean.isPwd() + "");
            EventBus.getDefault().post(new EvenMsg(MsgType.LOGIN_SUCCESS));
            SharedPreferencesUtil.getInstance().putString(Contents.USER_DETAIL, UserUtils.getUser(App.userBean));
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            AppManager.getAppManager().finishActivity();
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

        } else if (resultBean.getCode().equals(Contents.CODE_ONE)) {
            //ToastUtils.showShort(this, resultBean.getMsg());
            Intent intent = new Intent(this, BindPhoneActivity.class);
            intent.putExtra("openId", openid);
            intent.putExtra("iconUrl", header);
            intent.putExtra("nickName", nickName);
            intent.putExtra("gender", gender);
            intent.putExtra("type", type);
            startActivity(intent);
        } else {
            Toast.makeText(LoginActivity.this, resultBean.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void bindResult(SendCodeBean sendCodeBean) {

    }

    @Override
    public void retrievePwdResult(ModifyResultBean bean) {

    }

    @OnClick({R.id.login_sendcode, R.id.login_login_btn, R.id.login_finish, R.id.iv_clear, R.id.iv_login_wechat, R.id.iv_login_qq, R.id.tv_change_login_type, R.id.tv_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_sendcode:
                if (!isSendingCode) {
                    try {
                        JSONObject jsonObject = KeyUtil.getJson(this);
                        jsonObject.put("mobile", loginIphone.getText().toString());
                        if (NetWorkUtil.isNetworkConnected(this)) {
                            presenter.sendCode(RetrofitFactory.getRequestBody(jsonObject.toString()));
                        } else {
                            ToastUtils.showShort(this, "请检查网络设置");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.login_login_btn:
                if (cxAgreement.isChecked()) {
                    if (loginSendcode.getVisibility() == View.VISIBLE) {//验证码登录
                        UmengStatisticsUtil.statisticsEvent(this, "Login", "vcodeLogin", "验证码登录");
                        try {
                            JSONObject jsonObject = KeyUtil.getJson(this);
                            jsonObject.put("mobile", loginIphone.getText().toString());
                            jsonObject.put("code", loginCode.getText().toString());
                            if (NetWorkUtil.isNetworkConnected(this)) {
                                presenter.validationCode(RetrofitFactory.getRequestBody(jsonObject.toString()));
                            } else {
                                ToastUtils.showShort(this, "请检查网络设置");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        UmengStatisticsUtil.statisticsEvent(this, "Login", "pwdLogin", "密码登录");
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("mobile", loginIphone.getText().toString());
                        map.put("password", loginCode.getText().toString());
                        if (NetWorkUtil.isNetworkConnected(this)) {
                            presenter.loginWithPwd(RetrofitFactory.getRequestBody(new Gson().toJson(map)));
                        } else {
                            ToastUtils.showShort(this, "请检查网络设置");

                        }
                    }
                } else {
                    ToastUtils.showShort(this, "请勾选用户协议");
                }
                break;
            case R.id.login_finish:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.iv_clear:
                loginIphone.setText("");
                break;
            case R.id.tv_change_login_type:
                if (loginSendcode.getVisibility() == View.VISIBLE) {
                    loginSendcode.setVisibility(View.GONE);
                    loginCode.setText("");
                    loginCode.setHint("请输入密码");
                    tvChangeLoginType.setText("验证码登录");
                } else {
                    loginSendcode.setVisibility(View.VISIBLE);
                    loginCode.setText("");
                    loginCode.setHint("请输入验证码");
                    tvChangeLoginType.setText("密码登录");
                }
                break;
            case R.id.iv_login_wechat:
                type = 1;
                thirdLogin(SHARE_MEDIA.WEIXIN, 1);
                UmengStatisticsUtil.statisticsEvent(this, "Login", "weChatLogin", "微信登录");
                //ToastUtils.showShort(this, "敬请期待...");
                break;
            case R.id.iv_login_qq:
                type = 2;
                UmengStatisticsUtil.statisticsEvent(this, "Login", "QQLogin", "QQ登录");
                thirdLogin(SHARE_MEDIA.QQ, 2);
                break;
            case R.id.tv_agreement:
                startActivity(new Intent(this, AgreeementActivity.class));
                break;
        }
    }

    private void thirdLogin(SHARE_MEDIA share_media, final int type) {
        UMShareAPI api = UMShareAPI.get(this);
        api.getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                //      ToastUtils.showLong(LoginActivity.this, "开始");

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Log.e("LoginActivity", "昵称: " + map.get("uid"));
                nickName = map.get("name");
                openid = map.get("uid");
                province = map.get("uid");
                city = map.get("uid");
                gender = map.get("gender");
                header = map.get("iconurl");
                if (type == 1) {
                    orgin = "wechat";
                } else {
                    orgin = "qq";
                }
                try {
                    if (NetWorkUtil.isNetworkConnected(LoginActivity.this)) {
                        JSONObject jsonObject = KeyUtil.getJson(LoginActivity.this);
                        jsonObject.put("username", nickName);
                        jsonObject.put("openid", openid);
                        jsonObject.put("province", province);
                        jsonObject.put("city", city);
                        jsonObject.put("gender", gender);
                        jsonObject.put("header", header);
                        jsonObject.put("orgin", orgin);
                        presenter.thirdLogin(RetrofitFactory.getRequestBody(jsonObject.toString()));
                    } else {
                        ToastUtils.showShort(LoginActivity.this, "请检查网络设置");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                ToastUtils.showLong(LoginActivity.this, throwable.getMessage());

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                ToastUtils.showLong(LoginActivity.this, "取消登录");

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myCountDown.onFinish();
    }

    class SpinnerListener implements AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public class MyCountDown extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
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
}

