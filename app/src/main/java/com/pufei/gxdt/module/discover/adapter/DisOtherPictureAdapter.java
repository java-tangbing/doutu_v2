package com.pufei.gxdt.module.discover.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.discover.bean.DisPicDetBean;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cw on 2018/5/31.
 */

public class DisOtherPictureAdapter extends BaseQuickAdapter<DiscoverListBean.ResultBean, BaseViewHolder> {


    public DisOtherPictureAdapter(@Nullable List<DiscoverListBean.ResultBean> data) {
        super(R.layout.item_home_picture, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscoverListBean.ResultBean item) {
//        activity_picture_item_image

        GlideApp.with(mContext).load(item.getUrl()).placeholder(R.mipmap.ic_default_picture)
                .into((ImageView) helper.getView(R.id.activity_picture_item_image));
    }
}