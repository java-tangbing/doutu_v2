package com.pufei.gxdt.module.user.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseFragment;
import com.pufei.gxdt.contents.EventMsg;
import com.pufei.gxdt.contents.MsgType;
import com.pufei.gxdt.module.user.activity.AboutProductActivity;
import com.pufei.gxdt.module.user.activity.SettingActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends BaseFragment {

    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.iv_user_head)
    CircleImageView ivUserHead;

    @Override
    public void initView() {
        initUserInfo();
    }

    @Override
    public void getData() {

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

    private void initUserInfo() {
        if (App.userBean != null) {
            tvUserName.setText(App.userBean.getName());
            if (!App.userBean.getHead().isEmpty()) {
                Glide.with(this).load(App.userBean.getHead()).into(ivUserHead);
            } else {
                Glide.with(this).load(R.mipmap.my_uer_picture).into(ivUserHead);
            }
        } else {
            tvUserName.setText("未登录");
            ivUserHead.setImageResource(R.mipmap.my_uer_picture);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_user;
    }


    @OnClick({R.id.btn_checkin, R.id.btn_setting, R.id.fm_head, R.id.tv_user_favorite, R.id.tv_about_product, R.id.tv_user_task})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_checkin:
                break;
            case R.id.btn_setting:
                if (App.userBean == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                }
                break;
            case R.id.fm_head:
                if (App.userBean == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                }
                break;
            case R.id.tv_user_favorite:
                startActivity(new Intent(getActivity(), FavoriteTaskActivity.class));
                break;
            case R.id.tv_about_product:
                startActivity(new Intent(getActivity(), AboutProductActivity.class));
                break;
            case R.id.tv_user_task:
                startActivity(new Intent(getActivity(), UserTaskActivity.class));
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventData(EventMsg type) {
        //   Log.e(TAG, "eventData");
        if (type.getTYPE() == MsgType.LOGIN_SUCCESS) {
            initUserInfo();
        } else if (type.getTYPE() == MsgType.LOGIN_OUT) {
            initUserInfo();
        } else if (type.getTYPE() == MsgType.UPDATA_USER) {
            initUserInfo();
            EventBus.getDefault().removeStickyEvent(type);//移除sticky事件
        }
    }
}
