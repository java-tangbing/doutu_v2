package com.pufei.gxdt.module.maker.adapter;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;

import java.util.List;

public class SelectColorAdapter extends BaseQuickAdapter<Integer,BaseViewHolder> {
    private int size;
    private int type;//0-文字颜色，1-画笔颜色
    private int position;
    public SelectColorAdapter(@Nullable List<Integer> data,int type) {
        super(R.layout.fragment_add_text_select_color_item,data);
        if(data != null)
            size = data.size() - 1;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        if(type == 0 ) {
            if(position == helper.getAdapterPosition()) {
                helper.setGone(R.id.iv_choose_color,true);
            }else {
                helper.setGone(R.id.iv_choose_color,false);
            }
            GradientDrawable myGrad = (GradientDrawable)helper.getView(R.id.v_color).getBackground();
            myGrad.setColor(item);
        }else {
            if(position == size) {
                helper.setGone(R.id.iv_choose_color,false);
            }else {
                if(position == helper.getAdapterPosition()) {
                    helper.setGone(R.id.iv_choose_color,true);
                }else {
                    helper.setGone(R.id.iv_choose_color,false);
                }
            }

            if(helper.getAdapterPosition() != size) {
                helper.setGone(R.id.iv_earser,false);
                GradientDrawable myGrad = (GradientDrawable)helper.getView(R.id.v_color).getBackground();
                myGrad.setColor(item);
            }else {
                helper.setGone(R.id.iv_earser,true);
            }
        }


    }

    public void setSelectState(int position) {
        this.position = position;
    }
}
