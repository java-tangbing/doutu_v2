package com.pufei.gxdt.module.user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import java.util.List;

/**
 * Created by wangwenzhang on 2016/11/9.
 */
public class PublishAdapter extends RecyclerView.Adapter<PublishAdapter.MyHodler> {
    private List<PictureResultBean.ResultBean>list;
    private Context mcontext;

    public PublishAdapter(Context context, List<PictureResultBean.ResultBean> list){//获取数据源
        this.mcontext=context;
        this.list=list;
    }
    @Override
    public MyHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.item_publish,parent,false);
        MyHodler hodler=new MyHodler(view,mListener,mListener2);
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
        if(list.get(position).getIs_show()!=null){
            int isShow = Integer.parseInt(list.get(position).getIs_show());
            if(isShow == 0){
                holder.iv_black_bg.setAlpha(0.3f);
                holder.iv_black_bg.setImageResource(R.color.black);
                Glide.with(mcontext).load(list.get(position).getUrl()).into(holder.iv1);
                holder.hot_top.setBackgroundResource(R.mipmap.user_ic_myexpression_set_invisible);
            }else{
                Glide.with(mcontext).load(list.get(position).getUrl()).into(holder.iv1);
            }
        }
    }
    @Override
    public void onViewDetachedFromWindow(MyHodler holder) {
        super.onViewDetachedFromWindow(holder);
    }
   static class MyHodler extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        private ImageView iv1,hot_top,iv_black_bg;
        public MyHodler(View itemView,MyItemClickListener myItemClickListener,MyItemLongClickListener myItemLongClickListener) {
            super(itemView);
            mListener = myItemClickListener;
            mListener2 = myItemLongClickListener;
            itemView.setOnClickListener(this);//单项
            itemView.setOnLongClickListener(this);//常按
            iv1= (ImageView) itemView.findViewById(R.id.activity_picture_item_image);
            hot_top = (ImageView) itemView.findViewById(R.id.hot_top);
            iv_black_bg = (ImageView) itemView.findViewById(R.id.iv_black_bg);
        }

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                mListener.setOnItemClickListener(itemView,v, (Integer) v.getTag());
            }
        }

       @Override
       public boolean onLongClick(View v) {
           if (mListener2!=null){
               if(v!=null){
                   mListener2.setOnItemLongClickListener(itemView,v, (Integer) v.getTag());
               }
           }
           return true;
       }
   }
    private static MyItemClickListener mListener = null;//设置点击接口
    private static MyItemLongClickListener mListener2 =null;//设置点击接口

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mListener = listener;
    }
    public void setOnItemLongClickListener(MyItemLongClickListener listener) {
        this.mListener2 = listener;
    }
    public interface MyItemClickListener {
        void setOnItemClickListener(View itemview, View view, int postion);
    }
    public interface MyItemLongClickListener {
        void setOnItemLongClickListener(View itemview, View view, int postion);
    }

   
}
