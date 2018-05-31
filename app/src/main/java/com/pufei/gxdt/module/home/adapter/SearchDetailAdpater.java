package com.pufei.gxdt.module.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.home.model.RecommendBean;
import com.pufei.gxdt.widgets.GlideApp;


import java.util.List;

/**
 * Created by wangwenzhang on 2016/11/9.
 */
public class SearchDetailAdpater extends XRecyclerView.Adapter<SearchDetailAdpater.MyHodler> {
    private List<RecommendBean.ResultBean>list;
    private Context mcontext;
    private final RequestManager glide;
    public SearchDetailAdpater(Context context, List<RecommendBean.ResultBean> list, RequestManager glide){//获取数据源
        this.mcontext=context;
        this.list=list;
        this.glide = glide;
    }
    @Override
    public MyHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.fragment_pager_itme,parent,false);
        MyHodler hodler=new MyHodler(view,mListener);
        return hodler;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public void onBindViewHolder(final MyHodler holder, final int position) {
        holder.itemView.setTag(position);
        holder.titletv.setText(list.get(position).getCategory_name());
        GlideApp.with(mcontext).load(list.get(position).getImgs().get(0).getUrl()).placeholder(R.mipmap.newloding).into(holder.iv1);
        GlideApp.with(mcontext).load(list.get(position).getImgs().get(1).getUrl()).placeholder(R.mipmap.newloding).into(holder.iv2);
        GlideApp.with(mcontext).load(list.get(position).getImgs().get(2).getUrl()).placeholder(R.mipmap.newloding).into(holder.iv3);
    }

    static class MyHodler extends XRecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titletv;
        private ImageView iv1, iv2, iv3;
        public MyHodler(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener=myItemClickListener;
            itemView.setOnClickListener(this);
            titletv = (TextView) itemView.findViewById(R.id.fragment_pager_itme_title);
            iv1 = (ImageView) itemView.findViewById(R.id.fragment_pager_item_image1);
            iv2 = (ImageView) itemView.findViewById(R.id.fragment_pager_item_image2);
            iv3 = (ImageView) itemView.findViewById(R.id.fragment_pager_item_image3);
        }

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                mListener.setOnItemClickListener(itemView,v, (Integer) v.getTag());
            }
        }
    }
    private static MyItemClickListener mListener=null;//设置点击接口
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mListener = listener;
    }
    public interface MyItemClickListener {
        void setOnItemClickListener(View itemview, View view, int postion);
    }

}
