package com.pufei.gxdt.module.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangbing on 2018/4/26.
 */

public class OtherPictureAdapter extends RecyclerView.Adapter<OtherPictureAdapter.MyHodler> {
    private List<PictureResultBean.ResultBean> list = new ArrayList<>();
    private Context mcontext;
    public OtherPictureAdapter(Context context, List<PictureResultBean.ResultBean> list) {//获取数据源
        this.mcontext = context;
        this.list = list;
    }

    @Override
    public MyHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_hot, parent, false);
        MyHodler hodler = new MyHodler(view, mListener);
        return hodler;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final MyHodler holder, final int position) {
        holder.itemView.setTag(position);
        GlideApp.with(mcontext).load(list.get(position).getUrl()).placeholder(R.mipmap.ic_default_picture).into( holder.activity_picture_item_image);
    }

    class MyHodler extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView activity_picture_item_image;

        public MyHodler(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener = myItemClickListener;
            activity_picture_item_image = (ImageView) itemView.findViewById(R.id.activity_picture_item_image);
            itemView.setOnClickListener(this);//单项点击
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.setOnItemClickListener(itemView, v, (Integer) v.getTag());
            }
        }
    }

    private MyItemClickListener mListener = null;//设置点击接口

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mListener = listener;
    }

    public interface MyItemClickListener {
        void setOnItemClickListener(View itemview, View view, int postion);
    }

}