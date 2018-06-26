package com.pufei.gxdt.module.home.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.home.model.HomeResultBean;
import com.pufei.gxdt.widgets.GlideApp;
import java.util.List;

public class HomeListAdapter extends XRecyclerView.Adapter<XRecyclerView.ViewHolder> {
    private List<HomeResultBean.ResultBean> list;
    private Context mcontext;
    private int FIST_IMAGE = 2;
    public HomeListAdapter(Context context, List<HomeResultBean.ResultBean> list) {//获取数据源
        this.mcontext = context;
        this.list = list;
    }

    @Override
    public XRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FIST_IMAGE){
            View view=LayoutInflater.from(mcontext).inflate(R.layout.fragment_joke_item2,parent,false);
            OneHolder holder=new OneHolder(view,mListener);
            return holder;
        }else {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.fragment_joke_item1, parent, false);
            MyHodler hodler = new MyHodler(view, mListener);
            return hodler;
        }

    }


    @Override
    public int getItemViewType(int position) {
        return Integer.parseInt(list.get(position).getCat());
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
            if(list.get(position).getImgs()!=null){
                if(list.get(position).getImgs().size()>0){
                    GlideApp.with(mcontext).load(list.get(position).getImgs().get(0).getUrl()).placeholder(R.mipmap.ic_default_picture).into(((MyHodler) holder).iv1);
                    GlideApp.with(mcontext).load(list.get(position).getImgs().get(1).getUrl()).placeholder(R.mipmap.ic_default_picture).into(((MyHodler) holder).iv2);
                    GlideApp.with(mcontext).load(list.get(position).getImgs().get(2).getUrl() ).placeholder(R.mipmap.ic_default_picture).into(((MyHodler) holder).iv3);
                }
            }
        }else if(holder instanceof OneHolder){
            ((OneHolder) holder).tv2.setText(list.get(position).getTitle());
            ((OneHolder) holder).tv_eyes.setText(list.get(position).getView());
            ((OneHolder) holder).tv_hot.setText(list.get(position).getHot());
            if(list.get(position).getImages()!=null&&list.get(position).getImages().size()>0){
                GlideApp.with(mcontext).load(list.get(position).getImages().get(0)).placeholder(R.mipmap.ic_default_picture).into(((OneHolder) holder).iv11);
            }
        }
    }

   static class MyHodler extends XRecyclerView.ViewHolder implements View.OnClickListener {
        private TextView   titletv,tv_eyes,tv_hot;
        private ImageView iv1, iv2, iv3;

        public MyHodler(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener = myItemClickListener;
            titletv = (TextView) itemView.findViewById(R.id.frgament_second_joke_tiltletv);
            tv_eyes =  (TextView) itemView.findViewById(R.id.tv_eyes);
            tv_hot =  (TextView) itemView.findViewById(R.id.tv_hot);
            iv1 = (ImageView) itemView.findViewById(R.id.fragment_second_joke_image1);
            iv2 = (ImageView) itemView.findViewById(R.id.fragment_second_joke_image2);
            iv3 = (ImageView) itemView.findViewById(R.id.fragment_second_joke_image3);
            itemView.setOnClickListener(this);//单项点击
        }
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                if(v != null){
                    mListener.setOnItemClickListener(itemView, v, (Integer) v.getTag());
                }
            }
        }
    }
   static class OneHolder extends XRecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv2,tv_eyes,tv_hot;
        private ImageView iv11;
        public OneHolder(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            mListener=myItemClickListener;
            tv2= (TextView) itemView.findViewById(R.id.fragment_joke_itme2_content);
            tv_eyes =  (TextView) itemView.findViewById(R.id.tv_eyes);
            tv_hot =  (TextView) itemView.findViewById(R.id.tv_hot);
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
    }
}
