package com.pufei.gxdt.module.user.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.google.gson.Gson;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.contents.EventMsg;
import com.pufei.gxdt.contents.MsgType;
import com.pufei.gxdt.module.user.bean.SetAvatarResultBean;
import com.pufei.gxdt.module.user.bean.SetPersonalResultBean;
import com.pufei.gxdt.module.user.presenter.SetPersonalPresenter;
import com.pufei.gxdt.module.user.view.SetPersonalView;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.DialogUtil;
import com.pufei.gxdt.utils.ImageUtils;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.utils.UserUtils;
import com.pufei.gxdt.widgets.popupwindow.CommonPopupWindow;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SettingActivity extends BaseMvpActivity<SetPersonalPresenter> implements SetPersonalView, View.OnClickListener {

    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.userdata_name)
    TextView userdataname;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.login_state)
    TextView loginstate;

    @BindView(R.id.userdata_head)
    CircleImageView userdataHead;
    @BindView(R.id.userdata_dec)
    TextView userdata_dec;
    private String sex;
    private static final int REQUEST_CODE = 17;
    private CommonPopupWindow popupWindow;

    @Override
    public void initView() {
        llTitleLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("资料设置");
        tvRight.setText("保存");
        if (App.userBean != null) {

            if (!App.userBean.getHead().isEmpty()) {
                Glide.with(this).load(App.userBean.getHead()).into(userdataHead);
            } else {
                Glide.with(this).load(R.mipmap.my_uer_picture).into(userdataHead);
            }
            userdataname.setText(App.userBean.getName());
            tvSex.setText(App.userBean.getGender());
            loginstate.setText(R.string.log_out);
        }

    }

    @Override
    public void getData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

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

    public void initListener() {
        userdataname.setText(App.userBean.getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventData(EventMsg type) {
        //  Log.e(TAG, "eventData");
        if (type.getTYPE() == MsgType.LOGIN_SUCCESS) {
            initListener();
        } else if (type.getTYPE() == MsgType.LOGIN_SUCCESS) {
            initListener();
        } else if (type.getTYPE() == MsgType.UPDATA_USER) {
            initListener();
        }
    }

    @OnClick({R.id.ll_title_left, R.id.userdata_head_ll, R.id.userdata_name_ll, R.id.userdata_gender_ll, R.id.userdata_safe_ll, R.id.login_state, R.id.userdata_alipay_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.userdata_head_ll:
                ImageSelectorUtils.openPhoto(SettingActivity.this, REQUEST_CODE, true, 0);
                break;
            case R.id.userdata_name_ll:
                startActivity(new Intent(this, EditNameActivity.class));
                break;
            case R.id.userdata_gender_ll:
                popupWindow = new CommonPopupWindow.Builder(this)
                        .setView(R.layout.activity_user_pop)
                        .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setBackGroundLevel(0.5f)
                        .setAnimationStyle(R.style.anim_menu_pop)
                        .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                            @Override
                            public void getChildView(final View view, int layoutResId) {
                                view.findViewById(R.id.tv_sex_man).setOnClickListener(SettingActivity.this);
                                view.findViewById(R.id.tv_sex_woman).setOnClickListener(SettingActivity.this);
                                view.findViewById(R.id.tv_sex_safety).setOnClickListener(SettingActivity.this);
                            }
                        })
                        .setOutsideTouchable(true)
                        .create();
                popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.userdata_safe_ll:
                break;
            case R.id.login_state:
                if (App.userBean != null) {
                    DialogUtil.getInstance().canceDialog(this);
                } else {
                    ToastUtils.showLong(this, "请先登录");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(
                    ImageSelectorUtils.SELECT_RESULT);
            String base64 = ImageUtils.bitmapToBase64(BitmapFactory.decodeFile(images.get(0)));
            requestSetAvatar("pic", base64);
            App.userBean.setHead(images.get(0));
        }
    }

    private void requestSetAvatar(String type, String value) {
        Map<String, String> map = new HashMap<>();
        map.put("auth", App.userBean.getAuth());
        map.put(type, value);
        String requestString = new Gson().toJson(map);
        if (NetWorkUtil.isNetworkConnected(this)) {
            RequestBody requestBody =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                            requestString);
            presenter.setPersonalAvatar(requestBody);
        } else {
            ToastUtils.showShort(this, "请检查网络设置");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sex_man:
                sex = "男";
                requestSetSex("1");
                popupWindow.dismiss();
                break;
            case R.id.tv_sex_woman:
                sex = "女";
                requestSetSex("2");
                popupWindow.dismiss();
                break;
            case R.id.tv_sex_safety:
                sex = "保密";
                requestSetSex("0");
                popupWindow.dismiss();
                break;
        }
    }

    private void requestSetSex(String sex) {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("auth", App.userBean.getAuth());
        Map<String, String> map2 = new HashMap<>();
        map2.put("sex", sex);
        map1.put("data", map2);
        String resutString = new Gson().toJson(map1);
        if (NetWorkUtil.isNetworkConnected(this)) {
            RequestBody requestBody =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                            resutString);
            presenter.setPersonal(requestBody);
        } else {
            ToastUtils.showShort(this, "请检查网络设置");
        }

    }

    @Override
    public void setPresenter(SetPersonalPresenter presenter) {
        if (presenter == null) {
            this.presenter = new SetPersonalPresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void setPersonalInfo(SetPersonalResultBean bean) {
        if (bean.getCode() == 0) {
            tvSex.setText(sex);
            App.userBean.setGender(sex);
        }
        ToastUtils.showShort(this, bean.getMsg());
    }

    @Override
    public void setPersonAvatar(SetAvatarResultBean bean) {
        if (bean.getCode() == 0) {
            SharedPreferencesUtil.getInstance().putString(Contents.USER_DETAIL, UserUtils.getUser(App.userBean));
            if (!bean.getData().getAvatar().isEmpty()) {
                Glide.with(this).load(bean.getData().getAvatar()).into(userdataHead);
            } else {
                Glide.with(this).load(R.mipmap.my_uer_picture).into(userdataHead);
            }
            EventBus.getDefault().postSticky(new EventMsg(MsgType.UPDATA_USER));
        } else {
            ToastUtils.showShort(this, bean.getMsg());
        }
    }

}
