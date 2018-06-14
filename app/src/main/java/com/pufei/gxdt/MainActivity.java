package com.pufei.gxdt;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.base.TabVpAdapter;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.contents.EventMsg;
import com.pufei.gxdt.contents.MsgType;
import com.pufei.gxdt.module.discover.fragment.DiscoverFragment;
import com.pufei.gxdt.module.home.fragment.HomeFragment;
import com.pufei.gxdt.module.maker.activity.EditImageActivity;
import com.pufei.gxdt.module.maker.fragment.MakerFragment;
import com.pufei.gxdt.module.user.bean.UserBean;
import com.pufei.gxdt.module.user.fragment.UserFragment;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.StartUtils;
import com.pufei.gxdt.utils.ToastUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.entity.Alias;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;

public class MainActivity extends BaseActivity {

    @BindView(R.id.home_vp)
    ViewPager homeVp;
    @BindView(R.id.home_tab)
    TabLayout homeTab;
    private List<Fragment> fragmentList;
    private TabVpAdapter tabAdapter;
    private long mExitTime;
    private int previousItem;
    private PushAgent mPushAgent;


    @Override
    public void initView() {

        String user_detail = SharedPreferencesUtil.getInstance().getString(Contents.USER_DETAIL, null);
        if (user_detail != null) {
            App.userBean = new Gson().fromJson(user_detail, UserBean.class);
//            Log.e("fdsaf",App.userBean.getUid() +" ");
            initUPush(App.userBean.getUid());
        }
        addFragment();
        tabAdapter = new TabVpAdapter(this, getSupportFragmentManager(), fragmentList);
        homeVp.setAdapter(tabAdapter);
        homeVp.setOffscreenPageLimit(4);
        homeTab.setupWithViewPager(homeVp);
        for (int i = 0; i < fragmentList.size(); i++) {
            TabLayout.Tab tab = homeTab.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(tabAdapter.getCustomView(i));
                if (i == 0) {
                    ((ImageView) tab.getCustomView().findViewById(R.id.tab_iv)).setSelected(true);
                    ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setSelected(true);
                }
            }
        }

        homeTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_iv)).setSelected(true);
                ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setSelected(true);
                homeVp.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 2) {
                    Intent intent = new Intent(MainActivity.this, EditImageActivity.class);
                    startActivity(intent);
                } else {
                    previousItem = tab.getPosition();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((ImageView) tab.getCustomView().findViewById(R.id.tab_iv)).setSelected(false);
                ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void initUPush(String alias) {
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();
        mPushAgent.setAlias(alias, "User", new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String message) {
                Log.e("push", isSuccess + " " + message);
            }
        });
    }

    @Override
    public void getData() {
        Intent intent = getIntent();
        int index = intent.getIntExtra("make_finish",0);
        if(index == MsgType.MAKER_FINISH) {
            homeVp.setCurrentItem(1);
        }else {
            homeVp.setCurrentItem(0);

        }
        Acp.getInstance(this)
                .request(new AcpOptions.Builder().setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                StartUtils.getInstance(MainActivity.this).detection();
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                ToastUtils.showShort(MainActivity.this, "请求权限失败,请手动开启！");
                            }
                        });
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventMsg type) {
        if (type.getTYPE() == MsgType.MAKER_IMAGE) {
            homeVp.setCurrentItem(previousItem);
        }else if (type.getTYPE() == MsgType.LOGIN_SUCCESS) {
            initUPush(App.userBean.getUid());
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    private void addFragment() {
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new DiscoverFragment());
        fragmentList.add(new MakerFragment());
        fragmentList.add(new UserFragment());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().finishAllActivity();
                AppManager.getAppManager().AppExit(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
