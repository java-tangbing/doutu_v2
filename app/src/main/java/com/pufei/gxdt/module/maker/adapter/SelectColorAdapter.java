package com.pufei.gxdt.module.maker.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;

import java.util.List;

public class SelectColorAdapter extends BaseQuickAdapter<Integer,BaseViewHolder> {

    public SelectColorAdapter(@Nullable List<Integer> data) {
        super(R.layout.fragment_add_text_select_color_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        GradientDrawable myGrad = (GradientDrawable)helper.getView(R.id.v_color).getBackground();
        myGrad.setColor(item);
    }
}
