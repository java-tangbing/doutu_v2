package com.pufei.gxdt.module.find.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.commonality.model.RecommendBean;
import com.pufei.gxdt.util.AddHeader;
import com.pufei.gxdt.util.DensityUtils;
import com.pufei.gxdt.util.TimeUtils;
import com.pufei.gxdt.view.GlideRoundTransform;

import java.util.List;

/**
 * Created by wangwenzhang on 2016/11/9.
 */
public class FindPagerAdpater extends XRecyclerView.Adapter<XRecyclerView.ViewHolder> {
    private List<RecommendBean.ResultBean>list;
    private Context mcontext;
    private final RequestManager glide;
    public FindPagerAdpater(Context context, List<RecommendBean.ResultBean> list, RequestManager glide){//获取数据源
        this.mcontext=context;
        this.list=list;
        this.glide = glide;
    }
    @Override
    public XRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==0){
            View view= LayoutInflater.from(mcontext).inflate(R.layout.fragment_pager_itme,parent,false);
            MyHodler hodler=new MyHodler(view,mListener);
            return hodler;
        }else {
            View view=LayoutInflater.from(mcontext).inflate(R.layout.fragment_pager_advert,parent,false);
            AdvertHodler hodler=new AdvertHodler(view,mListener);
            return hodler;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
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
            glide.load(AddHeader.buildGlideUrl(list.get(position).getImgs().get(0).getUrl())).
                    placeholder(R.mipmap.newloding).diskCacheStrategy(DiskCacheStrategy.SOURCE).
                    centerCrop()
                    .crossFade().into(((MyHodler)holder).iv1);
            glide.load(AddHeader.buildGlideUrl(list.get(position).getImgs().get(1).getUrl())).
                    placeholder(R.mipmap.newloding).diskCacheStrategy(DiskCacheStrategy.SOURCE).
                    centerCrop()
                    .crossFade().into(((MyHodler)holder).iv2);
            glide.load(AddHeader.buildGlideUrl(list.get(position).getImgs().get(2).getUrl())).
                    placeholder(R.mipmap.newloding).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).
                    crossFade().into(((MyHodler)holder).iv3);
        }else if (holder instanceof AdvertHodler){
            glide.load(AddHeader.buildGlideUrl(list.get(position).getAdvert_image_url())).
                    placeholder(R.mipmap.newloding).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).
                    crossFade().into(((AdvertHodler)holder).image);
        }

    }

    static class MyHodler extends XRecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titletv;
        private ImageView iv1, iv2, iv3;
        public MyHodler(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener=myItemClickListener;
            itemView.setOnClickListener(this);
            titletv = (TextView) itemView.findViewById(R.id.fragment_pager_itme_title);
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
            image= (ImageView) itemView.findViewById(R.id.fragment_pager_advert_image);
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
   /* private OnCountClickListener onCountClickListener;
    public void setOnCountClickListener(OnCountClickListener listener){
        this.onCountClickListener=listener;
    }
    public interface OnCountClickListener{
        void OnLike(int position);
        void OnBtDelete(int position);
    }*/
}
