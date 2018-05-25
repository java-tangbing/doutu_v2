package com.pufei.gxdt.module.home.adapter;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangwenzhang on 2017/2/21.
 */
public class JokeDetalAdpater extends XRecyclerView.Adapter<JokeDetalAdpater.MyHolder> {
    private Context mcontext;
    private List<String> list;
    private List<String>imagelist;
    private int a;
    private String SHARE_APP_TAG="frist";
    public JokeDetalAdpater(Context context, List<String> list, List<String> list1){
        this.mcontext=context;
        this.list=list;
        this.imagelist=list1;
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
                Glide.with(mcontext)
                      .load(value)
                      .into(holder.iv);

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
