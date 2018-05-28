package com.pufei.gxdt.module.discover.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.List;

public class DiscoverAdapter extends BaseQuickAdapter<DiscoverListBean.ResultBean, BaseViewHolder> {
    public DiscoverAdapter(@Nullable List<DiscoverListBean.ResultBean> data) {
        super(R.layout.fragment_discover_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscoverListBean.ResultBean item) {
//        helper.setText(R.id.dis_item_iv);
        GlideApp.with(mContext).load(item.getUrl()).into((ImageView) helper.getView(R.id.dis_item_iv));
        GlideApp.with(mContext).load(item.getUser().getHeader()).into((ImageView) helper.getView(R.id.dis_item_user_header_iv));
        helper.setText(R.id.dis_item_user_name_tv, item.getUser().getUsername())
                .setText(R.id.dis_item_hot_tv, item.getHot());
    }
}
