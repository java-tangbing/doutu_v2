package com.pufei.gxdt.module.sign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.sign.model.CrunchiesBean;
import com.pufei.gxdt.widgets.GlideApp;
import com.pufei.gxdt.widgets.MyFrontTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wangwenzhang on 2017/8/15.
 */

public class CrunchinesAdapter extends XRecyclerView.Adapter<CrunchinesAdapter.MyHolder> {
    private Context context;
    private List<CrunchiesBean.ResultBean> list;

    public CrunchinesAdapter(Context context, List<CrunchiesBean.ResultBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_crunchines_item, parent, false);
        CrunchinesAdapter.MyHolder hodler = new CrunchinesAdapter.MyHolder(view);
        return hodler;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.itemView.setBackgroundResource(R.color.white);
        holder.ivtype.setVisibility(View.GONE);
        if (position == 0) {
            holder.ivtype.setVisibility(View.VISIBLE);
            holder.ivtype.setImageResource(R.mipmap.god);
            holder.tvPosition.setTextColor(context.getResources().getColor(R.color.heise));
            //holder.itemView.setBackgroundResource(R.color.nm_1);
        } else if (position == 1) {
            holder.ivtype.setVisibility(View.VISIBLE);
            holder.ivtype.setImageResource(R.mipmap.yin);
            holder.tvPosition.setTextColor(context.getResources().getColor(R.color.heise));
            //holder.itemView.setBackgroundResource(R.color.nm_2);
        } else if (position == 2) {
            holder.ivtype.setVisibility(View.VISIBLE);
            holder.ivtype.setImageResource(R.mipmap.tong);
            //holder.itemView.setBackgroundResource(R.color.nm_3);
            holder.tvPosition.setTextColor(context.getResources().getColor(R.color.heise));
        } else {
            holder.ivtype.setVisibility(View.GONE);
        }
        holder.tvPosition.setText(position + 1 + "");
        holder.tvname.setText(list.get(position).getUsername());
        holder.tvid.setText("ID：" + list.get(position).getUid());
        holder.tvscore.setText(list.get(position).getTotal_score());
//        Glide.with(context).load(list.get(position).getHeader()).crossFade().placeholder(R.mipmap.user_default).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.ivheader);
        GlideApp.with(context).load(list.get(position).getHeader()).placeholder(R.mipmap.classify_bt_default).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(holder.ivheader);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends XRecyclerView.ViewHolder {
        private MyFrontTextView tvPosition, tvname, tvid, tvscore;
        private CircleImageView ivheader;
        private ImageView ivtype;

        public MyHolder(View itemView) {
            super(itemView);
            tvPosition = (MyFrontTextView) itemView.findViewById(R.id.activity_crunchies_item_positon);
            tvname = (MyFrontTextView) itemView.findViewById(R.id.activity_crunchies_item_name);
            tvid = (MyFrontTextView) itemView.findViewById(R.id.activity_crunchies_item_id);
            tvscore = (MyFrontTextView) itemView.findViewById(R.id.activity_crunchies_item_score);
            ivheader = (CircleImageView) itemView.findViewById(R.id.activity_crunchies_item_header);
            ivtype = (ImageView) itemView.findViewById(R.id.activity_crunchies_item_type);
        }
    }
}
