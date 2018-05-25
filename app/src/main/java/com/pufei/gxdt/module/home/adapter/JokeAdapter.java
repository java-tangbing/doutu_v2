package com.pufei.gxdt.module.home.adapter;

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
import com.pufei.gxdt.utils.AddHeader;
import com.pufei.gxdt.utils.LogUtils;
import com.pufei.gxdt.widgets.MyFrontTextView;

import java.util.List;

public class JokeAdapter extends XRecyclerView.Adapter<XRecyclerView.ViewHolder> {
    private List<JokeResultBean.ResultBean> list;
    private Context mcontext;
    private int FIST_IMAGE = 1;
    private int THREE_IMAGE = 3;
    private int ZERO_IMAGE=0;
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
        }else if(viewType==ZERO_IMAGE){
            View view=LayoutInflater.from(mcontext).inflate(R.layout.fragment_second_advert,parent,false);
            AdvertHodler hodler=new AdvertHodler(view,mListener);
            return hodler;
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
            ((MyHodler) holder).titletv.setText(list.get(position).getTitle());
            Glide.with(mcontext)
                    .load(list.get(position).getImages().get(0))
                    .into(((MyHodler) holder).iv1);
//            glide.load(list.get(position).getImages().get(0)).
//                    placeholder(R.mipmap.loading).fitCenter()
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).
//                   crossFade().into(((MyHodler) holder).iv1);
//            glide.load(list.get(position).getImages().get(1)).
//                    placeholder(R.mipmap.loading).fitCenter()
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).
//                   crossFade().into(((MyHodler) holder).iv2);
//            glide.load(list.get(position).getImages().get(2)).
//                    placeholder(R.mipmap.loading).fitCenter()
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).
//                    crossFade().into(((MyHodler) holder).iv3);
            Glide.with(mcontext)
                    .load(list.get(position).getImages().get(1))
                    .into(((MyHodler) holder).iv2);
            Glide.with(mcontext)
                    .load(list.get(position).getImages().get(2))
                    .into(((MyHodler) holder).iv3);
        }else if(holder instanceof OneHolder){
            ((OneHolder) holder).tv2.setText(list.get(position).getTitle());
//            glide.load(list.get(position).getImages().get(0)).
//                    placeholder(R.mipmap.loading).fitCenter()
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).
//                   crossFade().into(((OneHolder) holder).iv11);
            Glide.with(mcontext)
                    .load(list.get(position).getImages().get(0))
                    .into(((OneHolder) holder).iv11);
        }else if (holder instanceof AdvertHodler){
            Log.e("SecondFragment",list.get(position).getAdvert_image_url());
            ((AdvertHodler) holder).title.setText(list.get(position).getTitle());
//            glide.load(AddHeader.buildGlideUrl(list.get(position).getAdvert_image_url())).
//                    placeholder(R.mipmap.newloding).centerCrop()
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).
//                    crossFade().into(((JokeAdapter.AdvertHodler)holder).image);
            Glide.with(mcontext)
                    .load(AddHeader.buildGlideUrl(list.get(position).getAdvert_image_url()))
                    .into(((JokeAdapter.AdvertHodler) holder).image);
        }
    }

   static class MyHodler extends XRecyclerView.ViewHolder implements View.OnClickListener {
        private TextView   titletv;
        private ImageView iv1, iv2, iv3;

        public MyHodler(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener = myItemClickListener;
            titletv = (TextView) itemView.findViewById(R.id.frgament_second_joke_tiltletv);
            /*shanchutv = (TextView) itemView.findViewById(R.id.fragment_second_joke_fenxiang);*/
            /*tvxihuan = (TextView) itemView.findViewById(R.id.fragment_second_joke_xihuan);*/
            iv1 = (ImageView) itemView.findViewById(R.id.fragment_second_joke_image1);
            iv2 = (ImageView) itemView.findViewById(R.id.fragment_second_joke_image2);
            iv3 = (ImageView) itemView.findViewById(R.id.fragment_second_joke_image3);
          /*  iv4= (ImageView) itemView.findViewById(R.id.fragment_second_joke_image4);*/
           /* iv5 = (ImageView) itemView.findViewById(R.id.fragment_second_joke_fenxiang_image);*/
           /* ll1 = (LinearLayout) itemView.findViewById(R.id.fragment_joke_fenxiang);
            ll2 = (LinearLayout) itemView.findViewById(R.id.fragment_joke_shoucang);*/
            itemView.setOnClickListener(this);//单项点击
           /* ll2.setOnClickListener(new View.OnClickListener() {//点击删除
                @Override
                public void onClick(View v) {
                    *//*list.get((Integer) v.getTag()).setChick(true);*//*
                    mListener.OnBtDelete((Integer) v.getTag());
                    notifyDataSetChanged();
                }
            });
            ll1.setOnClickListener(new View.OnClickListener() {//点击喜欢
                @Override
                public void onClick(View v) {
                    mListener.OnLike((Integer) v.getTag());

                }
            });*/
        }
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.setOnItemClickListener(itemView, v, (Integer) v.getTag());
            }
        }
    }
    static class AdvertHodler extends XRecyclerView.ViewHolder{
        private ImageView image;
        private MyFrontTextView title;
        public AdvertHodler(final View itemView, JokeAdapter.MyItemClickListener listener) {
            super(itemView);
            image= (ImageView) itemView.findViewById(R.id.fragment_second_advert_image);
            title= (MyFrontTextView) itemView.findViewById(R.id.fragment_second_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.setOnItemClickListener(itemView,v, (Integer) v.getTag());
                }
            });
        }
    }
   static class OneHolder extends XRecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv2;
        private ImageView iv11;
        public OneHolder(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener=myItemClickListener;
            tv2= (TextView) itemView.findViewById(R.id.fragment_joke_itme2_content);
            iv11= (ImageView) itemView.findViewById(R.id.fragment_joke_itme2_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.setOnItemClickListener(itemView, v, (Integer) v.getTag());
            }
        }
    }
    class ZeroHodler extends XRecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv2;
        private ImageView iv11;
        public ZeroHodler(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener=myItemClickListener;
            tv2= (TextView) itemView.findViewById(R.id.fragment_joke_itme2_content);
            iv11= (ImageView) itemView.findViewById(R.id.fragment_joke_itme2_image);
            itemView.setOnClickListener(this);
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
