package com.pufei.gxdt.module.news.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.news.bean.NewsBean;
import com.pufei.gxdt.module.news.bean.NewsTypeTwoBean;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsSystemAdapter extends BaseQuickAdapter<NewsTypeTwoBean.ResultBean, BaseViewHolder> {
    private boolean isbdphone;

    public NewsSystemAdapter(@Nullable List<NewsTypeTwoBean.ResultBean> data, boolean isbdphone) {
        super(R.layout.activity_news_system_item, data);
        this.isbdphone = isbdphone;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsTypeTwoBean.ResultBean item) {
//        if (isbdphone) {
//            helper.setGone(R.id.new_system_item_bd, true);
//        } else {
//            helper.setVisible(R.id.new_system_item_bd, true);
//        }
        helper
//                .addOnClickListener(R.id.new_system_item_bd)
                .setText(R.id.news_system_item_dateline_tv, item.getDateline())
                .setText(R.id.news_system_item_content_tv, item.getContent());
//        GlideApp.with(mContext).load(item.getUrl())
//                .placeholder(R.mipmap.my_uer_picture)
//                .into((CircleImageView) helper.getView(R.id.news_system_item_cv));
    }

}
