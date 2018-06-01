package com.pufei.gxdt.module.user.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.module.user.adapter.FavoriteTabVpAdapter;
import com.pufei.gxdt.module.user.fragment.FavoriteJokeFragment;
import com.pufei.gxdt.module.user.fragment.FavoritePkgFragment;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.widgets.viewpager.MyViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FavoriteActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {
    @BindView(R.id.ll_title_left)
    LinearLayout ll_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tab_driver)
    TabLayout tabDriver;
    @BindView(R.id.home_vp_driver)
    MyViewPager homeVpDriver;
    //    @BindView(R.id.fake_status_bar)
//    View mFakeStatusBar;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private String[] titleArray = {"表情包", "表情"};

    @Override
    public void initView() {
        tv_title.setText("我的收藏");
        ll_left.setVisibility(View.VISIBLE);
        setTvTitleBackgroundColor();
        addfragment();
        init();
    }

    @Override
    public void getData() {
    }

    @Override
    public int getLayout() {
        return R.layout.activity_favorite;
    }

    private void init() {

        tabDriver.setTabMode(TabLayout.MODE_FIXED);
        homeVpDriver.setAdapter(new FavoriteTabVpAdapter(getSupportFragmentManager(), fragmentList, titleList));
        //    reflex(tabDriver);
        tabDriver.addOnTabSelectedListener(this);
        tabDriver.setupWithViewPager(homeVpDriver);
        reflexTabIndicatorWidth();
    }

    @OnClick(R.id.ll_title_left)
    public void backLastActivity() {
        AppManager.getAppManager().finishActivity();
    }

    private void addfragment() {
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new FavoritePkgFragment());
        fragmentList.add(new FavoriteJokeFragment());
        titleList = new ArrayList<String>();
        Collections.addAll(titleList, titleArray);
    }

    @SuppressLint("NewApi")
    public void reflexTabIndicatorWidth() {
        Class<?> tablayout = tabDriver.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tablayout.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
            LinearLayout ll_tab = (LinearLayout) tabStrip.get(tabDriver);
            for (int i = 0; i < ll_tab.getChildCount(); i++) {
                View child = ll_tab.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                params.setMarginStart(dip2px(App.AppContext, 28f));
                params.setMarginEnd(dip2px(App.AppContext, 28f));
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public int dip2px(Context context, float dipValue) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void setTvTitleBackgroundColor() {
//        mFakeStatusBar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
