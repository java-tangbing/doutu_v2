package com.pufei.gxdt.module.user.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.home.model.JokeResultBean;
import com.pufei.gxdt.module.user.bean.MyImagesBean;
import com.pufei.gxdt.utils.AddHeader;
import com.pufei.gxdt.widgets.GlideApp;
import com.pufei.gxdt.widgets.MyFrontTextView;

import java.util.List;

public class FavoriteAdapter extends XRecyclerView.Adapter<XRecyclerView.ViewHolder> {
    private List<MyImagesBean.ResultBean> list;
    private Context mcontext;
    private int IMAGE_SIZE ;
    public FavoriteAdapter(Context context, List<MyImagesBean.ResultBean> list,int IMAGE_SIZE) {//获取数据源
        this.mcontext = context;
        this.list = list;
        this.IMAGE_SIZE = IMAGE_SIZE;
    }

    @Override
    public XRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (IMAGE_SIZE == 3){
            View view = LayoutInflater.from(mcontext).inflate(R.layout.fragment_joke_item1, parent, false);
            MyHodler hodler = new MyHodler(view, mListener);
            return hodler;
        }else {
            View view=LayoutInflater.from(mcontext).inflate(R.layout.item_hot,parent,false);
            OneHolder holder=new OneHolder(view,mListener);
            return holder;
           /// TODO: 2017/2/23
        }

    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(XRecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        if (holder instanceof MyHodler){
            ((MyHodler) holder).titletv.setText(list.get(position).getCategory_name());
            ((MyHodler) holder).tv_eyes.setText(list.get(position).getView());
            ((MyHodler) holder).tv_hot.setText(list.get(position).getHot());
            GlideApp.with(mcontext).load(list.get(position).getImgs().get(0).getUrl()).placeholder(R.mipmap.ic_default_picture).into(((MyHodler) holder).iv1);
            GlideApp.with(mcontext).load(list.get(position).getImgs().get(1).getUrl()).placeholder(R.mipmap.ic_default_picture).into(((MyHodler) holder).iv2);
            GlideApp.with(mcontext).load(list.get(position).getImgs().get(2).getUrl()).placeholder(R.mipmap.ic_default_picture).into(((MyHodler) holder).iv3);
        }else if(holder instanceof OneHolder){
            GlideApp.with(mcontext).load(list.get(position).getUrl()).placeholder(R.mipmap.ic_default_picture).into(((OneHolder) holder).iv11);
        }
    }

    class MyHodler extends XRecyclerView.ViewHolder implements View.OnClickListener {
        private TextView   titletv,tv_eyes,tv_hot;
        private ImageView iv1, iv2, iv3;

        public MyHodler(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener = myItemClickListener;
            titletv = (TextView) itemView.findViewById(R.id.frgament_second_joke_tiltletv);
            tv_eyes = (TextView) itemView.findViewById(R.id.tv_eyes);
            tv_hot = (TextView) itemView.findViewById(R.id.tv_hot);
            iv1 = (ImageView) itemView.findViewById(R.id.fragment_second_joke_image1);
            iv2 = (ImageView) itemView.findViewById(R.id.fragment_second_joke_image2);
            iv3 = (ImageView) itemView.findViewById(R.id.fragment_second_joke_image3);
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
        private ImageView iv11;
        public OneHolder(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener=myItemClickListener;
            iv11= (ImageView) itemView.findViewById(R.id.activity_picture_item_image);
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
        void OnLike(int position);

        void OnBtDelete(int position);
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
