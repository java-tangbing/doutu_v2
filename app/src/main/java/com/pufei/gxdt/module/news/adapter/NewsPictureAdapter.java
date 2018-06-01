package com.pufei.gxdt.module.news.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.pufei.gxdt.R;
import com.pufei.gxdt.module.news.bean.NewsBean;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.List;


public class NewsPictureAdapter extends BaseQuickAdapter<NewsBean.ResultBean, BaseViewHolder> {


    public NewsPictureAdapter(List<NewsBean.ResultBean> data) {
        super(R.layout.activity_news_picture_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsBean.ResultBean item) {
        helper.setText(R.id.news_picture_item_content, item.getContent())
                .setText(R.id.tv_dateLine,item.getDateline());
        GlideApp.with(mContext).load(item.getUrl())
//                .placeholder(R.mipmap.my_uer_picture)
                .into((ImageView) helper.getView(R.id.news_picture_item_url));
    }
}
