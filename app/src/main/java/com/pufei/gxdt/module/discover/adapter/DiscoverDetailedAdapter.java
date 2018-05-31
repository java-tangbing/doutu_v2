package com.pufei.gxdt.module.discover.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.discover.bean.DiscoverEditImageBean;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiscoverDetailedAdapter extends BaseQuickAdapter<DiscoverEditImageBean.ResultBean.DataBean, BaseViewHolder> {
    public DiscoverDetailedAdapter(@Nullable List<DiscoverEditImageBean.ResultBean.DataBean> data) {
        super(R.layout.fragment_discover_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscoverEditImageBean.ResultBean.DataBean item) {
        GlideApp.with(mContext).load(item.getUrl())
                .placeholder(R.mipmap.newloding)
                .error(R.mipmap.ic_default_picture)
                .fallback(R.mipmap.ic_default_picture)
                .into((ImageView) helper.getView(R.id.dis_item_iv));
        GlideApp.with(mContext).load(item.getUser().getHeader())
                .placeholder(R.mipmap.newloding)
                .error(R.mipmap.my_uer_picture)
                .fallback(R.mipmap.my_uer_picture)
                .into((CircleImageView) helper.getView(R.id.dis_item_user_header_iv));
        if (item.getUser().getUsername().length() > 0 || !item.getUser().getUsername().isEmpty()) {
            helper.setText(R.id.dis_item_user_name_tv, item.getUser().getUsername());
        }
        if (item.getHot().length() > 0 || !item.getHot().isEmpty()) {
            helper.setText(R.id.dis_item_hot_tv, item.getHot());
        }
    }
}
