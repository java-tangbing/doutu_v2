package com.pufei.gxdt.module.maker.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseView;

import java.util.List;

public class HotTextAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public HotTextAdapter(@Nullable List<String> data) {
        super(R.layout.fragment_addtext_pop_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_hot,item);
    }
}
