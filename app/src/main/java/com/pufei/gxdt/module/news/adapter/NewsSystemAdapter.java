package com.pufei.gxdt.module.news.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.news.bean.NewsBean;

import java.util.List;

public class NewsSystemAdapter extends BaseQuickAdapter<NewsBean.ResultBean, BaseViewHolder> {
    public NewsSystemAdapter(@Nullable List<NewsBean.ResultBean> data) {
        super(R.layout.activity_news_system_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsBean.ResultBean item) {

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
