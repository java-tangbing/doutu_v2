package com.pufei.gxdt.module.discover.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.discover.bean.DisWorksBean;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.List;

public class DisWorksdAdapter extends BaseQuickAdapter<DisWorksBean.ResultBean.ProductBean, BaseViewHolder> {
    public DisWorksdAdapter(@Nullable List<DisWorksBean.ResultBean.ProductBean> data) {
        super(R.layout.activity_disworks_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DisWorksBean.ResultBean.ProductBean item) {
        GlideApp.with(mContext).load(item.getUrl())
                .placeholder(R.mipmap.newloding)
                .error(R.mipmap.ic_default_picture)
                .fallback(R.mipmap.ic_default_picture)
                .into((ImageView) helper.getView(R.id.dis_wrok_item_iv));

    }
}
