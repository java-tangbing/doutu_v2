package com.pufei.gxdt.module.home.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.module.home.activity.PictureDetailActivity;
import com.pufei.gxdt.module.home.fragment.DouTuFragment;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.widgets.GlideApp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangwenzhang on 2016/11/9.
 */
public class ImageTypeAdapter extends RecyclerView.Adapter<ImageTypeAdapter.MyHodler> {
    private List<PictureResultBean.ResultBean>list;
    private Context mcontext;
    private DouTuFragment fragment;

    public ImageTypeAdapter(Context context, DouTuFragment fragment, List<PictureResultBean.ResultBean> list){//获取数据源
        this.mcontext=context;
        this.list=list;
        this.fragment = fragment;
    }
    @Override
    public MyHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.item_hot,parent,false);
        MyHodler hodler=new MyHodler(view);
        return hodler;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public void onBindViewHolder(final MyHodler holder, final int position) {
        GlideApp.with(mcontext).load(list.get(position).getUrl()).placeholder(R.mipmap.ic_default_picture).override(100,80).into(holder.iv1);
        holder.iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, PictureDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("picture_index", position);
                bundle.putSerializable("picture_list", (Serializable) list);
                intent.putExtras(bundle);
                fragment.startActivityForResult(intent,1);
            }
        });
    }
    @Override
    public void onViewDetachedFromWindow(MyHodler holder) {
        super.onViewDetachedFromWindow(holder);
    }
    class MyHodler extends RecyclerView.ViewHolder {
        private ImageView iv1;
        public MyHodler(View itemView) {
            super(itemView);
            iv1= (ImageView) itemView.findViewById(R.id.activity_picture_item_image);
        }


    }


}
