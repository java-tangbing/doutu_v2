package com.pufei.gxdt.module.news.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.news.bean.NewsBean;

import java.util.List;

public class NewsFeedBackAdapter extends BaseMultiItemQuickAdapter<NewsBean.ResultBean, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public NewsFeedBackAdapter(List<NewsBean.ResultBean> data) {
        super(data);
        addItemType(0, R.layout.activity_news_feedback_item_user);
        addItemType(1, R.layout.activity_news_feedback_item_server);
    }


    @Override
    protected void convert(BaseViewHolder helper, NewsBean.ResultBean item) {
        switch (helper.getItemViewType()) {
            case 1101:
//                helper.setImageUrl(R.id.tv, item.getContent());
                break;
            case 1102:
//                helper.setImageUrl(R.id.iv, item.getContent());
                break;
        }
    }
}
