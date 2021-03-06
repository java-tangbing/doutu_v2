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
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.List;

/**
 * Created by wangwenzhang on 2016/11/9.
 */
public class HotAdapter extends RecyclerView.Adapter<HotAdapter.MyHodler> {
    private List<PictureResultBean.ResultBean>list;
    private Context mcontext;
    boolean isTop = false;
    public HotAdapter(Context context, List<PictureResultBean.ResultBean> list,boolean isTop){//获取数据源
        this.mcontext=context;
        this.list=list;
        this.isTop = isTop;
    }
    public HotAdapter(Context context, List<PictureResultBean.ResultBean> list){//获取数据源
        this.mcontext=context;
        this.list=list;
    }
    @Override
    public MyHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.item_hot,parent,false);
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
        holder.setIsRecyclable(false);
        if(isTop){
                if(position == 0){
                    holder.hot_top.setBackgroundResource(R.mipmap.ic_expression_top1);
                }else if(position == 1){
                    holder.hot_top.setBackgroundResource(R.mipmap.ic_expression_top2);
                }else if(position == 2){
                    holder.hot_top.setBackgroundResource(R.mipmap.ic_expression_top3);
                }else{

                }
        }
        GlideApp.with(mcontext).load(list.get(position).getUrl()).placeholder(R.mipmap.ic_default_picture).override(100,80).into(holder.iv1);

    }
    @Override
    public void onViewDetachedFromWindow(MyHodler holder) {
        super.onViewDetachedFromWindow(holder);
    }
   class MyHodler extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView iv1,hot_top;
        public MyHodler(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener=myItemClickListener;
            itemView.setOnClickListener(this);//单项
            iv1= (ImageView) itemView.findViewById(R.id.activity_picture_item_image);
            hot_top = (ImageView) itemView.findViewById(R.id.hot_top);
        }

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                mListener.setOnItemClickListener(itemView,v, (Integer) v.getTag());
            }
        }
    }
    private  MyItemClickListener mListener=null;//设置点击接口
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mListener = listener;
    }
    public interface MyItemClickListener {
        void setOnItemClickListener(View itemview, View view, int postion);
    }

}
