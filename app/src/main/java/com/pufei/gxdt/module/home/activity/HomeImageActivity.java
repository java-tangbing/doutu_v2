package com.pufei.gxdt.module.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.module.home.adapter.HomeImageAdapter;
import com.pufei.gxdt.module.home.model.HomeResultBean;
import com.pufei.gxdt.module.home.model.HomeTypeBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.presenter.HomeListPresenter;
import com.pufei.gxdt.module.home.view.HomeListView;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.widgets.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
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
    @BindView(R.id.tv_top_title)
    TextView tv_top_title;
    @BindView(R.id.tv_hot)
    TextView tv_hot;
    @BindView(R.id.tv_eyes)
    TextView tv_eyes;
    private HomeImageAdapter adapter;
    @BindView(R.id.request_failed)
    LinearLayout request_failed;
    List<PictureResultBean.ResultBean> list = new ArrayList<>();
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
    public void resultHomeDetailList(PictureResultBean bean) {
        if(bean!=null){
            if(page == 1){
                list.clear();
            }
            list.addAll(bean.getResult());
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void resultHomeTypeList(HomeTypeBean bean) {

    }

    @Override
    public void initView() {
        tv_title.setText("");
        tv_top_title.setText(getIntent().getExtras().getString("title"));
        tv_hot.setText(getIntent().getExtras().getString("hot"));
        tv_eyes.setText(getIntent().getExtras().getString("eyes"));
        ll_left.setVisibility(View.VISIBLE);
        ll_right.setVisibility(View.VISIBLE);
        iv_right.setBackgroundResource(R.mipmap.com_bt_ttab_star_normal);
        xRecyclerView.setLayoutManager(new GridLayoutManager(HomeImageActivity.this, 3));
        xRecyclerView.addItemDecoration(new SpaceItemDecoration(dp2px(HomeImageActivity.this, 10)));
        adapter = new HomeImageAdapter(HomeImageActivity.this, list);
        xRecyclerView.setAdapter(adapter);
        srf_home_image.setRefreshHeader(new ClassicsHeader(this).setSpinnerStyle(SpinnerStyle.Translate));
        srf_home_image.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Translate));
        srf_home_image.setEnableLoadmore(true);
        srf_home_image.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        requestHomeImage(page);
                        try {
                            refreshlayout.finishLoadmore();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                    }
                }, 2000);
            }

            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        requestHomeImage(page);
                        try {
                            refreshlayout.finishRefresh();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);
            }
        });
        adapter.setOnItemClickListener(new HomeImageAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                Intent intent = new Intent(HomeImageActivity.this, PictureDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("picture_index", postion);
                bundle.putSerializable("picture_list", (Serializable) list);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onAdd(int position) {

            }
        });
    }

    @Override
    public void getData() {
        requestHomeImage(page);
    }
    private void requestHomeImage(int page){
        if (NetWorkUtil.isNetworkConnected(HomeImageActivity.this)) {
            try {
                JSONObject jsonObject = KeyUtil.getJson(this);
                jsonObject.put("category_id", getIntent().getExtras().getString("category_id"));
                jsonObject.put("page", page + "");
                jsonObject.put("net", NetWorkUtil.netType(HomeImageActivity.this));
                presenter.getHomeDetailList(RetrofitFactory.getRequestBody(jsonObject.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
                request_failed.setVisibility(View.VISIBLE);
            }
    }
    private  int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    @Override
    public int getLayout() {
        return R.layout.activity_home_image;
    }
}