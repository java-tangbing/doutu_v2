package com.pufei.gxdt.module.news.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.news.bean.NewsBean;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsFeedBackAdapter extends BaseMultiItemQuickAdapter<NewsBean.ResultBean, BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public NewsFeedBackAdapter(List<NewsBean.ResultBean> data) {
        super(data);
        addItemType(1, R.layout.activity_news_feedback_item_user);
        addItemType(0, R.layout.activity_news_feedback_item_server);
    }


    @Override
    protected void convert(BaseViewHolder helper, NewsBean.ResultBean item) {
        switch (helper.getItemViewType()) {
            case 1:
                helper.setText(R.id.news_feedback_item_user_dateline_tv, item.getDateline())
                        .setText(R.id.news_feedback_item_user_content_tv, item.getContent());
                GlideApp.with(mContext).load(item.getUrl())
                        .placeholder(R.mipmap.my_uer_picture)
                        .into((CircleImageView) helper.getView(R.id.news_feedback_item_user_icon_cv));
                break;
            case 0:
                helper.setText(R.id.news_feedback_item_server_dateline_tv, item.getDateline())
                        .setText(R.id.news_feedback_item_server_content_tv, item.getContent());
                GlideApp.with(mContext).load(item.getUrl())
                        .placeholder(R.mipmap.my_uer_picture)
                        .into((CircleImageView) helper.getView(R.id.news_feedback_item_server_icon_cv));
                break;
        }

    }
}
