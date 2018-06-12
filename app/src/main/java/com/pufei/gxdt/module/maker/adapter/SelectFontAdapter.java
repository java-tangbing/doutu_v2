package com.pufei.gxdt.module.maker.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.List;

public class SelectFontAdapter extends BaseQuickAdapter<Integer,BaseViewHolder> {

    public SelectFontAdapter(@Nullable List<Integer> data) {
        super(R.layout.fragment_add_font_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        GlideApp.with(mContext).load(item).placeholder(R.mipmap.newloding).into((ImageView)helper.getView(R.id.iv_font_style));
    }
}
