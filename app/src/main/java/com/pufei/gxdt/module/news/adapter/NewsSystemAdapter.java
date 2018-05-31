package com.pufei.gxdt.module.news.adapter;

import android.opengl.Visibility;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.module.news.bean.NewsBean;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsSystemAdapter extends BaseQuickAdapter<NewsBean.ResultBean, BaseViewHolder> {
    public NewsSystemAdapter(@Nullable List<NewsBean.ResultBean> data) {
        super(R.layout.activity_news_system_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsBean.ResultBean item) {
        if (App.userBean.getPhone().length() > 0) {
            helper.setGone(R.id.new_system_item_bd, true);
        } else {
            helper.setVisible(R.id.new_system_item_bd, true);
        }
        helper.addOnClickListener(R.id.new_system_item_bd)
                .setText(R.id.news_system_item_dateline_tv, item.getDateline())
                .setText(R.id.news_system_item_content_tv, item.getContent());
        GlideApp.with(mContext).load(item.getUrl())
                .placeholder(R.mipmap.my_uer_picture)
                .into((CircleImageView) helper.getView(R.id.news_system_item_cv));
    }


//    public NewsSystemAdapter(List data) {
//        super(R.id.activity_news_system_item,data);
////        addItemType(1101, R.layout.activity_news_item_unlanded);
////        addItemType(1102, R.layout.activity_news_system_item);
//    }
//
//
//    @Override
//    protected void convert(BaseViewHolder helper, NewsBean item) {
//        switch (helper.getItemViewType()) {
//            case 1101:
////                helper.setImageUrl(R.id.tv, item.getContent());
//                break;
//            case 1102:
////                helper.setImageUrl(R.id.iv, item.getContent());
//                break;
//        }
//    }
}
