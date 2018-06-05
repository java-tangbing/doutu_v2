package com.pufei.gxdt.module.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.contents.EventMsg;
import com.pufei.gxdt.contents.MsgType;
import com.pufei.gxdt.module.login.activity.BindPhoneActivity;
import com.pufei.gxdt.module.login.activity.LoginActivity;
import com.pufei.gxdt.module.user.bean.SetsBean;
import com.pufei.gxdt.module.user.presenter.PublishPresenter;
import com.pufei.gxdt.module.user.presenter.SettingPresenter;
import com.pufei.gxdt.module.user.view.PublishView;
import com.pufei.gxdt.module.user.view.SettingView;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.DataCleanManager;
import com.pufei.gxdt.utils.DialogUtil;
import com.pufei.gxdt.utils.FileUtil;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseMvpActivity<SettingPresenter> implements SettingView {
    @BindView(R.id.ll_title_left)
    LinearLayout ll_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.setting_data_editor)
    LinearLayout settingDataEditor;
    @BindView(R.id.setting_version_checking)
    LinearLayout settingVersionChecking;
    @BindView(R.id.setting_about_product)
    TextView settingAboutProduct;
    @BindView(R.id.userdata_name)
    TextView phoneBind;
    @BindView(R.id.wechat_bind)
    TextView wechatBind;
    @BindView(R.id.change_notify)
    ImageView changeNotify;
    @BindView(R.id.setting_log_out)
    Button settingLogOut;
    String mobile = "";
    String qq = "";
    String wechat = "";
    private String filepath;

    @Override
    public void initView() {
        tv_title.setText("设置");
        ll_left.setVisibility(View.VISIBLE);
        filepath = FileUtil.getCachePath(getApplicationContext());
        if (App.userBean != null) {
            settingLogOut.setText(R.string.log_out);
        }
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

    @OnClick({R.id.ll_title_left, R.id.setting_data_editor, R.id.setting_update_version, R.id.setting_about_product, R.id.setting_log_out, R.id.setting_clear_cache, R.id.change_notify, R.id.userdata_name_ll})
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
                    DialogUtil.getInstance().canceDialog(this);
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

    @OnClick(R.id.ll_title_left)
    public void backLastActivity() {
        AppManager.getAppManager().finishActivity();
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
//            if ((TextUtils.isEmpty(qq))) {
//                phoneBind.setText("未绑定");
//            } else {
//                phoneBind.setText("已绑定");
//            }
            if ((TextUtils.isEmpty(wechat))) {
                wechatBind.setText("未绑定");
            } else {
                wechatBind.setText("已绑定");
            }
        } else {
            ToastUtils.showShort(this, bean.getMsg() + " ");
        }
    }

    @Override
    public void setPresenter(SettingPresenter presenter) {
        if (presenter == null) {
            this.presenter = new SettingPresenter();
            this.presenter.attachView(this);
        }
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
