package com.pufei.gxdt.module.news.adapter;


import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.pufei.gxdt.module.news.bean.NewsBean;

import java.util.List;


public class NewsPictureAdapter extends BaseMultiItemQuickAdapter<NewsBean.ResultBean, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public NewsPictureAdapter(List<NewsBean.ResultBean> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsBean.ResultBean item) {

    }
}
