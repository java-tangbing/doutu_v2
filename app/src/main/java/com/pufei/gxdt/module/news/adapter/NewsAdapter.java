package com.pufei.gxdt.module.news.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.news.bean.NoticeBean;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.List;

public class NewsAdapter extends BaseQuickAdapter<NoticeBean.ResultBean, BaseViewHolder> {
    public NewsAdapter(@Nullable List<NoticeBean.ResultBean> data) {
        super(R.layout.activity_news_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeBean.ResultBean item) {
        helper.addOnClickListener(R.id.news_item_message)
                .setText(R.id.news_item_dateline_tv, item.getDateline())
                .setText(R.id.news_item_title_tv, item.getTitle())
                .setText(R.id.news_item_content_tv, item.getContent());
        GlideApp.with(mContext).load(R.mipmap.ic_launcher).into((ImageView) helper.getView(R.id.news_item_icon_cv));

    }
}
