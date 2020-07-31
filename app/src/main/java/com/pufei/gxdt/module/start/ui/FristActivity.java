package com.pufei.gxdt.module.start.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import com.pufei.gxdt.MainActivity;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.floating.FloatWindowService;
import com.pufei.gxdt.module.login.activity.LoginActivity;
import com.pufei.gxdt.module.maker.activity.EditImageActivity;
import com.pufei.gxdt.utils.AppManager;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangwenzhang on 2016/11/1.第一次登陆的时的页面
 */
public class FristActivity extends Activity {
    @BindView(R.id.frist_vp)
    ViewPager fristVp;
    @BindView(R.id.dot_1)
    View dot1;
    @BindView(R.id.dot_2)
    View dot2;
    @BindView(R.id.dot_3)
    View dot3;
    private List<View> viewList;
    private List<View> dotList;
    private String SHARE_APP_USER = "FLOAT";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int oldPosition = 0;// 记录上一次点的位置
    private int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frist);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(SHARE_APP_USER, 0);
        editor = sharedPreferences.edit();
        //ButterKnife.inject(this);
        dotList = new ArrayList<>();
        viewList = new ArrayList<>();
//        dotList.add(dot1);
//        dotList.add(dot2);
//        dotList.add(dot3);
//        dotList.get(0).setBackgroundResource(R.drawable.dot_focused);
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.frist_vp1, null);
        Button button_v1 = (Button) view1.findViewById(R.id.frist_loading1_btn);
        View view2 = inflater.inflate(R.layout.frist_vp2, null);
        Button button_v2 = (Button) view2.findViewById(R.id.frist_loading2_btn);
        View view3 = inflater.inflate(R.layout.frist_vp3, null);
        Button button_v3 = (Button) view3.findViewById(R.id.frist_loading3_btn);
        final CheckBox cb = (CheckBox) view3.findViewById(R.id.checkBox);
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        final PagerAdapter pagerAdapter = new PagerAdapter() {//viewpager的适配器
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };
        fristVp.setAdapter(pagerAdapter);
        fristVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                //下面就是获取上一个位置，并且把点的状体设置成默认状体
//                dotList.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
//                //获取到选中页面对应的点，设置为选中状态
//                dotList.get(position).setBackgroundResource(R.drawable.dot_focused);
//                //下面是记录本次的位置，因为在滑动，他就会变成过时的点了
//                oldPosition = position;
//                //关联页卡
//                currentItem = position;
//                if (position >= viewList.size()) {
//                    if (cb.isChecked()) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            if (!Settings.canDrawOverlays(FristActivity.this)) {
//                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                                        Uri.parse("package:" + getPackageName()));
//                                startActivityForResult(intent, 101);
//                            } else {
//                                editor.putBoolean(SHARE_APP_USER, true).apply();
//                                startService(new Intent(FristActivity.this, FloatWindowService.class));
//                            }
//                        } else {
//                            editor.putBoolean(SHARE_APP_USER, true).apply();
//                            startService(new Intent(FristActivity.this, FloatWindowService.class));
//                        }
//
//
//                    }
//                    startActivity(new Intent(FristActivity.this, LoginActivity.class));//点击跳转回主页面
//                    finish();
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        button_v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fristVp.setCurrentItem(1);
            }
        });
        button_v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fristVp.setCurrentItem(2);
            }
        });
        button_v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FristActivity.this, EditImageActivity.class));
                AppManager.getAppManager().finishActivity();
//                if (cb.isChecked()) {
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (!Settings.canDrawOverlays(FristActivity.this)) {
//                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                                    Uri.parse("package:" + getPackageName()));
//                            startActivityForResult(intent, 101);
//                        } else {
//                            editor.putBoolean(SHARE_APP_USER, true).apply();
//                            startService(new Intent(FristActivity.this, FloatWindowService.class));
//                            Log.e("fristactivity", "111111111111111");
//                            startActivity(new Intent(FristActivity.this, LoginActivity.class));//点击跳转回主页面
//                            finish();
//                        }
//                    } else {
//                        editor.putBoolean(SHARE_APP_USER, true).apply();
//                        startService(new Intent(FristActivity.this, FloatWindowService.class));
//                        Log.e("fristactivity", "111111111111111");
//                        startActivity(new Intent(FristActivity.this, LoginActivity.class));//点击跳转回主页面
//                        finish();
//                    }
//
//
//                }

            }
        });
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case 101:
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (Settings.canDrawOverlays(FristActivity.this)) {
//                        editor.putBoolean(SHARE_APP_USER, true).apply();
//                        startService(new Intent(FristActivity.this, FloatWindowService.class));
//                    } else {
//                        Toast.makeText(FristActivity.this, "悬浮窗权限被拒绝，请在设置里面开启相应权限，若无相应权限会影响使用", Toast.LENGTH_LONG).show();
//                    }
//                    Log.e("fristactivity", "111111111111111");
//                    startActivity(new Intent(FristActivity.this, LoginActivity.class));//点击跳转回主页面
//                    finish();
//                }
//
//                break;
//            default:
//                super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

}
