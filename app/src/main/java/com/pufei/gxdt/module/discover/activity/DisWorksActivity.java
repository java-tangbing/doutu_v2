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
import com.pufei.gxdt.module.discover.adapter.DisWorksdAdapter;
import com.pufei.gxdt.module.discover.bean.DisWorksBean;
import com.pufei.gxdt.module.discover.presenter.DisWorksPresenter;
import com.pufei.gxdt.module.discover.view.DisWorksView;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.widgets.GlideApp;
import com.pufei.gxdt.widgets.viewpager.GridSpacingItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;


public class DisWorksActivity extends BaseMvpActivity<DisWorksPresenter> implements DisWorksView, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.dis_work_user_icon_cv)
    CircleImageView cvUserIcon;
    @BindView(R.id.dis_work_user_name_tv)
    TextView tvUserName;
    @BindView(R.id.dis_work_sex_iv)
    ImageView ivUserSex;
    @BindView(R.id.dis_work_mind_tv)
    TextView tvUserMind;
    @BindView(R.id.dis_work_total_score_tv)
    TextView tvUserTotalScore;

    @BindView(R.id.dis_works_ry)
    RecyclerView recyclerView;
    private DisWorksdAdapter disWorksdAdapter;
    private String uid = "";
    private List<DisWorksBean.ResultBean.ProductBean> mlist;

    @Override
    public void initView() {
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        GridLayoutManager layoutManage = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManage);
        int spanCount = 3; //  columns
        int spacing = dp2px(5); // px
        boolean includeEdge = true;
        //间距
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


    }

    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    @Override
    public void getData() {
        mlist = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            DisWorksBean.ResultBean.ProductBean bean = new DisWorksBean.ResultBean.ProductBean();
//            mlist.add(bean);
//        }
        disWorksdAdapter = new DisWorksdAdapter(mlist);
        disWorksdAdapter.setOnItemClickListener(this);
//        discoverAdapter.addHeaderView(videoHeaderView);
        recyclerView.setAdapter(disWorksdAdapter);
        setAdapter();
    }

    private void setAdapter() {
        JSONObject jsonObject = KeyUtil.getJson(this);
        try {
            jsonObject.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.worksDetail(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } else {
            ToastUtils.showShort(this, getResources().getString(R.string.check_the_network_please));
        }

    }

    @Override
    public int getLayout() {
        return R.layout.activity_disworks;
    }


    @Override
    public void setPresenter(DisWorksPresenter presenter) {
        if (presenter == null) {
            this.presenter = new DisWorksPresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void getWorksDetail(DisWorksBean bean) {
        if (bean.getResult() == null) return;
        GlideApp.with(this).load(bean.getResult().getUser().getHeader())
                .placeholder(R.mipmap.ic_default_picture).into(cvUserIcon);
        tvUserName.setText(bean.getResult().getUser().getUsername());
        if ("男".equals(bean.getResult().getUser().getGender())) {
            GlideApp.with(this).load(R.mipmap.heuser_ic_woman)
                    .into(ivUserSex);
        } else if ("女".equals(bean.getResult().getUser().getGender())) {
            GlideApp.with(this).load(R.mipmap.heuser_ic_man)
                    .into(ivUserSex);
        }
        if (bean.getResult().getUser().getUsername().length() != 0) {
            tvUserMind.setText(bean.getResult().getUser().getMind());
        }
        if (bean.getResult().getUser().getUsername().length() != 0) {
            tvUserMind.setText(bean.getResult().getUser().getTotal_score());
        }

        if (bean.getResult().getProduct() != null && bean.getResult().getProduct().size() > 0) {
            mlist.addAll(bean.getResult().getProduct());
            disWorksdAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void requestErrResult(String msg) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent01 = new Intent(this, DiscoverDetailedActivity.class);
        Bundle bundle01 = new Bundle();
        bundle01.putString("id", mlist.get(position).getId());
        bundle01.putString("orginid", mlist.get(position).getOrginid());
        bundle01.putString("orgintable", mlist.get(position).getOrgintable());
        bundle01.putString("uid", mlist.get(position).getUid());
        intent01.putExtras(bundle01);
        startActivity(intent01);
    }
}
