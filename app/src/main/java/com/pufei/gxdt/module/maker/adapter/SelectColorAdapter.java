package com.pufei.gxdt.module.maker.adapter;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;

import java.util.List;

public class SelectColorAdapter extends BaseQuickAdapter<Integer,BaseViewHolder> {
    private int size;
    public SelectColorAdapter(@Nullable List<Integer> data) {
        super(R.layout.fragment_add_text_select_color_item,data);
        if(data != null)
            size = data.size() - 1;
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        if(helper.getAdapterPosition() != size) {
            helper.setGone(R.id.iv_earser,false);
            GradientDrawable myGrad = (GradientDrawable)helper.getView(R.id.v_color).getBackground();
            myGrad.setColor(item);
        }else {
            helper.setGone(R.id.iv_earser,true);
        }

    }
}
