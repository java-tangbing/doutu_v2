package com.pufei.gxdt.module.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.home.model.JokeResultBean;
import com.pufei.gxdt.widgets.GlideApp;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;

public class JokeAdvAdapter extends XRecyclerView.Adapter<XRecyclerView.ViewHolder>{
    private List<JokeResultBean.ResultBean> list;
    private Context mcontext;
    private Activity activity;
    private int FIST_IMAGE = 1;
    private int THREE_IMAGE = 3;
    private List<NativeExpressADView> adLists;
    private NativeExpressADView nativeExpressADView;
    private NativeExpressAD nativeExpressAD;
    public JokeAdvAdapter(Context context, List<JokeResultBean.ResultBean> list,List<NativeExpressADView> adLists) {//获取数据源
        this.mcontext = context;
        this.list = list;
        this.adLists = adLists;
        activity = (Activity) mcontext;
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
            if(list.get(position).getType() == 1){
                setAdv(((MyHodler) holder).container);
            }else{
                ((MyHodler) holder).container.setVisibility(View.GONE);
            }
            ((MyHodler) holder).tv_eyes.setText(list.get(position).getView());
            ((MyHodler) holder).tv_hot.setText(list.get(position).getHot());
            ((MyHodler) holder).titletv.setText(list.get(position).getTitle());
            GlideApp.with(mcontext).load(list.get(position).getImages().get(0)).placeholder(R.mipmap.ic_default_picture).into(((MyHodler) holder).iv1);
            GlideApp.with(mcontext).load(list.get(position).getImages().get(1)).placeholder(R.mipmap.ic_default_picture).into(((MyHodler) holder).iv2);
            GlideApp.with(mcontext).load(list.get(position).getImages().get(2)).placeholder(R.mipmap.ic_default_picture).into(((MyHodler) holder).iv3);
        }else if(holder instanceof OneHolder){
            if(list.get(position).getType() == 1){
                setAdv(((OneHolder) holder).container);
            }else {
                ((OneHolder) holder).container.setVisibility(View.GONE);
            }
            ((OneHolder) holder).tv2.setText(list.get(position).getTitle());
            ((OneHolder) holder).tv_eyes.setText(list.get(position).getView());
            ((OneHolder) holder).tv_hot.setText(list.get(position).getHot());
            GlideApp.with(mcontext).load(list.get(position).getImages().get(0)).placeholder(R.mipmap.ic_default_picture).into(((OneHolder) holder).iv11);
        }
    }
    private void setAdv(RelativeLayout layout){
        if(adLists!=null&&adLists.size()>0){
            layout.setVisibility(View.VISIBLE);
            layout.removeAllViews();
            layout.addView(adLists.get(0));
            adLists.get(0).render();
            adLists.remove(0);
            if(adLists.size()<5){
                refreshAd();
            }
        }else{
            refreshAd();
        }
    }
   static class MyHodler extends XRecyclerView.ViewHolder implements View.OnClickListener {
        private TextView  titletv,tv_eyes,tv_hot;
        private ImageView iv1, iv2, iv3;
        private RelativeLayout container;

        public MyHodler(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener = myItemClickListener;
            titletv = (TextView) itemView.findViewById(R.id.frgament_second_joke_tiltletv);
            iv1 = (ImageView) itemView.findViewById(R.id.fragment_second_joke_image1);
            iv2 = (ImageView) itemView.findViewById(R.id.fragment_second_joke_image2);
            iv3 = (ImageView) itemView.findViewById(R.id.fragment_second_joke_image3);
            container =  (RelativeLayout) itemView.findViewById(R.id.container);
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

   static class OneHolder extends XRecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv2,tv_eyes,tv_hot;
        private ImageView iv11;
       private RelativeLayout container;
        public OneHolder(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener=myItemClickListener;
            tv2 = (TextView) itemView.findViewById(R.id.fragment_joke_itme2_content);
            tv_eyes = (TextView) itemView.findViewById(R.id.tv_eyes);
            container =  (RelativeLayout) itemView.findViewById(R.id.container);
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
    private static MyItemClickListener mListener = null;//设置点击接口

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mListener = listener;
    }

    public interface MyItemClickListener {
        void setOnItemClickListener(View itemview, View view, int postion);

    }
    //广告
    private void refreshAd() {
        nativeExpressAD = new NativeExpressAD(mcontext, new ADSize(ADSize.FULL_WIDTH, 100), Contents.TENCENT_ID, Contents.NativeExpressPosID, new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onNoAD(AdError adError) {

            }

            @Override
            public void onADLoaded(List<NativeExpressADView> adList) {
                adLists.clear();
                adLists.addAll(adList);
            }

            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADClosed(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

            }
        });
        nativeExpressAD.loadAD(10);
    }




}
