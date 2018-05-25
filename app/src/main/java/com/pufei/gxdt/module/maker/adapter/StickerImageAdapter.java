package com.pufei.gxdt.module.maker.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.List;

public class StickerImageAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public StickerImageAdapter(@Nullable List<String> data) {
        super(R.layout.fragment_featured_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        GlideApp.with(mContext).load(item).placeholder(R.mipmap.ic_launcher).into((ImageView)helper.getView(R.id.iv_sticker));
    }
}
