package com.pufei.gxdt.module.discover.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.module.discover.adapter.DiscoverAdapter;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import com.pufei.gxdt.module.discover.presenter.DiscoverPresenter;
import com.pufei.gxdt.widgets.viewpager.DividerGridItemDecoration;
import com.pufei.gxdt.widgets.viewpager.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DiscoverDetailedActivity extends BaseMvpActivity<DiscoverPresenter> {

    @BindView(R.id.dis_det_ry)
    RecyclerView recyclerView;
    private List<DiscoverListBean.ResultBean> mlist;
    private DiscoverAdapter discoverAdapter;

    @Override
    public void initView() {
        GridLayoutManager layoutManage = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManage);
        int spanCount = 2; //  columns
        int spacing = dp2px(10); // px
        boolean includeEdge = true;
        //间距
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        //边框线
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
    }

    /**
     * convert dp to its equivalent px
     */
    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    public void getData() {
        mlist = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            DiscoverListBean.ResultBean bean = new DiscoverListBean.ResultBean();
            mlist.add(bean);
        }
        discoverAdapter = new DiscoverAdapter(mlist);
//        discoverAdapter.setOnItemClickListener(this);
//        discoverAdapter.addHeaderView(videoHeaderView);
        recyclerView.setAdapter(discoverAdapter);
        setAdapter();
    }

    private void setAdapter() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_discover_det;
    }

    @Override
    public void setPresenter(DiscoverPresenter presenter) {

    }
}
