package com.pufei.gxdt.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.pufei.gxdt.MainActivity;
import com.pufei.gxdt.R;
import com.pufei.gxdt.utils.UmengStatisticsUtil;

import java.util.List;

public class TabVpAdapter extends FragmentPagerAdapter {

    private List<Fragment> mData;
    private Context context;

    public TabVpAdapter(Context context,FragmentManager fm, List<Fragment> mData) {
        super(fm);
        this.context = context;
        this.mData = mData;
    }

    @Override
    public Fragment getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return 4;
    }

    public View getCustomView(int position){
        View view= LayoutInflater.from(context).inflate(R.layout.activity_main_tab_item,null);
        ImageView iv= (ImageView) view.findViewById(R.id.tab_iv);
        TextView tv= (TextView) view.findViewById(R.id.tab_tv);
        switch (position){
            case 0:
                iv.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.home_tab_state));
                tv.setText("首页");
                UmengStatisticsUtil.statisticsEvent(context,"1");
                break;
            case 1:
                iv.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.home_tab_find));
                tv.setText("发现");
                UmengStatisticsUtil.statisticsEvent(context,"2");
                break;
            case 2:
                iv.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.home_tab_made));
                tv.setText("制图");
                UmengStatisticsUtil.statisticsEvent(context,"3");
                break;
            case 3:
                iv.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.home_tab_user));
                tv.setText("我的");
                UmengStatisticsUtil.statisticsEvent(context,"5");
                break;
        }
        return view;
    }

}
