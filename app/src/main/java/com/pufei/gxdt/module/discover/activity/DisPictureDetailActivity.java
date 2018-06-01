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
import com.pufei.gxdt.module.home.model.PictureDetailBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.maker.activity.EditImageActivity;
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
    TextView iv_editimage;
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
    private String orginid, orgintable, id, uid, mcount;
    private List<DiscoverListBean.ResultBean> pictureList = new ArrayList<>();
    private List<DiscoverListBean.ResultBean> sendpictureList = new ArrayList<>();
    private List<DiscoverListBean.ResultBean> mlist = new ArrayList<>();

    private List<DiscoverListBean.ResultBean> pictureList01 = new ArrayList<>();
    private List<DiscoverListBean.ResultBean> mlist01 = new ArrayList<>();
    private DisOtherPictureAdapter adapter;

    @Override
    public void initView() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        orginid = intent.getStringExtra("orginid");
        orgintable = intent.getStringExtra("orgintable");
        index = intent.getIntExtra("picture_index", 0);
        pictureList = (List<DiscoverListBean.ResultBean>) intent.getSerializableExtra("picture_list");
//        sendpictureList=(List<DiscoverListBean.ResultBean>) intent.getSerializableExtra("picture_list");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rl_picture.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void getData() {


        adapter = new DisOtherPictureAdapter(mlist);
        adapter.setOnItemClickListener(this);
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

    public void setImageDetail() {
        JSONObject jsonObject = KeyUtil.getJson(this);
        try {
            jsonObject.put("id", id);
            jsonObject.put("orginid", orginid);
            jsonObject.put("orgintable", orgintable);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.getImageDetail(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } else {
            ToastUtils.showShort(this, "请检查网络设置");
        }
    }


    @Override
    public void getImageDetail(DisPicDetBean bean) {
        GlideApp.with(this).load(bean.getResult().getMake_url()).placeholder(R.mipmap.ic_default_picture)
                .into(iv_picture);
        if (bean.getResult().getView().length() > 0) {
            tv_eyes.setText(bean.getResult().getView());
        }
        if (bean.getResult().getHot().length() > 0) {
            tv_hot.setText(bean.getResult().getHot());
        }
        if (bean.getResult().getCount().length() > 0) {
            tv_change_img.setText(bean.getResult().getCount());
        }
        uid = bean.getResult().getUid();
        mcount = bean.getResult().getCount();
    }

    @Override
    public void resultImageDetail(PictureDetailBean bean) {
        if (bean.getResult() != null) {
            Intent intent = new Intent(this, EditImageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("picture_bean", bean.getResult());
            intent.putExtras(bundle);
            intent.putExtra(EditImageActivity.EDIT_TYPE, EditImageActivity.EDIT_TYPE_EDIT);
            startActivity(intent);
        } else {
            ToastUtils.showShort(this, "value is none");
        }

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
        id = mlist.get(position).getId();
        orginid = mlist.get(position).getOrginid();
        orgintable = mlist.get(position).getOrgintable();
        setMyAdapter();
    }


    @OnClick({R.id.look_edit_image_iv, R.id.activity_home1_cancel, R.id.tv_change_img})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.activity_home1_cancel:
                finish();
                break;
            case R.id.look_edit_image_iv:
                setImageDetail();
                break;
            case R.id.tv_change_img:
//                if (Integer.parseInt(mcount) > 0) {
                Intent intent01 = new Intent(this, DiscoverDetailedActivity.class);
                Bundle bundle01 = new Bundle();
                bundle01.putString("id", id);
                bundle01.putString("orginid", orginid);
                bundle01.putString("orgintable", orgintable);
                bundle01.putString("uid", uid);
                intent01.putExtras(bundle01);
                startActivity(intent01);
//                } else {
//                    ToastUtils.showShort(this, getResources().getString(R.string.none_pic));
//                }

                break;
        }
    }
}
