package com.pufei.gxdt.module.home.activity;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.module.home.adapter.HotAdapter;
import com.pufei.gxdt.module.home.model.HomeDetailBean;
import com.pufei.gxdt.module.home.model.HomeResultBean;
import com.pufei.gxdt.module.home.presenter.HomeListPresenter;
import com.pufei.gxdt.module.home.view.HomeListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by tb on 2018/5/28.
 */

public class HomeImageActivity extends BaseMvpActivity<HomeListPresenter> implements HomeListView {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_title_left)
    LinearLayout ll_left;
    @BindView(R.id.ll_title_right)
    LinearLayout ll_right;
    @BindView(R.id.iv_title_right)
    ImageView iv_right;
    @BindView(R.id.srf_home_image)
    SmartRefreshLayout srf_home_image;
    @BindView(R.id.xrl_home_image)
    XRecyclerView xRecyclerView;
    private HotAdapter adapter;
    List<HomeDetailBean.ResultBean> list = new ArrayList<>();
    private int page = 1;
    @Override
    public void setPresenter(HomeListPresenter presenter) {
        if (presenter == null) {
            this.presenter = new HomeListPresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void resultHomeList(HomeResultBean bean) {

    }

    @Override
    public void resultHomeDetailList(HomeDetailBean bean) {

    }

    @Override
    public void initView() {
        tv_title.setText("");
        ll_left.setVisibility(View.VISIBLE);
        ll_right.setVisibility(View.VISIBLE);
        iv_right.setBackgroundResource(R.mipmap.com_bt_ttab_star_normal);
        xRecyclerView.setLayoutManager(new GridLayoutManager(HomeImageActivity.this, 3));
//        adapter = new HotAdapter(HomeImageActivity.this, list);
//        hotXryv.setAdapter(adapter);
    }

    @Override
    public void getData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_home_image;
    }
}
