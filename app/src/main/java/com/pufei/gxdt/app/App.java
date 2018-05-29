package com.pufei.gxdt.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;

import com.pufei.gxdt.module.user.bean.UserBean;
import com.pufei.gxdt.utils.LogUtils;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;


public class App extends Application {

    public static Typeface TEXT_TYPE;
    public static Context AppContext;
    public static int KEMU = 1;
    public static int TEXT = 1;
    public static UserBean userBean;
    public static String token=null;
    public static String Total_score="0";
    public static String path1 = Environment.getExternalStorageDirectory().getPath() + "/斗图大师";

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.isShow = true;
        AppContext = getApplicationContext();
        initPrefs();
        FlowManager.init(this);
    }

    /**
     * 初始化SharedPreference
     */
    protected void initPrefs() {
        SharedPreferencesUtil.init(getApplicationContext(), getPackageName() + "_preference", Context.MODE_MULTI_PROCESS);
    }
}
