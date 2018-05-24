package com.pufei.dashi.module.mistakes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pufei.dashi.R;
import com.pufei.dashi.app.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangbing on 2018/4/26.
 */

public class MisTakesAdapter  extends RecyclerView.Adapter<MisTakesAdapter.MyHodler> {
    private List<String> list = new ArrayList<>();
    private List<Integer>nuberList = new ArrayList<>();
    private Context mcontext;
    private String[] titles;
    private int[] frist_picture;
    public MisTakesAdapter(Context context, List<String> list, List<Integer> nuberList,int[] frist_picture,String[] titles) {//获取数据源
        this.mcontext = context;
        this.list = list;
        this.nuberList = nuberList;
        this.frist_picture = frist_picture;
        this.titles = titles;
    }

    @Override
    public MyHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_mistakes, parent, false);
        MyHodler hodler = new MyHodler(view, mListener);
        return hodler;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final MyHodler holder, final int position) {
        holder.itemView.setTag(position);
        if(App.KEMU == 1){
            holder.mistake_title.setText(titles[Integer.parseInt(list.get(position))-2]);
            holder.mistake_image.setBackgroundResource(frist_picture[Integer.parseInt(list.get(position))-2]);
        }else {
            holder.mistake_title.setText(titles[Integer.parseInt(list.get(position))-20]);
            holder.mistake_image.setBackgroundResource(frist_picture[Integer.parseInt(list.get(position))-20]);
        }

        holder.mistake_count.setText(nuberList.get(position)+"" );
//        holder.iv1.setImageResource(idlist[position]);
    }

    class MyHodler extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mistake_title;
        private TextView mistake_count;
        private ImageView mistake_image;

        public MyHodler(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener = myItemClickListener;
            mistake_title = (TextView) itemView.findViewById(R.id.mistake_title);
            mistake_count = (TextView) itemView.findViewById(R.id.mistake_count);
            mistake_image = (ImageView) itemView.findViewById(R.id.mistake_image);
            itemView.setOnClickListener(this);//单项点击
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.setOnItemClickListener(itemView, v, (Integer) v.getTag());
            }
        }
    }

    private MyItemClickListener mListener = null;//设置点击接口

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mListener = listener;
    }

    public interface MyItemClickListener {
        void setOnItemClickListener(View itemview, View view, int postion);
    }

}