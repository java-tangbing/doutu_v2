package com.pufei.gxdt.module.maker.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.maker.bean.MaterialBean;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.List;

public class StickerImageAdapter extends BaseQuickAdapter<MaterialBean.ResultBean,BaseViewHolder> {

    public StickerImageAdapter(@Nullable List<MaterialBean.ResultBean> data) {
        super(R.layout.fragment_featured_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MaterialBean.ResultBean item) {
        GlideApp.with(mContext).load(item.getImage()).placeholder(R.mipmap.newloding).into((ImageView)helper.getView(R.id.iv_sticker));
    }
}
