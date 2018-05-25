package com.pufei.gxdt.module.home.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.base.MyPagerAdapder;
import com.pufei.gxdt.module.home.fragment.DouTuFragment;
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
    private String[] titleArray = {"斗图", "搞笑", "萌娃", "宠物", "明星", "影视", "美食", "其他"};
    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public void initView() {
        addfragment();
        fragmentHomeTb.setTabMode(TabLayout.MODE_SCROLLABLE);
        homeVpDriver.setAdapter(new MyPagerAdapder(getSupportFragmentManager(), fragmentList, titleList));
        //    reflex(tabDriver);
        fragmentHomeTb.addOnTabSelectedListener(this);
        fragmentHomeTb.setupWithViewPager(homeVpDriver);
        reflexTabIndicatorWidth();
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
        fragmentList.add(new DouTuFragment());
        fragmentList.add(new DouTuFragment());
        fragmentList.add(new DouTuFragment());
        fragmentList.add(new DouTuFragment());
        fragmentList.add(new DouTuFragment());
        fragmentList.add(new DouTuFragment());
        fragmentList.add(new DouTuFragment());
        fragmentList.add(new DouTuFragment());
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
    public void reflexTabIndicatorWidth(){
        Class<?> tablayout = fragmentHomeTb.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tablayout.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
            LinearLayout ll_tab= (LinearLayout) tabStrip.get(fragmentHomeTb);
            for (int i = 0; i < ll_tab.getChildCount(); i++) {
                View child = ll_tab.getChildAt(i);
                child.setPadding(0,0,0,0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1);
                params.setMarginStart(dip2px(App.AppContext,5f));
                params.setMarginEnd(dip2px(App.AppContext,5f));
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
