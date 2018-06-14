package com.pufei.gxdt.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.pufei.gxdt.module.news.activity.NewsPictureActivity;
import com.pufei.gxdt.module.sign.utils.ResolutionUtil;
import com.pufei.gxdt.module.user.bean.UserBean;
import com.pufei.gxdt.utils.LogUtils;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
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
        ResolutionUtil.getInstance().init(this);
        initPrefs();
        FlowManager.init(this);
        initUMConfig();
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.i(TAG, "device token: " + deviceToken);

            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

        UmengNotificationClickHandler clickHandler = new UmengNotificationClickHandler(){
            @Override
            public void openActivity(Context context, UMessage uMessage) {
                Intent intent = new Intent(context,NewsPictureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
        mPushAgent.setNotificationClickHandler(clickHandler);

        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 通知的回调方法
             * @param context
             * @param msg
             */
            @Override
            public void dealWithNotificationMessage(Context context, UMessage msg) {
                super.dealWithNotificationMessage(context, msg);
                Log.e("UPush",msg.text+"");
            }
        };
        mPushAgent.setMessageHandler(messageHandler);
    }

    /**
     * 初始化SharedPreference
     */
    protected void initPrefs() {
        SharedPreferencesUtil.init(getApplicationContext(), getPackageName() + "_preference", Context.MODE_MULTI_PROCESS);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initUMConfig() {
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setCatchUncaughtExceptions(true);
        PlatformConfig.setQQZone("1105886594", "CUKkoCW26egFbEL5");
        PlatformConfig.setWeixin("wx8f75dcadece0c95f", "ca3d1f513757b97bbdc313eafff76a8a");
    }

}
