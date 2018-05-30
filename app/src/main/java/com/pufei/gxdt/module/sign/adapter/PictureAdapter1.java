package com.pufei.gxdt.module.sign.adapter;

import android.content.Context;
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
import com.pufei.gxdt.module.sign.model.PictureBeanRe;

import java.io.File;
import java.util.List;

/**
 * Created by wangwenzhang on 2016/11/9.
 */
public class PictureAdapter1 extends RecyclerView.Adapter<PictureAdapter1.MyHodler> {
    private List<PictureBeanRe.ResultBean> list;
    private Context mcontext;
    private int a = 0;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public PictureAdapter1(Context context, List<PictureBeanRe.ResultBean> list) {//获取数据源
        this.mcontext = context;
        this.list = list;
    }

    @Override
    public MyHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.fragment_myfrist_item, parent, false);
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
        holder.iv2.setTag(position);
        if (a == 0) {
            holder.iv2.setVisibility(View.GONE);
        } else {
            holder.iv2.setVisibility(View.VISIBLE);
            holder.iv2.setChecked(false);
        }
        if (list.get(position).getType() != 0) {
            holder.tongbu.setVisibility(View.VISIBLE);
        } else {
            holder.tongbu.setVisibility(View.GONE);
        }

        String url = list.get(position).getUrl();
        if (mcontext != null) {
//            if (url.contains("http")) {
//                Glide.with(mcontext).load(url).diskCacheStrategy(DiskCacheStrategy.NONE).fitCenter().placeholder(R.mipmap.newloding).into(holder.iv1);
//            } else {
//                Glide.with(mcontext).load(new File(url)).diskCacheStrategy(DiskCacheStrategy.NONE).fitCenter().placeholder(R.mipmap.newloding).into(holder.iv1);
//            }
        }
    }

    public static void loadImage(RequestManager glide, String url, ImageView imageView) {
//        glide.load(url).diskCacheStrategy(DiskCacheStrategy.NONE).fitCenter().placeholder(R.mipmap.newloding).into(imageView);
    }

    static class MyHodler extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv1, tongbu;
        private CheckBox iv2;

        public MyHodler(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener = myItemClickListener;
            itemView.setOnClickListener(this);//单项
            iv1 = (ImageView) itemView.findViewById(R.id.fragment_picture_item_image);
            iv2 = (CheckBox) itemView.findViewById(R.id.fragmen_my_frist_delete);
            tongbu = (ImageView) itemView.findViewById(R.id.fragment_myfrist_tongbu);
            iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iv2.isChecked()) {
                        mListener.onAdd((Integer) iv2.getTag());
                    } else {
                        mListener.onDelete((Integer) iv2.getTag());
                    }

                }
            });
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.setOnItemClickListener(itemView, v, (Integer) v.getTag());
            }
        }
    }

    private static MyItemClickListener mListener = null;//设置点击接口

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mListener = listener;
    }

    public interface MyItemClickListener {
        void setOnItemClickListener(View itemview, View view, int postion);

        void onAdd(int position);

        void onDelete(int position);
    }
   /* private OnCountClickListener onCountClickListener;
    public void setOnCountClickListener(OnCountClickListener listener){
        this.onCountClickListener=listener;
    }
    public interface OnCountClickListener{
        void OnLike(int position);
        void OnBtDelete(int position);
    }*/
}
