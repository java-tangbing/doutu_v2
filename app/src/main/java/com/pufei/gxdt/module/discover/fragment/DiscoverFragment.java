package com.pufei.gxdt.module.discover.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseFragment;
import com.pufei.gxdt.module.discover.adapter.DiscoverTabVpAdapter;
import com.pufei.gxdt.module.news.activity.NewsActivity;
import com.pufei.gxdt.widgets.GlideApp;
import com.pufei.gxdt.widgets.viewpager.MyViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DiscoverFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    @BindView(R.id.tv_title)
    TextView titleTextView;
    @BindView(R.id.tab_discover)
    TabLayout tabLayout;
    @BindView(R.id.vp_discover)
    MyViewPager myViewPager;
    @BindView(R.id.iv_title_right)
    ImageView newsImageView;

    @BindView(R.id.ll_title_right)
    LinearLayout newsLinearLayout;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private String[] titleArray = {"推荐", "全部"};

    @Override
    public void initView() {
//        if (isAdded()) {
//            titleArray = getResources().getStringArray(R.array.discover_title);
//        }
        titleTextView.setText("发现");
        GlideApp.with(getActivity()).load(R.mipmap.com_bt_ttab_news_normal).into(newsImageView);
        newsLinearLayout.setVisibility(View.VISIBLE);
        addfragment();
        init();
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
        setIndicator(tabLayout, 70, 70);
    }

    @OnClick({R.id.ll_title_right})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title_right:
                Intent intent = new Intent(getContext(), NewsActivity.class);
//        intent.putExtra("a", a);
//        intent.putExtra("b", b);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
//if(tab.getPosition()==0){
//
//}else {
//
//}
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    //下划线长
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

//    @SuppressLint("NewApi")
//    public void reflexTabIndicatorWidth() {
//        Class<?> tablayout = tabLayout.getClass();
//        Field tabStrip = null;
//        try {
//            tabStrip = tablayout.getDeclaredField("mTabStrip");
//            tabStrip.setAccessible(true);
//            LinearLayout ll_tab = (LinearLayout) tabStrip.get(tabLayout);
//            for (int i = 0; i < ll_tab.getChildCount(); i++) {
//                View child = ll_tab.getChildAt(i);
//                child.setPadding(0, 0, 0, 0);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
//                params.setMarginStart(dip2px(App.AppContext, 28f));
//                params.setMarginEnd(dip2px(getContext(), 28f));
//                child.setLayoutParams(params);
//                child.invalidate();
//            }
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    public int dip2px(Context context, float dipValue) {
//
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dipValue * scale + 0.5f);
//    }
}
