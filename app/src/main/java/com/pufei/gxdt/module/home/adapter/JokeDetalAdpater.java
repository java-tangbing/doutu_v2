package com.pufei.gxdt.module.joke.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangwenzhang on 2017/2/21.
 */
public class JokeDetalAdpater extends XRecyclerView.Adapter<JokeDetalAdpater.MyHolder> {
    private Context mcontext;
    private List<String> list;
    private List<String>imagelist;
    private final RequestManager glide;
    private HashMap<String,Integer> height;
    private HashMap<String,Integer> weith;
    private int a;
    private String SHARE_APP_TAG="frist";
    public JokeDetalAdpater(Context context, List<String> list, List<String> list1, RequestManager glide){
        this.mcontext=context;
        this.list=list;
        this.imagelist=list1;
        this.glide = glide;
        height=new HashMap<>();
        weith=new HashMap<>();
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.activity_jokedetal_item,parent,false);
        MyHolder holder=new MyHolder(view,itemOnclick);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        SharedPreferences setting = mcontext.getSharedPreferences(SHARE_APP_TAG, 0);
        //Boolean gif=setting.getBoolean("GIF",false);
        final String value=list.get(position);
        Log.e("笑话的图片资源",value);
        for (int i=0;i<imagelist.size();i++){
            if (value.equals(imagelist.get(i))){
                holder.ll.setTag(i);
            }
        }
        if (value.contains("jpg")&&value.contains("http")){
                holder.tv.setVisibility(View.GONE);
                holder.ll.setVisibility(View.VISIBLE);
            holder.ivgif.setVisibility(View.GONE);
          /*  glide.load(value).
                    placeholder(R.mipmap.loading).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.iv);
            glide.load(value).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    int width1 = resource.getWidth();
                    int height1 = resource.getHeight();
                    double a = width1 / height1;
                    FrameLayout.LayoutParams params= (FrameLayout.LayoutParams) holder.iv.getLayoutParams();
                    if (height.get(value) == null) {
                        height.put(value, (int) (params.height/a));
                        weith.put(value, resource.getWidth());
                    }
                    params.height= height.get(value);
                    holder.iv.setLayoutParams(params);
                }
            });*/
           glide.load(value)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                            if (holder.iv.getScaleType() != ImageView.ScaleType.FIT_XY) {
                                holder.iv.setScaleType(ImageView.ScaleType.FIT_XY);
                            }
                            if (height.get(value)==null){
                                ViewGroup.LayoutParams params = holder.iv.getLayoutParams();
                                int vw = holder.iv.getWidth() - holder.iv.getPaddingLeft() - holder.iv.getPaddingRight();
                                float scale = (float) vw / (float) resource.getIntrinsicWidth();
                                int vh = Math.round(resource.getIntrinsicHeight() * scale);
                                //params.height = vh + holder.iv.getPaddingTop() + holder.iv.getPaddingBottom();
                                height.put(value,vh + holder.iv.getPaddingTop() + holder.iv.getPaddingBottom());
                            }
                            ViewGroup.LayoutParams params = holder.iv.getLayoutParams();
                           /* int vw = holder.iv.getWidth() - holder.iv.getPaddingLeft() - holder.iv.getPaddingRight();
                            float scale = (float) vw / (float) resource.getIntrinsicWidth();
                            int vh = Math.round(resource.getIntrinsicHeight() * scale);*/
                            params.height = height.get(value);
                            holder.iv.setLayoutParams(params);
                            return false;
                        }
                    })

                    .into(holder.iv);
            //Bitmap bitmap=glide.load(value).asBitmap().into()
/*
                    }); /*{
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            //resource.get
                          *//*  if (height.get(value)==null){
                                height.put(value,resource.getIntrinsicHeight());
                                weith.put(value,resource.getIntrinsicWidth());
                            }*//*
                            holder.iv.setImageDrawable(resource);
                          *//*  FrameLayout.LayoutParams layoutParams= (FrameLayout.LayoutParams) holder.iv.getLayoutParams();
                            layoutParams.height=height.get(value);
                            layoutParams.width=weith.get(value);*//*
                           // holder.iv.setLayoutParams(layoutParams);
                        }
                    });*/

              /*  holder.iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200));*/
                //
           /* if ((value.substring(value.length()-3,value.length())).equals("800")){
                holder.ivgif.setVisibility(View.GONE);
                Glide.with(mcontext).load(value).asBitmap().
                        placeholder(R.mipmap.loading).diskCacheStrategy(DiskCacheStrategy.SOURCE).
                        fitCenter().into(holder.iv);
            }else {
                if (!gif){
                    holder.ivgif.setVisibility(View.GONE);
                    Glide.with(mcontext).load(value).asGif().
                            placeholder(R.mipmap.loading).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade()
                            .fitCenter().into(holder.iv);
                }else {
                    holder.ivgif.setVisibility(View.VISIBLE);
                    Glide.with(mcontext).load(value).asBitmap().
                            placeholder(R.mipmap.loading).diskCacheStrategy(DiskCacheStrategy.SOURCE).//先加载静态
                            fitCenter().into(holder.iv);
                    holder.ivgif.setOnClickListener(new View.OnClickListener() {//点击后加载动态
                        @Override
                        public void onClick(View v) {
                            holder.ivgif.setVisibility(View.GONE);
                            Glide.with(mcontext).load(value).asGif().
                                    placeholder(R.mipmap.loading).diskCacheStrategy(DiskCacheStrategy.NONE).crossFade()
                                    .fitCenter().into(holder.iv);
                        }
                    });
                }*/

           // }
           /* Glide.with(mcontext).load(value).
                        placeholder(R.mipmap.loading).diskCacheStrategy(DiskCacheStrategy.SOURCE).
                        fitCenter()
                        .crossFade().into(holder.iv);*/

            }else {
                holder.tv.setVisibility(View.VISIBLE);
                holder.ll.setVisibility(View.GONE);
                holder.tv.setText(value);
            }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends XRecyclerView.ViewHolder{
        private TextView tv;
        private ImageView iv,ivgif;
        private FrameLayout ll;
        public MyHolder(View itemView,MyItemOnclick myItemOnclick) {
            super(itemView);
            itemOnclick=myItemOnclick;
            ivgif= (ImageView) itemView.findViewById(R.id.activity_jokedetail_item_gif);
            tv= (TextView) itemView.findViewById(R.id.activity_jokedetail_item_tv);
            iv= (ImageView) itemView.findViewById(R.id.activity_jokedetail_item_iv);
            ll= (FrameLayout) itemView.findViewById(R.id.activity_jokedetail_item_ll);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnclick.OnImage((Integer) v.getTag());
                }
            });
            ll.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemOnclick.OnlongImage((Integer) v.getTag());
                    return false;
                }
            });
        }
    }

    private MyItemOnclick itemOnclick=null;
    public void setOnItemClickListener(MyItemOnclick listener) {
        this.itemOnclick = listener;
    }
    public interface  MyItemOnclick{
        void OnImage(int position);
        void OnlongImage(int position);
    }
}
