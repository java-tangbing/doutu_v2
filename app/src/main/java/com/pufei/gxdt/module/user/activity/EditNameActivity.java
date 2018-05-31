package com.pufei.gxdt.module.user.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EditNameActivity extends BaseMvpActivity<SetPersonalPresenter> implements SetPersonalView, TextWatcher {
    @BindView(R.id.et_nick_name)
    EditText etNickName;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;

    private String result;

    @Override
    public void initView() {
        llTitleLeft.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        tvTitle.setText("修改昵称");
        tvRight.setText("保存");
        etNickName.addTextChangedListener(this);
    }

    @Override
    public void getData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_editname;
    }


    @OnClick({R.id.ll_title_left, R.id.tv_right, R.id.iv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.tv_right:
                result = etNickName.getText().toString();
                if (!TextUtils.isEmpty(result)) {
                    try {
                        if (NetWorkUtil.isNetworkConnected(this)) {
                            JSONObject jsonObject = KeyUtil.getJson(this);
                            jsonObject.put("auth", App.userBean.getAuth());
                            jsonObject.put("header", "");
                            jsonObject.put("username", result);
                            jsonObject.put("gender", "");
                            jsonObject.put("mind", "");
                            presenter.setPersonal(RetrofitFactory.getRequestBody(jsonObject.toString()));
                        } else {
                            ToastUtils.showShort(this, "请检查网络设置");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showShort(EditNameActivity.this, "名字不能为空");
                }
                break;
            case R.id.iv_delete:
                etNickName.setText("");
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable edit) {
        if (TextUtils.isEmpty(edit.toString())) {
            ivDelete.setVisibility(View.GONE);
        } else {
            ivDelete.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void setPersonalInfo(SetPersonalResultBean bean) {
        if (bean.getCode() == 0) {
            App.userBean.setName(result);
            SharedPreferencesUtil.getInstance().putString(Contents.USER_DETAIL, UserUtils.getUser(App.userBean));
            EventBus.getDefault().postSticky(new EventMsg(MsgType.UPDATA_USER));
            AppManager.getAppManager().finishActivity();
        } else {
            ToastUtils.showShort(this, bean.getMsg() + " ");
        }
    }

    @Override
    public void setPersonAvatar(SetAvatarResultBean bean) {

    }

    @Override
    public void setPresenter(SetPersonalPresenter presenter) {
        if (presenter == null) {
            this.presenter = new SetPersonalPresenter();
            this.presenter.attachView(this);
        }
    }

}
