package com.pufei.gxdt.module.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pufei.gxdt.MainActivity;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.contents.EventMsg;
import com.pufei.gxdt.contents.MsgType;
import com.pufei.gxdt.module.login.activity.BindPhoneActivity;
import com.pufei.gxdt.module.login.activity.LoginActivity;
import com.pufei.gxdt.module.login.model.LoginResultBean;
import com.pufei.gxdt.module.user.bean.BindAccountBean;
import com.pufei.gxdt.module.user.bean.SetsBean;
import com.pufei.gxdt.module.user.bean.UserBean;
import com.pufei.gxdt.module.user.presenter.PublishPresenter;
import com.pufei.gxdt.module.user.presenter.SettingPresenter;
import com.pufei.gxdt.module.user.view.PublishView;
import com.pufei.gxdt.module.user.view.SettingView;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.DataCleanManager;
import com.pufei.gxdt.utils.DialogUtil;
import com.pufei.gxdt.utils.EvenMsg;
import com.pufei.gxdt.utils.FileUtil;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.utils.UserUtils;
import com.umeng.message.PushAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseMvpActivity<SettingPresenter> implements SettingView {
    @BindView(R.id.ll_title_left)
    LinearLayout ll_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.userdata_name)
    TextView phoneBind;
    @BindView(R.id.wechat_bind)
    TextView wechatBind;
    @BindView(R.id.qq_bind)
    TextView qqBind;
    @BindView(R.id.setting_log_out)
    Button settingLogOut;
    String mobile = "";
    String qq = "";
    String wechat = "";
    private String filepath;
    private PushAgent mPushAgent;
    private String nickName;
    private String openid;
    private String province;
    private String city;
    private String gender;
    private String header;
    private String orgin;
    private int type;


    @Override
    public void initView() {
        tv_title.setText("设置");
        ll_left.setVisibility(View.VISIBLE);
        filepath = FileUtil.getCachePath(getApplicationContext());
        if (App.userBean != null) {
            settingLogOut.setText(R.string.log_out);
        }
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();
    }

    @Override
    public void getData() {
        if (NetWorkUtil.isNetworkConnected(SettingActivity.this)) {
            requestSets();
        } else {
            ToastUtils.showShort(this, "请检查网络设置");
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_setting;
    }

    @OnClick({R.id.ll_title_left, R.id.setting_data_editor, R.id.setting_update_version, R.id.setting_about_product, R.id.setting_log_out, R.id.setting_clear_cache, R.id.change_notify, R.id.userdata_name_ll, R.id.userdata_name_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.setting_data_editor:
                if ((TextUtils.isEmpty(mobile))) {
                    startActivity(new Intent(SettingActivity.this, BindPhoneActivity.class));
                }
                break;
            case R.id.setting_update_version:
                DialogUtil.getInstance().showVersionDialog(this);
                break;
            case R.id.setting_about_product:
                startActivity(new Intent(SettingActivity.this, AboutProductActivity.class));
                break;
            case R.id.setting_log_out:
                if (App.userBean != null) {
                    DialogUtil.getInstance().canceDialog(this, mPushAgent);
                } else {
                    ToastUtils.showLong(this, "请先登录");
                }
                break;
            case R.id.setting_clear_cache:
                dialog();
                break;
            case R.id.change_notify:
                break;
            case R.id.userdata_name_ll:
                type = 1;
                thirdLogin(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.userdata_name_qq:
                type = 2;
                thirdLogin(SHARE_MEDIA.QQ);
                break;
            default:
                break;
        }
    }

    private void requestSets() {
        try {
            JSONObject jsonObject = KeyUtil.getJson(this);
            jsonObject.put("auth", SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH));
            presenter.getPublish(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void resultSets(SetsBean bean) {
        if (bean.getCode().equals(Contents.CODE_ZERO)) {
            mobile = bean.getResult().getMobile();
            qq = bean.getResult().getQq();
            wechat = bean.getResult().getWechat();
            if ((TextUtils.isEmpty(mobile))) {
                phoneBind.setText("未绑定");
            } else {
                phoneBind.setText("已绑定");
            }
            if ((TextUtils.isEmpty(qq))) {
                qqBind.setText("未绑定");
            } else {
                qqBind.setText(App.userBean.getQq());
            }
            if ((TextUtils.isEmpty(wechat))) {
                wechatBind.setText("未绑定");
            } else {
                wechatBind.setText(App.userBean.getWechat());
            }
        } else {
            ToastUtils.showShort(this, bean.getMsg() + " ");
        }
    }

    @Override
    public void sendRusult(BindAccountBean resultBean) {
        if (resultBean.getCode().equals(Contents.CODE_ZERO)) {
            if (type == 1) {
                App.userBean.setWechat(nickName);
                wechatBind.setText(nickName);
            } else {
                App.userBean.setQq(nickName);
                qqBind.setText(nickName);
            }
            SharedPreferencesUtil.getInstance().putString(Contents.USER_DETAIL, UserUtils.getUser(App.userBean));
            Toast.makeText(SettingActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//            LoginResultBean.ResultBean bean = resultBean.getResult();
//            String name = "";
//            String header = "";
//            String gender = "";
//            String address = "";
//            if (!TextUtils.isEmpty(bean.getUsername())) {
//                name = bean.getUsername();
//            } else {
//                name = "萌新上路";
//            }
//            if (!TextUtils.isEmpty(bean.getHeader())) {
//                header = bean.getHeader();
//            }
//            if (!TextUtils.isEmpty(bean.getGender())) {
//                gender = bean.getGender();
//            } else {
//                gender = "保密";
//            }
//            if (!TextUtils.isEmpty(bean.getCity())) {
//                address = bean.getCity();
//            } else {
//                address = "未知";
//            }
//            SharedPreferencesUtil.getInstance().putString(Contents.STRING_AUTH, bean.getAuth());
//            App.userBean = new UserBean(name, header, gender, address, bean.getAuth(), bean.getMobile(), bean.getUid());
//            if (!TextUtils.isEmpty(bean.getMobile())) {
//                Toast.makeText(SettingActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                EventBus.getDefault().post(new EvenMsg(MsgType.LOGIN_SUCCESS));
//                SharedPreferencesUtil.getInstance().putString(Contents.USER_DETAIL, UserUtils.getUser(App.userBean));
//                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
//                startActivity(intent);
//                AppManager.getAppManager().finishActivity();
//            } else {
//                Intent intent = new Intent(this, BindPhoneActivity.class);
//                intent.putExtra("openId", openid);
//                intent.putExtra("iconUrl", header);
//                intent.putExtra("nickName", nickName);
//                intent.putExtra("gender", gender);
//                intent.putExtra("type", type);
//                intent.putExtra("orgin", orgin);
//                startActivity(intent);
//            }

        } else {
            Toast.makeText(SettingActivity.this, resultBean.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setPresenter(SettingPresenter presenter) {
        if (presenter == null) {
            this.presenter = new SettingPresenter();
            this.presenter.attachView(this);
        }
    }

    private void thirdLogin(SHARE_MEDIA share_media) {
        UMShareAPI api = UMShareAPI.get(this);
        api.getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                //ToastUtils.showLong(SettingActivity.this, "开始");

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    Log.e("LoginActivity", "key:" + entry.getKey() + "; value:" + entry.getValue());
                }

                nickName = map.get("name");
                openid = map.get("uid");
                province = map.get("province");
                city = map.get("city");
                gender = map.get("gender");
                header = map.get("iconurl");
                if (type == 1) {
                    orgin = "wechat";
                } else {
                    orgin = "qq";
                }
                try {
                    if (NetWorkUtil.isNetworkConnected(SettingActivity.this)) {
                        JSONObject jsonObject = KeyUtil.getJson(SettingActivity.this);
                        jsonObject.put("username", nickName);
                        jsonObject.put("openid", openid);
                        jsonObject.put("province", province);
                        jsonObject.put("city", city);
                        jsonObject.put("gender", gender);
                        jsonObject.put("header", header);
                        jsonObject.put("orgin", orgin);
                        presenter.bindAccount(RetrofitFactory.getRequestBody(jsonObject.toString()));
                    } else {
                        ToastUtils.showShort(SettingActivity.this, "请检查网络设置");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                ToastUtils.showLong(SettingActivity.this, throwable.getMessage());

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                ToastUtils.showLong(SettingActivity.this, "取消登录");

            }
        });
    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setMessage("确定清除缓存数据？");
        builder.setPositiveButton(getString(R.string.determine), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataCleanManager.cleanCustomCache(filepath);
                ToastUtils.showShort(SettingActivity.this, "清理成功");
                //activitySettingCleartext.setText("0M");
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.create().show();
    }
}
