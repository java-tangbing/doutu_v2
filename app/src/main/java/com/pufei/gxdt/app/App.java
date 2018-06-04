package com.pufei.gxdt.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.pufei.gxdt.module.user.bean.UserBean;
import com.pufei.gxdt.utils.LogUtils;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;


public class App extends Application {
    private static final String TAG = App.class.getName();
    public static Typeface TEXT_TYPE;
    public static Context AppContext;
    public static int KEMU = 1;
    public static int TEXT = 1;
    public static String typeId;
    public static UserBean userBean;
    public static String Total_score = "0";
    public static String path1 = Environment.getExternalStorageDirectory().getPath() + "/斗图大师";

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.isShow = true;
        AppContext = getApplicationContext();
        initPrefs();
        FlowManager.init(this);
//        final PushAgent mPushAgent = PushAgent.getInstance(this);
//        mPushAgent.register(new IUmengRegisterCallback() {
//
//            @Override
//            public void onSuccess(String deviceToken) {
//                //注册成功会返回device token
//                Log.i(TAG, "device token: " + deviceToken);
//
//                Toast.makeText(App.AppContext, mPushAgent.getRegistrationId(), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//
//            }
//        });
        initUMConfig();
    }

    /**
     * 初始化SharedPreference
     */
    protected void initPrefs() {
        SharedPreferencesUtil.init(getApplicationContext(), getPackageName() + "_preference", Context.MODE_MULTI_PROCESS);
    }

    private void initUMConfig() {
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        PlatformConfig.setQQZone("1105886594", "CUKkoCW26egFbEL5");
        PlatformConfig.setWeixin("wx8f75dcadece0c95f", "ca3d1f513757b97bbdc313eafff76a8a");
    }

}
