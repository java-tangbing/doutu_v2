package com.pufei.gxdt.module.user.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.db.DraftInfo;
import com.pufei.gxdt.widgets.GlideApp;

import java.io.File;
import java.util.List;

public class DraftAdapter extends BaseQuickAdapter<DraftInfo,BaseViewHolder> {
    public DraftAdapter(@Nullable List<DraftInfo> data) {
        super(R.layout.activity_draft_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DraftInfo item) {

        if(item.imagePath.contains("http:") || item.imagePath.contains("https:")) {
            GlideApp.with(mContext).load(item.imagePath).into((ImageView) helper.getView(R.id.iv_preview));
        }else {
            GlideApp.with(mContext).load(new File(item.imagePath)).into((ImageView) helper.getView(R.id.iv_preview));
        }

        helper.addOnClickListener(R.id.iv_edit)
                .addOnClickListener(R.id.tv_publish);
    }
}
