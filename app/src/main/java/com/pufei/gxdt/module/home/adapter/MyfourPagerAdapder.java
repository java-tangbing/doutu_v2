package com.pufei.gxdt.module.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MyfourPagerAdapder extends FragmentPagerAdapter {
	private List<Fragment> list;
	private List<String> titlelist;
	public MyfourPagerAdapder(FragmentManager fm, List<Fragment> list, List<String> titllelist) {
		super(fm);
		this.list=list;
		this.titlelist=titllelist;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titlelist.get(position%titlelist.size());
	}
}
