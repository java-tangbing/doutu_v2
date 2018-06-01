package com.pufei.gxdt.module.discover.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpFragment;
import com.pufei.gxdt.module.discover.activity.DisPictureDetailActivity;
import com.pufei.gxdt.module.discover.adapter.DiscoverAdapter;
import com.pufei.gxdt.module.discover.bean.DiscoverEditImageBean;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import com.pufei.gxdt.module.discover.presenter.DiscoverPresenter;
import com.pufei.gxdt.module.home.activity.PictureDetailActivity;
import com.pufei.gxdt.module.discover.view.DiscoverView;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.widgets.viewpager.DividerGridItemDecoration;
import com.pufei.gxdt.widgets.viewpager.GridSpacingItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

public class DiscoverAllFragment extends BaseMvpFragment<DiscoverPresenter> implements DiscoverView
        , SwipeRefreshLayout.OnRefreshListener
        , BaseQuickAdapter.OnItemClickListener
        , BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.rv_all_dis)
    RecyclerView recyclerView;
    @BindView(R.id.dis_all_refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private List<DiscoverListBean.ResultBean> mlist;
    private DiscoverAdapter discoverAdapter;
    private int page;
    private boolean isLoadMore = false;
    private boolean isDiscover = false;


    @Override
    public void initView() {

        GridLayoutManager layoutManage = new GridLayoutManager(activity, 2);
        recyclerView.setLayoutManager(layoutManage);
        int spanCount = 2; //  columns
        int spacing = 30; // px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(activity));


    }

    @Override
    public void getData() {
        mlist = new ArrayList<>();
        discoverAdapter = new DiscoverAdapter(mlist);
        discoverAdapter.setEnableLoadMore(false);
        discoverAdapter.setOnItemClickListener(this);
        discoverAdapter.setOnLoadMoreListener(this, recyclerView);
//        discoverAdapter.addHeaderView(videoHeaderView);
        recyclerView.setAdapter(discoverAdapter);
        discoverAdapter.disableLoadMoreIfNotFullPage();
        page = 1;
        setMyadapter();
        initRefreshLayout();
    }

    private void setMyadapter() {
        JSONObject jsonObject = KeyUtil.getJson(getContext());
        try {
            jsonObject.put("order", "");
            jsonObject.put("page", page + "");
            jsonObject.put("auth", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetWorkUtil.isNetworkConnected(getActivity())) {
            presenter.discoverHotList(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } else {
            ToastUtils.showShort(getActivity(), "请检查网络设置");
        }
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
        Intent intent = new Intent(activity, DisPictureDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", mlist.get(position).getId());
        bundle.putString("orginid", mlist.get(position).getOrginid());
        bundle.putString("orgintable", mlist.get(position).getOrgintable());
        bundle.putInt("picture_index", position);
        bundle.putSerializable("picture_list", (Serializable) mlist);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void getDiscoverHotList(DiscoverListBean bean) {
        if (bean.getResult() == null) return;
        if (bean.getResult().size() > 0) {
            page = page + 1;
//            if (isDiscover) {
//                isDiscover = false;
//                mlist.clear();
//                mlist.addAll(bean.getResult());
//                discoverAdapter.setNewData(mlist);
//            } else {
//                mlist.addAll(bean.getResult());
//                discoverAdapter.loadMoreComplete();
//            }

            mlist.addAll(bean.getResult());
            discoverAdapter.setNewData(mlist);
            discoverAdapter.loadMoreComplete();
            discoverAdapter.notifyDataSetChanged();
//            isLoadMore = false;
        } else {
//            isLoadMore = false;
            discoverAdapter.loadMoreEnd();
        }

    }

    @Override
    public void getDiscoverDetailed(DiscoverEditImageBean bean) {

    }

    @Override
    public void onLoadMoreRequested() {
        setMyadapter();
    }


    private void initRefreshLayout() {
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
//        final Random random = new Random();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setMyadapter();
//                mList.add(0, "我是天才" + random.nextInt(100) + "号");
                discoverAdapter.notifyDataSetChanged();
                Toast.makeText(activity, "刷新了一条数据", Toast.LENGTH_SHORT).show();
                // 加载完数据设置为不刷新状态，将下拉进度收起来
                refreshLayout.setRefreshing(false);
            }
        }, 1200);


    }
}
