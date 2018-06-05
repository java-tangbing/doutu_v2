package com.pufei.gxdt.module.discover.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpFragment;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.discover.activity.DisPictureDetailActivity;
import com.pufei.gxdt.module.discover.activity.DisWorksActivity;
import com.pufei.gxdt.module.discover.adapter.DiscoverAdapter;
import com.pufei.gxdt.module.discover.bean.DiscoverEditImageBean;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import com.pufei.gxdt.module.discover.presenter.DiscoverPresenter;

import com.pufei.gxdt.module.discover.view.DiscoverView;
import com.pufei.gxdt.module.login.activity.LoginActivity;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.widgets.viewpager.DividerGridItemDecoration;
import com.pufei.gxdt.widgets.viewpager.GridSpacingItemDecoration;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;

public class DiscoverAllFragment extends BaseMvpFragment<DiscoverPresenter> implements DiscoverView
        , SwipeRefreshLayout.OnRefreshListener
        , BaseQuickAdapter.OnItemChildClickListener
        , BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.rv_all_dis)
    RecyclerView recyclerView;
    @BindView(R.id.dis_all_refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private List<DiscoverListBean.ResultBean> mlist;
    private DiscoverAdapter discoverAdapter;
    private int page;
    private boolean isLoadMore = false;
    private boolean isRefreshing = false;
    private boolean isfirst = true;
    private String auth;

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
        discoverAdapter.setOnItemChildClickListener(this);
        discoverAdapter.setOnLoadMoreListener(this, recyclerView);
//        discoverAdapter.addHeaderView(videoHeaderView);
        discoverAdapter.disableLoadMoreIfNotFullPage();
        recyclerView.setAdapter(discoverAdapter);

        page = 1;
        setMyadapter();
        initRefreshLayout();
    }

    private void setMyadapter() {
//        if (App.userBean == null) {
//            startActivity(new Intent(activity, LoginActivity.class));
//        } else {
        auth = SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH);
        JSONObject jsonObject = KeyUtil.getJson(getContext());
        try {
            jsonObject.put("order", "");
            jsonObject.put("page", page + "");
            jsonObject.put("auth", auth);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetWorkUtil.isNetworkConnected(getActivity())) {
            presenter.discoverHotList(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } else {
            ToastUtils.showShort(getActivity(), "请检查网络设置");
        }
//        }
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
    public void getDiscoverHotList(DiscoverListBean bean) {
//        if (bean.getResult() == null) return;
        if (bean.getResult().size() > 0) {
            if (isLoadMore) {
                page = page + 1;
                mlist.addAll(bean.getResult());
                discoverAdapter.notifyDataSetChanged();
                isLoadMore = false;
                discoverAdapter.loadMoreComplete();
            }
            if (isRefreshing) {
                page = page + 1;
                mlist = new ArrayList<>();
                mlist.addAll(bean.getResult());
                discoverAdapter.setNewData(mlist);
                discoverAdapter.notifyDataSetChanged();
                isRefreshing = false;
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showShort(getActivity(), "刷新完毕");
            }
            if (isfirst) {
                isfirst = false;
                isLoadMore = true;
                isRefreshing = true;
                mlist.addAll(bean.getResult());
                discoverAdapter.notifyDataSetChanged();
            }
        } else {
            swipeRefreshLayout.setRefreshing(false);
            discoverAdapter.loadMoreComplete();
            discoverAdapter.loadMoreEnd();
//            ToastUtils.showShort(getActivity(), "刷新完毕");
        }

    }

    @Override
    public void getDiscoverDetailed(DiscoverEditImageBean bean) {

    }

    @Override
    public void requestErrResult(String msg) {

    }

    //下拉刷新
    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(this);
    }


    //下拉刷新
    @Override
    public void onRefresh() {
        page = 1;
        isRefreshing = true;
        isLoadMore = false;
        setMyadapter();

//        swipeRefreshView.setRefreshing(false);
    }

    //上拉加载跟多
    @Override
    public void onLoadMoreRequested() {
        isLoadMore = true;
        isRefreshing = false;
        setMyadapter();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.dis_item_iv:
                Intent intent = new Intent(activity, DisPictureDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("isSaveImg", mlist.get(position).getIsSaveImg());
                bundle.putString("id", mlist.get(position).getId());
                bundle.putString("orginid", mlist.get(position).getOrginid());
                bundle.putString("orgintable", mlist.get(position).getOrgintable());
                bundle.putInt("picture_index", position);
                bundle.putSerializable("picture_list", (Serializable) mlist);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.dis_item_user_img_list:
                Intent intent01 = new Intent(activity, DisWorksActivity.class);
                Bundle bundle01 = new Bundle();
                bundle01.putString("uid", mlist.get(position).getUser().getUid());
                intent01.putExtras(bundle01);
                startActivity(intent01);
                break;
        }
    }
}
