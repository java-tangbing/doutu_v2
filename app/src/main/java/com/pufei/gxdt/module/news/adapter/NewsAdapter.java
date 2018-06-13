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
        //1=系统消息 2=斗图小助手 3=意见反馈
        switch (item.getType()) {
            case "1":
                GlideApp.with(mContext).load(R.mipmap.news_ic_system).into((ImageView) helper.getView(R.id.news_item_icon_cv));
                break;
            case "2":
                GlideApp.with(mContext).load(R.mipmap.news_ic_assistant).into((ImageView) helper.getView(R.id.news_item_icon_cv));
                break;
            case "3":
                GlideApp.with(mContext).load(R.mipmap.news_ic_opinion).into((ImageView) helper.getView(R.id.news_item_icon_cv));
                break;
        }


    }
}
