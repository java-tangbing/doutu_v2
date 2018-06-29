package com.pufei.gxdt.module.user.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.contents.EventMsg;
import com.pufei.gxdt.contents.MsgType;
import com.pufei.gxdt.module.user.bean.SetAvatarResultBean;
import com.pufei.gxdt.module.user.bean.SetPersonalResultBean;
import com.pufei.gxdt.module.user.bean.UserBean;
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

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ProfileActivity extends BaseMvpActivity<SetPersonalPresenter> implements SetPersonalView, View.OnClickListener {
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
    private String sex = "";
    private static final int REQUEST_CODE = 17;
    private CommonPopupWindow popupWindow;
    private ProgressDialog mProgressDialog;

    @Override
    public void initView() {
        llTitleLeft.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        tvTitle.setText("资料编辑");
        tvRight.setText("保存");
        if (App.userBean != null) {
            if (!App.userBean.getHead().isEmpty()) {
                Glide.with(this).load(App.userBean.getHead()).into(userdataHead);
            } else {
                Glide.with(this).load(R.mipmap.my_uer_picture).into(userdataHead);
            }
            userdataname.setText(App.userBean.getName());
            tvSex.setText(App.userBean.getGender());
            userdata_dec.setText(App.userBean.getMind());
            loginstate.setText(R.string.log_out);
        }
    }

    @Override
    public void getData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_profile;
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
        userdata_dec.setText(App.userBean.getMind());
        tvSex.setText(App.userBean.getGender());
        Glide.with(this).load(App.userBean.getHead()).into(userdataHead);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventData(EventMsg type) {
        if (type.getTYPE() == MsgType.LOGIN_SUCCESS) {
            initListener();
        } else if (type.getTYPE() == MsgType.LOGIN_SUCCESS) {
            initListener();
        } else if (type.getTYPE() == MsgType.UPDATA_USER) {
            initListener();
        }
    }

    @OnClick({R.id.ll_title_left, R.id.tv_right, R.id.userdata_head_ll, R.id.userdata_name_ll, R.id.userdata_gender_ll, R.id.userdata_safe_ll, R.id.login_state, R.id.userdata_declaration_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.tv_right:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.userdata_head_ll:
                //ImageSelectorUtils.openPhoto(ProfileActivity.this, REQUEST_CODE, true, 0);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, ""), REQUEST_CODE);
                break;
            case R.id.userdata_name_ll:
                startActivity(new Intent(this, EditNameActivity.class));
                break;
            case R.id.userdata_declaration_ll:
                startActivity(new Intent(this, EditDeclarationActivity.class));
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
                                view.findViewById(R.id.tv_sex_man).setOnClickListener(ProfileActivity.this);
                                view.findViewById(R.id.tv_sex_woman).setOnClickListener(ProfileActivity.this);
                                view.findViewById(R.id.tv_sex_safety).setOnClickListener(ProfileActivity.this);
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
                    DialogUtil.getInstance().canceDialog(this, null);
                } else {
                    ToastUtils.showLong(this, "请先登录");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            String path1 = "";
            Uri uri = data.getData();
            if (uri.toString().contains("provider")) {
                path1 = ImageUtils.getFilePathByUri(this, uri);
            } else {
                path1 = getRealFilePath(this, uri);
            }
            if (path1 != null) {
                Luban.with(this)
                        .load(new File(path1))
                        .ignoreBy(100)
                        .setTargetDir(App.path1 + "/")
                        .filter(new CompressionPredicate() {
                            @Override
                            public boolean apply(String path) {
                                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                            }
                        })
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onSuccess(File file) {
                                //GlideApp.with(ProfileActivity.this).asBitmap().load(file).into(userdataHead);
                                String base64 = ImageUtils.bitmapToBase64(BitmapFactory.decodeFile(file.getPath()));
                                requestSetAvatar(base64);
                                UserBean userBean = App.userBean;
                                userBean.setHead(file.getPath());
                                SharedPreferencesUtil.getInstance().putString(Contents.USER_DETAIL, UserUtils.getUser(App.userBean));
                            }

                            @Override
                            public void onError(Throwable e) {
                            }
                        }).launch();


//                String base64 = ImageUtils.bitmapToBase64(BitmapFactory.decodeFile(path1));
//                requestSetAvatar(base64);
//                App.userBean.setHead(base64);
//                SharedPreferencesUtil.getInstance().putString(Contents.USER_DETAIL, UserUtils.getUser(App.userBean));
            }
            Log.e("album path", path1 + " " + uri);

//            ArrayList<String> images = data.getStringArrayListExtra(
//                    ImageSelectorUtils.SELECT_RESULT);
//            String base64 = ImageUtils.bitmapToBase64(BitmapFactory.decodeFile(images.get(0)));
//            requestSetAvatar(base64);
//            App.userBean.setHead(images.get(0));
//            SharedPreferencesUtil.getInstance().putString(Contents.USER_DETAIL, UserUtils.getUser(App.userBean));
        }
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private void requestSetAvatar(String value) {
        try {
            if (NetWorkUtil.isNetworkConnected(this)) {
                showLoading("上传中...");
                org.json.JSONObject jsonObject = com.pufei.gxdt.utils.KeyUtil.getJson(this);
                jsonObject.put("auth", App.userBean.getAuth());
                jsonObject.put("header", value);
                jsonObject.put("username", "");
                jsonObject.put("gender", "");
                jsonObject.put("mind", "");
                presenter.setPersonal(com.pufei.gxdt.utils.RetrofitFactory.getRequestBody(jsonObject.toString()));
            } else {
                ToastUtils.showShort(this, "请检查网络设置");
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sex_man:
                sex = "男";
                break;
            case R.id.tv_sex_woman:
                sex = "女";
                break;
            case R.id.tv_sex_safety:
                sex = "保密";
                break;
        }

        requestSetSex(sex);
        popupWindow.dismiss();
    }

    private void requestSetSex(String sex) {
        try {
            if (NetWorkUtil.isNetworkConnected(this)) {
                org.json.JSONObject jsonObject = com.pufei.gxdt.utils.KeyUtil.getJson(this);
                jsonObject.put("auth", App.userBean.getAuth());
                jsonObject.put("header", "");
                jsonObject.put("username", "");
                jsonObject.put("gender", sex);
                jsonObject.put("mind", "");
                presenter.setPersonal(com.pufei.gxdt.utils.RetrofitFactory.getRequestBody(jsonObject.toString()));
            } else {
                ToastUtils.showShort(this, "请检查网络设置");
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
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
        hideLoading();
        if (bean.getCode() == 0) {
            if (!TextUtils.isEmpty(sex)) {
                App.userBean.setGender(sex);
            }
            SharedPreferencesUtil.getInstance().putString(Contents.USER_DETAIL, UserUtils.getUser(App.userBean));
            EventBus.getDefault().postSticky(new EventMsg(MsgType.UPDATA_USER));
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

    @Override
    public void requestErrResult(String msg) {
        ToastUtils.showShort(this, msg);
    }


    void showLoading(@NonNull String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
