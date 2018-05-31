package com.pufei.gxdt.module.discover.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.discover.adapter.DisOtherPictureAdapter;
import com.pufei.gxdt.module.discover.bean.DisPicDetBean;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import com.pufei.gxdt.module.discover.presenter.DisPicDetPresenter;
import com.pufei.gxdt.module.discover.presenter.DiscoverPresenter;
import com.pufei.gxdt.module.discover.view.DisPicDetView;
import com.pufei.gxdt.module.home.adapter.OtherPictureAdapter;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.widgets.GlideApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 表情操作
 * Created by tb on 2018/5/23.
 */

public class DisPictureDetailActivity extends BaseMvpActivity<DisPicDetPresenter> implements DisPicDetView, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.iv_now_picture)
    ImageView iv_picture;
    @BindView(R.id.look_edit_image_iv)
    ImageView iv_editimage;
    @BindView(R.id.activity_home1_cancel)
    LinearLayout linearLayout_cancel;
    @BindView(R.id.rl_picture)
    RecyclerView rl_picture;

    @BindView(R.id.activity_home1_title)
    TextView textViewTitle;


    @BindView(R.id.tv_eyes)
    TextView tv_eyes;
    @BindView(R.id.tv_hot)
    TextView tv_hot;
    @BindView(R.id.tv_change_img)
    TextView tv_change_img;
    private int index;
    private String orginid, orgintable, id;
    private List<DiscoverListBean.ResultBean> pictureList = new ArrayList<>();
    private List<DiscoverListBean.ResultBean> mlist = new ArrayList<>();
    private DisOtherPictureAdapter adapter;

    @Override
    public void initView() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        orginid = intent.getStringExtra("orginid");
        orgintable = intent.getStringExtra("orgintable");
        index = intent.getIntExtra("picture_index", 0);
        pictureList = (List<DiscoverListBean.ResultBean>) intent.getSerializableExtra("picture_list");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rl_picture.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void getData() {


        adapter = new DisOtherPictureAdapter(mlist);
        rl_picture.setAdapter(adapter);
        setMyAdapter();
        getImageDetailList();
    }


    public void setMyAdapter() {
        JSONObject jsonObject = KeyUtil.getJson(this);
        try {
            jsonObject.put("id", id);
            jsonObject.put("orginid", orginid);
            jsonObject.put("orgintable", orgintable);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.imageDetail(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } else {
            ToastUtils.showShort(this, "请检查网络设置");
        }
    }

    public void getImageDetailList() {
        if (pictureList.size() > 0) {
            mlist.addAll(pictureList);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void getImageDetail(DisPicDetBean bean) {
        GlideApp.with(this).load(bean.getResult().getUrl()).placeholder(R.mipmap.ic_default_picture)
                .into(iv_picture);
        tv_eyes.setText(bean.getResult().getView());
        tv_hot.setText(bean.getResult().getHot());
        tv_change_img.setText(bean.getResult().getCount());
    }


    @Override
    public int getLayout() {
        return R.layout.activity_look;
    }

    @Override
    public void setPresenter(DisPicDetPresenter presenter) {
        if (presenter == null) {
            this.presenter = new DisPicDetPresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, DisPictureDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", mlist.get(position).getId());
        bundle.putString("orginid", mlist.get(position).getOrginid());
        bundle.putString("orgintable", mlist.get(position).getOrgintable());
        bundle.putSerializable("pictureList", (Serializable) pictureList);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


    @OnClick({R.id.look_edit_image_iv, R.id.activity_home1_cancel,R.id.tv_change_img})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.activity_home1_cancel:
                finish();
                break;
            case R.id.look_edit_image_iv:
                finish();
                break;
            case R.id.tv_change_img:
                finish();
                break;
        }
    }
}
