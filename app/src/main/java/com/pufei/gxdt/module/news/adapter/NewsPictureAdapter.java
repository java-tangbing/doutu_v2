package com.pufei.gxdt.module.news.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.news.bean.NewsBean;

import java.util.List;

public class NewsPictureAdapter extends BaseMultiItemQuickAdapter<NewsBean, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public NewsPictureAdapter(List data) {
        super(data);
        addItemType(1101, R.layout.activity_news_picture_item);
        addItemType(1102, R.layout.activity_news_item_unlanded);
    }


    @Override
    protected void convert(BaseViewHolder helper, NewsBean item) {
        switch (helper.getItemViewType()) {
            case 1101:
                helper.addOnClickListener(R.id.news_picture_item);
//                helper.setImageUrl(R.id.tv, item.getContent());
                break;
            case 1102:
//                helper.setImageUrl(R.id.iv, item.getContent());
                break;
        }
    }
}
