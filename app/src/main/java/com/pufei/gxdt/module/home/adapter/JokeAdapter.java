package com.pufei.gxdt.module.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.home.model.JokeResultBean;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.List;

public class JokeAdapter extends XRecyclerView.Adapter<XRecyclerView.ViewHolder> {
    private List<JokeResultBean.ResultBean> list;
    private Context mcontext;
    private int FIST_IMAGE = 1;
    private int THREE_IMAGE = 3;
    public JokeAdapter(Context context, List<JokeResultBean.ResultBean> list) {//获取数据源
        this.mcontext = context;
        this.list = list;
    }

    @Override
    public XRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==THREE_IMAGE){
            View view = LayoutInflater.from(mcontext).inflate(R.layout.fragment_joke_item1, parent, false);
            MyHodler hodler = new MyHodler(view, mListener);
            return hodler;
        }else if (viewType==FIST_IMAGE){
            View view=LayoutInflater.from(mcontext).inflate(R.layout.fragment_joke_item2,parent,false);
            OneHolder holder=new OneHolder(view,mListener);
            return holder;
           /// TODO: 2017/2/23  
        }

        return null;
    }

  
    @Override
    public int getItemViewType(int position) {
        return list.get(position).getImages().size();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(XRecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        if (holder instanceof MyHodler){
            ((MyHodler) holder).tv_eyes.setText(list.get(position).getView());
            ((MyHodler) holder).tv_hot.setText(list.get(position).getHot());
            ((MyHodler) holder).titletv.setText(list.get(position).getTitle());
            GlideApp.with(mcontext).load(list.get(position).getImages().get(0)).placeholder(R.mipmap.ic_default_picture).into(((MyHodler) holder).iv1);
            GlideApp.with(mcontext).load(list.get(position).getImages().get(1)).placeholder(R.mipmap.ic_default_picture).into(((MyHodler) holder).iv2);
            GlideApp.with(mcontext).load(list.get(position).getImages().get(2)).placeholder(R.mipmap.ic_default_picture).into(((MyHodler) holder).iv3);
        }else if(holder instanceof OneHolder){
            ((OneHolder) holder).tv2.setText(list.get(position).getTitle());
            ((OneHolder) holder).tv_eyes.setText(list.get(position).getView());
            ((OneHolder) holder).tv_hot.setText(list.get(position).getHot());
            GlideApp.with(mcontext).load(list.get(position).getImages().get(0)).placeholder(R.mipmap.ic_default_picture).into(((OneHolder) holder).iv11);
        }
    }

    class MyHodler extends XRecyclerView.ViewHolder implements View.OnClickListener {
        private TextView  titletv,tv_eyes,tv_hot;
        private ImageView iv1, iv2, iv3;

        public MyHodler(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener = myItemClickListener;
            titletv = (TextView) itemView.findViewById(R.id.frgament_second_joke_tiltletv);
            iv1 = (ImageView) itemView.findViewById(R.id.fragment_second_joke_image1);
            iv2 = (ImageView) itemView.findViewById(R.id.fragment_second_joke_image2);
            iv3 = (ImageView) itemView.findViewById(R.id.fragment_second_joke_image3);
            tv_eyes = (TextView) itemView.findViewById(R.id.tv_eyes);
            tv_hot = (TextView) itemView.findViewById(R.id.tv_hot);
            itemView.setOnClickListener(this);//单项点击
        }
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.setOnItemClickListener(itemView, v, (Integer) v.getTag());
            }
        }
    }

    class OneHolder extends XRecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv2,tv_eyes,tv_hot;
        private ImageView iv11;
        public OneHolder(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener=myItemClickListener;
            tv2 = (TextView) itemView.findViewById(R.id.fragment_joke_itme2_content);
            tv_eyes = (TextView) itemView.findViewById(R.id.tv_eyes);
            tv_hot = (TextView) itemView.findViewById(R.id.tv_hot);
            iv11 = (ImageView) itemView.findViewById(R.id.fragment_joke_itme2_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.setOnItemClickListener(itemView, v, (Integer) v.getTag());
            }
        }
    }
    private  MyItemClickListener mListener = null;//设置点击接口

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mListener = listener;
    }

    public interface MyItemClickListener {
        void setOnItemClickListener(View itemview, View view, int postion);

    }

}
