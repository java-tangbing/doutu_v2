package com.pufei.gxdt.module.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.contents.EventMsg;
import com.pufei.gxdt.contents.MsgType;
import com.pufei.gxdt.module.login.activity.BindPhoneActivity;
import com.pufei.gxdt.module.login.activity.BindPhoneNewActivity;
import com.pufei.gxdt.module.login.activity.UnBindActivity;
import com.pufei.gxdt.module.update.model.UpdateBean;
import com.pufei.gxdt.module.user.bean.BindAccountBean;
import com.pufei.gxdt.module.user.bean.SetsBean;
import com.pufei.gxdt.module.user.presenter.SettingPresenter;
import com.pufei.gxdt.module.user.view.SettingView;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.DataCleanManager;
import com.pufei.gxdt.utils.DialogUtil;
import com.pufei.gxdt.utils.FileUtil;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.OkhttpUtils;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.StartUtils;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.utils.UmengStatisticsUtil;
import com.pufei.gxdt.utils.UserUtils;
import com.suke.widget.SwitchButton;
import com.umeng.message.IUmengCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SettingActivity extends BaseMvpActivity<SettingPresenter> implements SettingView {
    @BindView(R.id.ll_title_left)
    LinearLayout ll_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.userdata_name)
    TextView phoneBind;
    @BindView(R.id.wechat_bind)
    TextView wechatBind;
    @BindView(R.id.change_notify)
    SwitchButton switchButton;
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
    private String newVersion = "";
    private String oldVersion = "";
    private String des;
    private int newcode;
    private int oldcode;


    @Override
    public void initView() {
        AppManager.getAppManager().addActivity(this);
        tv_title.setText("设置");
        ll_left.setVisibility(View.VISIBLE);
        filepath = FileUtil.getCachePath(getApplicationContext());
        if (App.userBean != null) {
            settingLogOut.setText(R.string.log_out);
        }
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (App.userBean != null) {
                    if (isChecked) {
                        mPushAgent.enable(new IUmengCallback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onFailure(String s, String s1) {
                            }
                        });
                    } else {
                        mPushAgent.disable(new IUmengCallback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onFailure(String s, String s1) {
                            }
                        });
                    }
                }

            }
        });
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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventMsg type) {
        if (type.getTYPE() == MsgType.UNBIND_NEW || type.getTYPE() == MsgType.BIND_NEW) {
            mobile = App.userBean.getPhone();
            qq = App.userBean.getQq();
            wechat = App.userBean.getWechat();
            if (TextUtils.isEmpty(mobile)) {
                phoneBind.setText("未绑定");
            } else {
                phoneBind.setText("已绑定");
            }
            if (TextUtils.isEmpty(qq)) {
                qqBind.setText("未绑定");
            } else {
                qqBind.setText(qq);
            }
            if (TextUtils.isEmpty(wechat)) {
                wechatBind.setText("未绑定");
            } else {
                wechatBind.setText(wechat);
            }
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
                UmengStatisticsUtil.statisticsEvent(SettingActivity.this, "35");
                if (TextUtils.isEmpty(mobile)) {
                    if (App.userBean.getState().equals(Contents.CODE_ZERO)) {
                        startActivity(new Intent(SettingActivity.this, BindPhoneActivity.class));
                    } else {
                        startActivity(new Intent(SettingActivity.this, BindPhoneNewActivity.class));
                    }
                } else {
                    startActivity(getSetIntent("mobile"));
                }
                break;
            case R.id.setting_update_version:

                if (!NetWorkUtil.isNetworkConnected(App.AppContext)) {
                    Toast.makeText(App.AppContext, "当前网络不可用，请检查网络情况", Toast.LENGTH_LONG).show();
                    return;
                }
                JSONObject jsonObject = KeyUtil.getJson(this);
                try {
                    OkhttpUtils.post(Contents.Update, jsonObject.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e(StartUtils.class.getSimpleName(), e.toString());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            UpdateBean updateBean = null;
                            try {
                                updateBean = new Gson().fromJson(result, UpdateBean.class);
                                newVersion = updateBean.getResult().getVersion();
                                final String upURl = updateBean.getResult().getLink();
                                final boolean update = updateBean.getResult().getUpdateOpen().equals("1");
                                //final boolean force = updateBean.getResult().isForce();
                                des = updateBean.getResult().getDes();
                                //newVersion="1.0";
                                newcode = Integer.parseInt(updateBean.getResult().getVersion_code());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        PackageManager pm = getApplicationContext().getPackageManager();
                                        PackageInfo pi = null;
                                        try {
                                            pi = pm.getPackageInfo(getApplicationContext().getPackageName(), 0);
                                        } catch (PackageManager.NameNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                        if (pi != null) {
                                            oldVersion = pi.versionName;
                                            oldcode = pi.versionCode;
                                        }
                                        if (newcode > oldcode) {
                                            DialogUtil.getInstance().showVersionDialog(SettingActivity.this, upURl, newVersion);
                                        } else {
                                            DialogUtil.getInstance().showVersionDialog(SettingActivity.this, "", "");
                                        }
                                    }
                                });
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
            case R.id.userdata_name_ll:
                type = 1;
                if (TextUtils.isEmpty(wechat)) {
                    bindAccount(SHARE_MEDIA.WEIXIN);
                } else {
                    startActivity(getSetIntent("wechat"));
                }
                break;
            case R.id.userdata_name_qq:
                type = 2;
                if (TextUtils.isEmpty(qq)) {
                    bindAccount(SHARE_MEDIA.QQ);
                } else {
                    startActivity(getSetIntent("qq"));
                }
                break;
            default:
                break;
        }
    }

    private void requestSets() {
        JSONObject jsonObject = KeyUtil.getJson(this);
        presenter.getPublish(RetrofitFactory.getRequestBody(jsonObject.toString()));
    }

    @Override
    public void resultSets(SetsBean bean) {
        if (bean.getCode().equals(Contents.CODE_ZERO)) {
            mobile = bean.getResult().getMobile();
            qq = bean.getResult().getQq();
            wechat = bean.getResult().getWechat();
            if (TextUtils.isEmpty(mobile)) {
                phoneBind.setText("未绑定");
            } else {
                phoneBind.setText("已绑定");
            }
            if (TextUtils.isEmpty(qq)) {
                qqBind.setText("未绑定");
            } else {
                qqBind.setText(qq);
            }
            if (TextUtils.isEmpty(wechat)) {
                wechatBind.setText("未绑定");
            } else {
                wechatBind.setText(wechat);
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
            Toast.makeText(SettingActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SettingActivity.this, resultBean.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void requestErrResult(String msg) {
        ToastUtils.showShort(this, msg);
    }

    @Override
    public void setPresenter(SettingPresenter presenter) {
        if (presenter == null) {
            this.presenter = new SettingPresenter();
            this.presenter.attachView(this);
        }
    }

    private void bindAccount(SHARE_MEDIA share_media) {
        UMShareAPI api = UMShareAPI.get(this);
        api.getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                ToastUtils.showLong(SettingActivity.this, "开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                openid = map.get("uid");
                nickName = map.get("name");
                province = map.get("province");
                //city = map.get("city");
                //gender = map.get("gender");
                //header = map.get("iconurl");
                if (type == 1) {
                    orgin = "wechat";
                } else {
                    orgin = "qq";
                }
                try {
                    if (NetWorkUtil.isNetworkConnected(SettingActivity.this)) {
                        JSONObject jsonObject = KeyUtil.getJson(SettingActivity.this);
                        jsonObject.put("openid", openid);
                        jsonObject.put("orgin", orgin);
                        jsonObject.put("nickname", nickName);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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

    private Intent getSetIntent(String orgin) {
        Intent intent = new Intent(SettingActivity.this, UnBindActivity.class);
        intent.putExtra("orgin", orgin);
        return intent;
    }
}
