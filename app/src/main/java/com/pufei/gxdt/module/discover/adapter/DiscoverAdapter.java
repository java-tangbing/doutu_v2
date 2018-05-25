package com.pufei.gxdt.module.discover.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;

import java.util.List;

public class DiscoverAdapter extends BaseQuickAdapter<DiscoverListBean, BaseViewHolder> {
    public DiscoverAdapter(@Nullable List<DiscoverListBean> data) {
        super(R.layout.fragment_discover_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscoverListBean item) {

    }
}
