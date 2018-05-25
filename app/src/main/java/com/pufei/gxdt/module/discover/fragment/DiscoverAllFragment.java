package com.pufei.gxdt.module.discover.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpFragment;
import com.pufei.gxdt.module.discover.activity.DiscoverDetailedActivity;
import com.pufei.gxdt.module.discover.adapter.DiscoverAdapter;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import com.pufei.gxdt.module.discover.presenter.DiscoverPresenter;
import com.pufei.gxdt.module.view.DiscoverView;
import com.pufei.gxdt.utils.IntenetUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.widgets.viewpager.DividerGridItemDecoration;
import com.pufei.gxdt.widgets.viewpager.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class DiscoverAllFragment extends BaseMvpFragment<DiscoverPresenter> implements DiscoverView, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.rv_all_dis)
    RecyclerView recyclerView;
    private List<DiscoverListBean.ResultBean> mlist;
    private DiscoverAdapter discoverAdapter;
    private int page;
    private boolean isLoadMore = false;

    @Override
    public void initView() {

        GridLayoutManager layoutManage = new GridLayoutManager(activity, 2);
        recyclerView.setLayoutManager(layoutManage);

        int spanCount = 2; //  columns
        int spacing = 20; // px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(activity));


    }

    @Override
    public void getData() {
        mlist = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DiscoverListBean.ResultBean bean = new DiscoverListBean.ResultBean();
            mlist.add(bean);
        }
        discoverAdapter = new DiscoverAdapter(mlist);
        discoverAdapter.setOnItemClickListener(this);
        discoverAdapter.setOnLoadMoreListener(this, recyclerView);
//        discoverAdapter.addHeaderView(videoHeaderView);
        recyclerView.setAdapter(discoverAdapter);
        setMyadapter();
    }

    private void setMyadapter() {
        if (NetWorkUtil.isNetworkConnected(getActivity())) {
            TelephonyManager tm = (TelephonyManager) App.AppContext.getSystemService((Context.TELEPHONY_SERVICE));
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (tm.getDeviceId() == null) {
                return;
            }
            Map<String, String> map = new HashMap<>();
            map.put("sign", "sign");
            map.put("key", "key");
            map.put("timestamp", getTime());
            map.put("os", "2");
            map.put("version", "1.0");
            map.put("net", IntenetUtil.getNetworkState(getContext()) + "");
            map.put("order", "");
            page = 1;
            map.put("order", page + "");
            presenter.discoverHotList(RetrofitFactory.getRequestBody(new Gson().toJson(map)));
        } else {
            ToastUtils.showShort(getActivity(), "请检查网络设置");
        }
    }

    //getTime方法返回的就是10位的时间戳
    public String getTime() {
        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        String str = String.valueOf(time);
        return str;

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_discover_all;
    }

    @Override
    public void setPresenter(DiscoverPresenter presenter) {
        if (presenter == null) {
            this.presenter = new DiscoverPresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(activity, DiscoverDetailedActivity.class);
//        intent.putExtra("a", a);
//        intent.putExtra("b", b);
        startActivity(intent);
    }

    @Override
    public void getDiscoverHotList(DiscoverListBean bean) {
        page = page + 1;
        if (bean.getResult().size() > 0) {
            mlist.addAll(bean.getResult());
            discoverAdapter.notifyDataSetChanged();
            discoverAdapter.loadMoreComplete();
            isLoadMore = false;
        } else {
            isLoadMore = false;
            discoverAdapter.loadMoreEnd(true);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (!isLoadMore) {
            isLoadMore = true;
            setMyadapter();
        }
    }
}
