package com.pufei.gxdt.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.pufei.gxdt.R;
import com.pufei.gxdt.utils.AppManager;
import com.umeng.message.PushAgent;

import butterknife.ButterKnife;

/**
 * Activity的基�
 * Created by wangwenzhang on 2017/11/9.
 */

public abstract class BaseMvpActivity<P extends BasePresenter>extends AppCompatActivity implements BaseView<P> {
    protected P presenter;
    protected String TAG=getClass().getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white));
//        PushAgent.getInstance(this).onAppStart();
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
//        UMConfigure.setLogEnabled(true);
//        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE, "");
//        MobclickAgent.setCatchUncaughtExceptions(true);
        setPresenter(presenter);
        initView();
        getData();

    }

    /**
     * 初始化布局
     */
    public abstract void initView();

    /**
     * 获取数据
     */
    public abstract void getData();

    /**
     * 设置布局文件id
     * @return
     */
    public abstract int getLayout();

    /**
     * 布局销�调用presenter置空view，防止内存溢�
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter!=null){
            presenter.detachView();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(getClass().getSimpleName()); //手动统计页面("SplashScreen"为页面名称，可自定义)
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(getClass().getSimpleName());
//        MobclickAgent.onPause(this);
    }
}
