package com.pufei.gxdt.module.home.activity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.base.MyPagerAdapder;
import com.pufei.gxdt.module.home.fragment.DouTuFragment;
import com.pufei.gxdt.module.home.model.HomeTypeBean;
import com.pufei.gxdt.widgets.viewpager.MyViewPager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;

/**表情分类
 * Created by tb on 2018/5/23.
 */

public class FaceTypeActivity extends BaseMvpActivity implements TabLayout.OnTabSelectedListener {
    @BindView(R.id.fragment_home_tb)
    TabLayout fragmentHomeTb;
    @BindView(R.id.home_vp_type)
    MyViewPager homeVpDriver;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private String[] titleArray = new String[8];
    private List<HomeTypeBean.ResultBean> titleLists = new ArrayList<>();
    private int index;
    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public void initView() {
        Bundle bundle = this.getIntent().getExtras();
        titleLists = (List<HomeTypeBean.ResultBean>) bundle.getSerializable("title_list");
        index = bundle.getInt("index");
        for (int i = 0;i<8;i++){
            titleArray[i] = titleLists.get(i).getCategory_name();
        }
        addfragment();
        fragmentHomeTb.setTabMode(TabLayout.MODE_SCROLLABLE);
        homeVpDriver.setOffscreenPageLimit(1);
        homeVpDriver.setAdapter(new MyPagerAdapder(getSupportFragmentManager(), fragmentList, titleList));
        homeVpDriver.setCurrentItem(index);
        fragmentHomeTb.addOnTabSelectedListener(this);
        fragmentHomeTb.setupWithViewPager(homeVpDriver);
        setIndicator(fragmentHomeTb,5,5);
    }
    @Override
    public void getData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_type_face;
    }
    private void addfragment() {
        fragmentList = new ArrayList<Fragment>();
        for (int i = 0;i<titleArray.length;i++){
            fragmentList.add(DouTuFragment.newInstence(titleLists.get(i).getId()));
        }
        titleList = new ArrayList<String>();
        Collections.addAll(titleList, titleArray);
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
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

}
