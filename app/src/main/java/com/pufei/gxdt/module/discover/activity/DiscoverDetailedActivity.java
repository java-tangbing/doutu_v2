package com.pufei.gxdt.module.discover.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.discover.adapter.DiscoverDetailedAdapter;
import com.pufei.gxdt.module.discover.bean.DiscoverEditImageBean;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import com.pufei.gxdt.module.discover.presenter.DiscoverPresenter;
import com.pufei.gxdt.module.discover.view.DiscoverView;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.widgets.GlideApp;
import com.pufei.gxdt.widgets.viewpager.DividerGridItemDecoration;
import com.pufei.gxdt.widgets.viewpager.GridSpacingItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class DiscoverDetailedActivity extends BaseMvpActivity<DiscoverPresenter> implements DiscoverView, BaseQuickAdapter.OnItemChildClickListener {
    @BindView((R.id.dis_detailed_original_iv))
    ImageView originalImageView;
    @BindView((R.id.dis_det_back_iv))
    ImageView imageViewBack;
    @BindView((R.id.dis_detailed_count_iv))
    TextView countTextView;
    @BindView((R.id.dis_det_username_tv))
    TextView tv_username;

    @BindView(R.id.dis_det_ry)
    RecyclerView recyclerView;
    private List<DiscoverEditImageBean.ResultBean.DataBean> mlist;
    private DiscoverDetailedAdapter discoverDetailedAdapter;
    private String orginid, orgintable, id, uid;

    @Override
    public void initView() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        orginid = intent.getStringExtra("orginid");
        orgintable = intent.getStringExtra("orgintable");
        uid = intent.getStringExtra("uid");
        GridLayoutManager layoutManage = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManage);
        int spanCount = 2; //  columns
        int spacing = dp2px(10); // px
        boolean includeEdge = true;
        //间距
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        //边框
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
        setOrginImagedet();
        mlist = new ArrayList<>();
        discoverDetailedAdapter = new DiscoverDetailedAdapter(mlist);
        discoverDetailedAdapter.setOnItemChildClickListener(this);
//        discoverAdapter.addHeaderView(videoHeaderView);
        recyclerView.setAdapter(discoverDetailedAdapter);
        setAdapter();
    }

    private void setAdapter() {
        JSONObject jsonObject = KeyUtil.getJson(this);
        try {
            if (id == null || orginid == null || orgintable == null || uid == null) return;
            jsonObject.put("id", id);
            jsonObject.put("orginid", orginid);//orginid 原始图id
            jsonObject.put("orgintable", orgintable);//orgintable 数据
            jsonObject.put("uid", uid);
            jsonObject.put("auth", App.userBean.getAuth());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.discoverEditImage(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } else {
            ToastUtils.showShort(this, getResources().getString(R.string.check_the_network_please));
        }

    }

    @Override
    public int getLayout() {
        return R.layout.activity_discover_det;
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

    }

    @Override
    public void getDiscoverDetailed(DiscoverEditImageBean bean) {

        if (bean.getResult() == null) return;

        GlideApp.with(this).load(bean.getResult().getOrgin_url())
                .placeholder(R.mipmap.ic_default_picture).into(originalImageView);
        countTextView.setText(bean.getResult().getCount());
        tv_username.setText(bean.getResult().getUsername());
        if (bean.getResult().getData().size() > 0) {
//            mlist = new ArrayList<>();
            mlist.clear();
            mlist.addAll(bean.getResult().getData());
//            discoverDetailedAdapter.setNewData(mlist);
            discoverDetailedAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }

    @Override
    public void requestErrResult(String msg) {

    }

    public void setOrginImagedet() {
    }

//    @Override
//    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//        Intent intent = new Intent(this, DisPictureDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("id", mlist.get(position).getId());
//        bundle.putString("orginid", mlist.get(position).getOrginid());
//        bundle.putString("orgintable", mlist.get(position).getOrgintable());
//        bundle.putInt("picture_index", position);
//        bundle.putString("type", "disdet");
//        bundle.putString("isSaveImg", mlist.get(position).getIsSaveImg());
//        bundle.putSerializable("picture_list", (Serializable) mlist);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }

    @OnClick(R.id.dis_det_back_iv)
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.dis_det_back_iv:
                finish();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.dis_item_iv:
                Intent intent = new Intent(this, DisPictureDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", "disdet");
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
                Intent intent01 = new Intent(this, DisWorksActivity.class);
                Bundle bundle01 = new Bundle();
                bundle01.putString("uid", mlist.get(position).getUser().getUid());
                intent01.putExtras(bundle01);
                startActivity(intent01);
                break;
        }
    }
}
