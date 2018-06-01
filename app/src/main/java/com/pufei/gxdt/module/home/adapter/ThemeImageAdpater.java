package com.pufei.gxdt.module.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.home.model.ThemeResultBean;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.List;

/**
 * Created by wangwenzhang on 2016/11/9.
 */
public class ThemeImageAdpater extends XRecyclerView.Adapter<XRecyclerView.ViewHolder> {
    private List<ThemeResultBean.ResultBean> list;
    private Context mcontext;
    public ThemeImageAdpater(Context context, List<ThemeResultBean.ResultBean> list){//获取数据源
        this.mcontext=context;
        this.list=list;
    }
    @Override
    public XRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(mcontext).inflate(R.layout.item_theme_image,parent,false);
            MyHodler hodler=new MyHodler(view,mListener);
            return hodler;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public void onBindViewHolder(XRecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        if (holder instanceof MyHodler){
            ((MyHodler)holder).titletv.setText(list.get(position).getCategory_name());
            ((MyHodler)holder).tv_desc.setText(list.get(position).getDesc());
            GlideApp.with(mcontext).load(list.get(position).getImgs().get(0).getUrl()).placeholder(R.mipmap.newloding).into(((MyHodler)holder).iv1);
            GlideApp.with(mcontext).load(list.get(position).getImgs().get(1).getUrl()).placeholder(R.mipmap.newloding).into(((MyHodler)holder).iv2);
            GlideApp.with(mcontext).load(list.get(position).getImgs().get(2).getUrl()).placeholder(R.mipmap.newloding).into(((MyHodler)holder).iv3);
        }else if (holder instanceof AdvertHodler){
        }

    }

    static class MyHodler extends XRecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titletv,tv_desc;
        private ImageView iv1, iv2, iv3;
        public MyHodler(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener=myItemClickListener;
            itemView.setOnClickListener(this);
            titletv = (TextView) itemView.findViewById(R.id.fragment_pager_itme_title);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
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
    static class AdvertHodler extends XRecyclerView.ViewHolder{
        private ImageView image;
        public AdvertHodler(final View itemView, MyItemClickListener listener) {
            super(itemView);
            //image= (ImageView) itemView.findViewById(R.id.fragment_pager_advert_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.setOnItemClickListener(itemView,v, (Integer) v.getTag());
                }
            });
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
