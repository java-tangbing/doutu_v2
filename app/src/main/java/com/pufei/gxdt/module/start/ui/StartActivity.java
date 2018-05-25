package com.pufei.gxdt.module.start.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.pufei.gxdt.MainActivity;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.utils.OkhttpUtils;
import com.pufei.gxdt.utils.UrlString;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by wangwenzhang on 2016/11/8.
 */
public class StartActivity extends Activity {
    @BindView(R.id.start_iamge)
    ImageView startIamge;
    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.activity_start_fram)
    LinearLayout activityStartFram;
    /* @InjectView(R.id.start_iamge)
        ImageView startIamge;
        @InjectView(R.id.start_time)
        TextView startTime;*/
    private String SHARE_APP_TAG = "frist_3.3.5";
    private final String TAG = "StartActivity";
    private int timer = 3;
    private boolean imageis = true;
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
                case 2:
//                    if (startAdvertBean.getResults() != null) {
//                        Intent intent = new Intent(StartActivity.this, WebAdvertActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("URL", startAdvertBean.getResults().get(0).getDest_url());
//                        bundle.putString("source", "start");
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    } else {
//                        IfStart();
//                    }
                    break;
                case 3:
                    IfStart();
                    break;
                case 4:
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private SharedPreferences setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        ButterKnife.bind(this);
//        StatusBarUtil.StatusBarLightMode(this);
        setting = getSharedPreferences(SHARE_APP_TAG, 0);//判断是否是第一次启动
        user_first = setting.getBoolean("FIRST", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            StatusBarUtil.transparencyBar(this);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (!user_first) {
            //getAdvert();
        }
        //getData();
        //Message message = handler.obtainMessage(1);
        handler.sendEmptyMessage(1);
        final Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo);
        //handler.sendMessageDelayed(message, 1000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(App.path1 + "/" + "0" + ".jpg");
                    FileOutputStream out = new FileOutputStream(file);
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    //保存图片后发送广播通知更新数据库
                    Uri uri = Uri.fromFile(file);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

    @OnClick({R.id.start_iamge, R.id.start_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_iamge:
                if (user_first) {
                    IfStart();
                } else {
//                    if (!TextUtils.isEmpty(advert_url)) {
//                        /*if (Build.VERSION.SDK_INT>=24){
//                            Intent intent = new Intent();
//                            intent.setAction("android.intent.action.VIEW");
//                            Uri content_url = Uri.parse(advert_url);
//                            intent.setData(content_url);
//                            startActivity(intent);
//                            handler.sendEmptyMessage(4);
//                        }else {*/
//                            imageis = false;
//                            Intent intent = new Intent(StartActivity.this, WebAdvertActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("URL", advert_url);
//                            bundle.putString("source", "start");
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                            handler.sendEmptyMessage(4);
//                        //}
//                    } else if (!TextUtils.isEmpty(down_url)){
//                       /* if (Build.VERSION.SDK_INT>=24){
//                            Intent intent = new Intent();
//                            intent.setAction("android.intent.action.VIEW");
//                            Uri content_url = Uri.parse(down_url);
//                            intent.setData(content_url);
//                            startActivity(intent);
//                            handler.sendEmptyMessage(4);
//                        }else {*/
//                            AgentUtils.getAgentUtils().getAgent(activityStartFram,StartActivity.this,down_url);
//                            handler.sendEmptyMessage(4);
//                        //}
//                    }else {
//                        IfStart();
//                    }

                }
                break;
            case R.id.start_time:
                IfStart();
                handler.removeMessages(1);
                break;
        }
    }

    private boolean user_first;

    private void IfStart() {
        if (user_first) {//第一次
            setting.edit().putBoolean("FIRST", false).apply();
            setting.edit().putBoolean("GIF", true).apply();
            setting.edit().apply();
            startActivity(new Intent(StartActivity.this, FristActivity.class));
            finish();
        } else {
            if (imageis) {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        }
    }

}
