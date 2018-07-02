package com.pufei.gxdt.module.discover.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpFragment;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.discover.activity.DisPictureDetailActivity;
import com.pufei.gxdt.module.discover.activity.DisWorksActivity;
import com.pufei.gxdt.module.discover.adapter.DiscoverAdapter;
import com.pufei.gxdt.module.discover.bean.DiscoverEditImageBean;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import com.pufei.gxdt.module.discover.bean.IsSaveImgBean;
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
import butterknife.OnClick;

public class DiscoverAllFragment extends BaseMvpFragment<DiscoverPresenter> implements DiscoverView
        , SwipeRefreshLayout.OnRefreshListener
        , BaseQuickAdapter.OnItemChildClickListener
        , BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.rv_all_dis)
    RecyclerView recyclerView;
    @BindView(R.id.dis_all_refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.request_failed)
    LinearLayout requestFailed;


    private List<DiscoverListBean.ResultBean> mlist;
    private DiscoverAdapter discoverAdapter;
    private int page;
    private boolean isLoadMore = false;
    private boolean isRefreshing = false;
    private boolean isfirst = true;
    private String auth;
    private final static int REQUESTCODE = 1; // 返回的结果码
    private List<IsSaveImgBean> mbeanlist = new ArrayList<>();

    @Override
    public void initView() {
        requestFailed.setVisibility(View.GONE);
        final GridLayoutManager layoutManage = new GridLayoutManager(activity, 2);
        recyclerView.setLayoutManager(layoutManage);
        int spanCount = 2; //  columns
        int spacing = 30; // px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(activity));

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
//                        (layoutManage.findLastVisibleItemPosition() ==
//                                layoutManage.getItemCount() - 1)
//                        ) {
//                    page++;
////                    requestHomeImage(page);
//                    setMyadapter();
//                }
//            }
//        });

    }

    @Override
    public void getData() {

        mlist = new ArrayList<>();
        discoverAdapter = new DiscoverAdapter(mlist);


//        discoverAdapter.setEnableLoadMore(false);
        discoverAdapter.setOnItemChildClickListener(this);
        discoverAdapter.setOnLoadMoreListener(this, recyclerView);
        discoverAdapter.setPreLoadNumber(1);
        discoverAdapter.setLoadMoreView(new CustomLoadMoreView());
        discoverAdapter.setEnableLoadMore(true);
//        discoverAdapter.addHeaderView(videoHeaderView);
//        discoverAdapter.disableLoadMoreIfNotFullPage();
        recyclerView.setAdapter(discoverAdapter);

        page = 1;
        setMyadapter();
        initRefreshLayout();
    }

    public final class CustomLoadMoreView extends LoadMoreView {

        @Override
        public int getLayoutId() {
            return R.layout.customloadmore_view;
        }

        /**
         * 如果返回true，数据全部加载完毕后会隐藏加载更�
         * 如果返回false，数据全部加载完毕后会显示getLoadEndViewId()布局
         */
        @Override
        public boolean isLoadEndGone() {
            return true;
        }

        @Override
        protected int getLoadingViewId() {
            return R.id.load_more_loading_view;
        }

        @Override
        protected int getLoadFailViewId() {
            return R.id.load_more_load_fail_view;
        }

        /**
         * isLoadEndGone()为true，可以返�
         * isLoadEndGone()为false，不能返�
         */
        @Override
        protected int getLoadEndViewId() {
            return 0;
        }
    }

    private void setMyadapter() {
//        if (App.userBean == null) {
//            startActivity(new Intent(activity, LoginActivity.class));
//        } else {
        if (NetWorkUtil.isNetworkConnected(getActivity())) {
            requestFailed.setVisibility(View.GONE);
            auth = SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH);
            JSONObject jsonObject = KeyUtil.getJson(getContext());
            try {
                jsonObject.put("order", "");
                jsonObject.put("page", page + "");
                jsonObject.put("auth", auth);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            presenter.discoverHotList(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } else {
            requestFailed.setVisibility(View.VISIBLE);

//            ToastUtils.showShort(getActivity(), getResources().getString(R.string.check_the_network_please));
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
        if (bean == null) return;
        if (page == 1) {
            mlist.clear();
        }
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
//                ToastUtils.showShort(getActivity(), getResources().getString(R.string.msg_refresh_success));
            }
            if (isfirst) {
                page = page + 1;
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
//                startActivity(intent);
                // 这种启动方式：startActivity(intent);并不能返回结�
                startActivityForResult(intent, REQUESTCODE);//REQUESTCODE--->1
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

    // 为了获取结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        // operation succeeded. 默认值是-1
        if (requestCode == REQUESTCODE) {
            switch (resultCode) {
                case 10:
                    mbeanlist = (List<IsSaveImgBean>) data.getSerializableExtra("IsSaveImgBeanList");
                    if (mbeanlist.size() == 0) return;
                    for (int i = 0; i < mbeanlist.size(); i++) {
                        int mindex = mbeanlist.get(i).getIndex();
                        String isSaveImg = mbeanlist.get(i).getIsSaveImg();
                        mlist.get(mindex).setIsSaveImg(isSaveImg);

                    }
                    break;
            }
        }
    }

    public void refresh() {
        page = 1;
        isRefreshing = true;
        isLoadMore = false;
        setMyadapter();
    }

    @Override
    public void onResume() {
        super.onResume();
//        refresh();
    }

    @OnClick({R.id.btn_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_refresh:
                refresh();
                break;
        }
    }
}
