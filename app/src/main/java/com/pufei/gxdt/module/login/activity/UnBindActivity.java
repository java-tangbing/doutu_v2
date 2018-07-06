package com.pufei.gxdt.module.login.activity;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.contents.EventMsg;
import com.pufei.gxdt.contents.MsgType;
import com.pufei.gxdt.module.login.presenter.UnBindPresenter;
import com.pufei.gxdt.module.login.view.UnBindView;
import com.pufei.gxdt.module.user.bean.BindAccountBean;
import com.pufei.gxdt.module.user.bean.UserBean;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class UnBindActivity extends BaseMvpActivity<UnBindPresenter> implements UnBindView {
    @BindView(R.id.setting_log_out)
    Button unBindBtn;
    @BindView(R.id.unbind_key)
    TextView unBindKey;
    @BindView(R.id.unbind_value)
    TextView unBindValue;

    private String orgin = "";

    @Override
    public void initView() {
    }

    @Override
    public void getData() {
        orgin = getIntent().getStringExtra("orgin");
        if ("mobile".equals(orgin)) {
            unBindKey.setText("手机号码");
            unBindBtn.setText("解绑手机号");
            unBindValue.setText(App.userBean.getPhone());
        } else if ("qq".equals(orgin)) {
            unBindKey.setText("QQ");
            unBindBtn.setText("解绑QQ");
            unBindValue.setText(App.userBean.getQq());
        } else {
            unBindKey.setText("微信");
            unBindBtn.setText("解绑微信");
            unBindValue.setText(App.userBean.getWechat());
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_unbind_phone;
    }

//    @Override
//    public void bindResult(SendCodeBean sendCodeBean) {
//        if (sendCodeBean.getCode().equals(Contents.CODE_ZERO)) {
//            UserBean bean = App.userBean;
//            bean.setPhone(loginIphone.getText().toString());
//            Toast.makeText(UnBindActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
//            UmengStatisticsUtil.statisticsEvent(this, "36");
//            if (AppManager.getAppManager().activityStackCount() == 1 || AppManager.getAppManager().activityStackCount() == 2) {
//                Intent intent = new Intent(UnBindActivity.this, MainActivity.class);
//                startActivity(intent);
//            } else {
//                String user_detail = SharedPreferencesUtil.getInstance().getString(Contents.USER_DETAIL, null);
//                if (user_detail != null) {
//                    EventBus.getDefault().postSticky(new EventMsg(MsgType.LOGIN_SUCCESS));
//                    App.userBean = new Gson().fromJson(user_detail, UserBean.class);
//                }
//            }
//            AppManager.getAppManager().finishActivity();
//        } else {
//            ToastUtils.showShort(this, sendCodeBean.getMsg());
//        }
//    }

    @Override
    public void unBindResult(BindAccountBean sendCodeBean) {
        if (sendCodeBean.getCode().equals(Contents.CODE_ZERO)) {
            UserBean bean = App.userBean;
            if ("mobile".equals(orgin)) {
                bean.setPhone(null);
            } else if ("qq".equals(orgin)) {
                bean.setQq(null);
            } else {
                bean.setWechat(null);
            }
            ToastUtils.showLong(UnBindActivity.this, "解绑成功");
            EventBus.getDefault().postSticky(new EventMsg(MsgType.UNBIND_NEW));
        }
        AppManager.getAppManager().finishActivity();
    }

    @Override
    public void requestErrResult(String msg) {
        ToastUtils.showShort(this, msg);
    }


    @OnClick({R.id.ll_title_left, R.id.setting_log_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.setting_log_out:
                try {
                    JSONObject jsonObject = KeyUtil.getJson(this);
                    jsonObject.put("orgin", orgin);
                    if (NetWorkUtil.isNetworkConnected(this)) {
                        presenter.unbindAccount(RetrofitFactory.getRequestBody(jsonObject.toString()));
                    } else {
                        ToastUtils.showShort(this, "请检查网络设置");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(UnBindPresenter presenter) {
        if (presenter == null) {
            this.presenter = new UnBindPresenter();
            this.presenter.attachView(this);
        }
    }
}
