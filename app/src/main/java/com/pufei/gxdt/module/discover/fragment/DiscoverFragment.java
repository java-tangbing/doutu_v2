package com.pufei.gxdt.module.discover.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseFragment;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.base.TabVpAdapter;
import com.pufei.gxdt.module.discover.adapter.DiscoverTabVpAdapter;
import com.pufei.gxdt.module.discover.presenter.DiscoverPresenter;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.widgets.viewpager.MyViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class DiscoverFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.tab_discover)
    TabLayout tabLayout;
    @BindView(R.id.vp_discover)
    MyViewPager myViewPager;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private String[] titleArray = getResources().getStringArray(R.array.discover_title);

    @Override
    public void initView() {
        addfragment();
    }

    @Override
    public void getData() {

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_discover;
    }


    private void addfragment() {
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new DiscoverRecommendFragment());
        fragmentList.add(new DiscoverAllFragment());
        titleList = new ArrayList<String>();
        Collections.addAll(titleList, titleArray);
    }

    private void init() {

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        myViewPager.setAdapter(new DiscoverTabVpAdapter(getChildFragmentManager(), fragmentList, titleList));
        //    reflex(tabDriver);
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(myViewPager);
//        reflexTabIndicatorWidth();
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

    @SuppressLint("NewApi")
    public void reflexTabIndicatorWidth() {
        Class<?> tablayout = tabLayout.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tablayout.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
            LinearLayout ll_tab = (LinearLayout) tabStrip.get(tabLayout);
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
}
