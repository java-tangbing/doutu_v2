package com.pufei.gxdt.module.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.commonality.model.PictureBeanRe;
import com.pufei.gxdt.util.AddHeader;

import java.util.List;

/**
 * Created by wangwenzhang on 2016/11/9.
 */
public class HotAdapter extends RecyclerView.Adapter<HotAdapter.MyHodler> {
    private List<PictureBeanRe.ResultBean>list;
    private Context mcontext;
    private int a=0;
    private Boolean is=false;
    private final RequestManager glide;
    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public Boolean getIs() {
        return is;
    }

    public void setIs(Boolean is) {
        this.is = is;
    }

    public HotAdapter(Context context, List<PictureBeanRe.ResultBean> list, RequestManager glide){//获取数据源
        this.mcontext=context;
        this.list=list;
        this.glide = glide;
    }
    @Override
    public MyHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.activity_picture_item,parent,false);
        MyHodler hodler=new MyHodler(view,mListener);
        return hodler;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public void onBindViewHolder(final MyHodler holder, final int position) {
       // holder.iv2.setTag(position);
        holder.itemView.setTag(position);
       /* Glide.with(mcontext).load(list.get(position).getUrl()).asBitmap().placeholder(R.mipmap.newloding)
                .diskCacheStrategy(DiskCacheStrategy.NONE).transform(new GlideRoundTransform(mcontext,5)).fitCenter()
                .thumbnail(0.1f
        ).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                holder.iv1.setImageBitmap(resource);
                PictureActivity.gridviewBitmapCaches.put(list.get(position).getUrl(),resource);
            }
        });*/
       /* HttpPost httpPost=new HttpPost(list.get(position).getUrl());
        httpPost.addHeader();*/
        glide.load(AddHeader.buildGlideUrl(list.get(position).getUrl())).crossFade().placeholder(R.mipmap.newloding).override(100,80)
                .diskCacheStrategy(DiskCacheStrategy.RESULT).fitCenter().dontAnimate()
                .into(holder.iv1);
       /* if (a==0){
            holder.iv2.setVisibility(View.GONE);
        }else {
            holder.iv2.setVisibility(View.VISIBLE);
        }*/
        //releaseImageViewResouce(holder.iv1);
    }
    @Override
    public void onViewDetachedFromWindow(MyHodler holder) {
        super.onViewDetachedFromWindow(holder);
        Glide.clear(holder.iv1);
    }
   static class MyHodler extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView iv1;
        private CheckBox  iv2;
        public MyHodler(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener=myItemClickListener;
            itemView.setOnClickListener(this);//单项
            iv1= (ImageView) itemView.findViewById(R.id.activity_picture_item_image);
           /* iv2= (CheckBox) itemView.findViewById(R.id.fragmen_my_frist_delete);
            iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iv2.isChecked()){
                        mListener.onDelete((Integer) iv2.getTag());
                    }else {
                        mListener.onAdd((Integer) iv2.getTag());
                    }

                }
            });
*/
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
        void onDelete(int position);
        void onAdd(int position);
    }
   /* private OnCountClickListener onCountClickListener;
    public void setOnCountClickListener(OnCountClickListener listener){
        this.onCountClickListener=listener;
    }
    public interface OnCountClickListener{
        void OnLike(int position);
        void OnBtDelete(int position);
    }*/
   public void releaseImageViewResouce(ImageView imageView) {
       if (imageView == null) return;
       Drawable drawable = imageView.getDrawable();
       if (drawable != null && drawable instanceof BitmapDrawable) {
           BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
           Bitmap bitmap = bitmapDrawable.getBitmap();
           if (bitmap != null && !bitmap.isRecycled()) {
               bitmap.recycle();
               System.gc();
           }
       }
   }
}
