package com.pufei.gxdt.module.news.activity.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;

import java.util.List;

public class NewsSystemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public NewsSystemAdapter(@Nullable List<String> data) {
        super(R.layout.activity_news_system_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
