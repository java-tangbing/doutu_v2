package com.pufei.gxdt.module.start.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pufei.gxdt.MainActivity;
import com.pufei.gxdt.R;
import com.pufei.gxdt.contents.MsgType;
import com.pufei.gxdt.utils.AdvUtil;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.EvenMsg;
import com.pufei.gxdt.utils.LogUtils;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangwenzhang on 2016/11/8.
 */
public class StartActivity extends Activity {
    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.activity_start_fram)
    LinearLayout activityStartFram;
    @BindView(R.id.rl_adv)
    RelativeLayout rl_adv;
    private String SHARE_APP_TAG = "frist_3.3.5";
    private final String TAG = "StartActivity";
    private int timer = 5;
    private boolean imageis = true;
    private boolean user_first;
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    startTime.setText("跳过" + "(" + timer + ")");
                    timer--;
                    if (timer == 0) {
                        IfStart();
                        handler.removeMessages(1);
                    } else {
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private SharedPreferences setting;

    @Override
    public void onStart() {
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        super.onStart();
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            //加上判断
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        AppManager.getAppManager().addActivity(this);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            AppManager.getAppManager().finishActivity();
            return;
        }
        ButterKnife.bind(this);
//        StatusBarUtil.StatusBarLightMode(this);
        setting = getSharedPreferences(SHARE_APP_TAG, 0);//判断是否是第一次启
        user_first = setting.getBoolean("FIRST", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            StatusBarUtil.transparencyBar(this);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if(NetWorkUtil.isNetworkConnected(this)){
            AdvUtil.getInstance(this).getAdvHttp(this,rl_adv,7);
        }else{
            ToastUtils.showShort(this,"请检查网络设置");
            handler.sendEmptyMessage(1);
        }

//        if (!user_first) {
//            //getAdvert();
//        }
        //getData();
        //Message message = handler.obtainMessage(1);
//        handler.sendEmptyMessage(1);
//        final Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo);
//        //handler.sendMessageDelayed(message, 1000);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    File file = new File(App.path1 + "/" + "0" + ".jpg");
//                    FileOutputStream out = new FileOutputStream(file);
//                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                    out.flush();
//                    out.close();
//                    //保存图片后发送广播通知更新数据
//                    Uri uri = Uri.fromFile(file);
//                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateAdv(EvenMsg type) {
        if (type.getTYPE() == MsgType.START_ADV) {
            startTime.setVisibility(View.VISIBLE);
            handler.sendEmptyMessage(1);
        } else if (type.getTYPE() == MsgType.START_ADV_NO) {
            timer = 1;
            handler.sendEmptyMessage(1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @OnClick(R.id.start_time)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_time:
                IfStart();
                handler.removeMessages(1);
                break;
        }
    }


    private void IfStart() {
        if (user_first) {//第一
            setting.edit().putBoolean("FIRST", false).apply();
            setting.edit().putBoolean("GIF", true).apply();
            setting.edit().apply();
            startActivity(new Intent(StartActivity.this, FristActivity.class));
            AppManager.getAppManager().finishActivity();
        } else {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            AppManager.getAppManager().finishActivity();
        }
    }


}
