package com.pufei.gxdt.module.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobads.AdSettings;
import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpFragment;
import com.pufei.gxdt.module.home.activity.FaceTypeActivity;
import com.pufei.gxdt.module.home.activity.HomeImageActivity;
import com.pufei.gxdt.module.home.activity.HotImageActivity;
import com.pufei.gxdt.module.home.activity.JokeActivity;
import com.pufei.gxdt.module.home.activity.JokeDetailActivity;
import com.pufei.gxdt.module.home.activity.SearchActivity;
import com.pufei.gxdt.module.home.activity.ThemeImageActivity;
import com.pufei.gxdt.module.home.adapter.HomeListAdapter;
import com.pufei.gxdt.module.home.model.HomeResultBean;
import com.pufei.gxdt.module.home.model.HomeTypeBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.presenter.HomeListPresenter;
import com.pufei.gxdt.module.home.view.HomeListView;
import com.pufei.gxdt.module.news.activity.NewsActivity;
import com.pufei.gxdt.utils.AdvUtil;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.widgets.GlideApp;
import com.pufei.gxdt.widgets.GridSpaceItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseMvpFragment<HomeListPresenter> implements HomeListView {
    @BindView(R.id.request_failed)
    LinearLayout request_failed;
    @BindView(R.id.srf_home_list)
    SmartRefreshLayout srf_home_lisyt;
    @BindView(R.id.rl_home_list)
    XRecyclerView rl_home_list;
    @BindView(R.id.your_original_layout)
    RelativeLayout your_original_layout;
    private HomeListAdapter adapter;
    private int page = 1;
    private List<HomeResultBean.ResultBean> homeList = new ArrayList<>();
    private List<HomeTypeBean.ResultBean> homeTypeList = new ArrayList<>();
    private View headView;


    @Override
    public void initView() {
        AdvUtil.getAdvHttp(getActivity(),your_original_layout,1);
        LayoutInflater lif = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headView = lif.inflate(R.layout.home_head, null);
        rl_home_list.addHeaderView(headView);
        adapter = new HomeListAdapter(getActivity(), homeList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());//布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rl_home_list.setLayoutManager(layoutManager);
        rl_home_list.setAdapter(adapter);
        srf_home_lisyt.setRefreshHeader(new ClassicsHeader(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        srf_home_lisyt.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        srf_home_lisyt.setEnableLoadmore(true);
        srf_home_lisyt.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        requestHomeList(page);
                        try {
                            refreshlayout.finishLoadmore();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                    }
                }, 2000);
            }

            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        requestHomeList(page);
                        try {
                            refreshlayout.finishRefresh();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);
            }
        });
        adapter.setOnItemClickListener(new HomeListAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                if ("3".equals(homeList.get(postion).getCat())) {
                    Intent intent = new Intent(getActivity(), HomeImageActivity.class);
                    intent.putExtra("category_id", homeList.get(postion).getImgs().get(0).getCategory_id());
                    intent.putExtra("title", homeList.get(postion).getCategory_name());
                    intent.putExtra("eyes", homeList.get(postion).getView());
                    intent.putExtra("hot", homeList.get(postion).getHot());
                    startActivity(intent);
                }else if("1".equals(homeList.get(postion).getCat())){
                    int position = 0;
                    for(int  i = 0;i<homeTypeList.size();i++){
                        if(homeList.get(postion).getId().equals(homeTypeList.get(i).getId()) ){
                            position = i;
                            break;
                        }
                    }
                    Intent intent = new Intent(getActivity(), FaceTypeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("title_list", (Serializable) homeTypeList);
                    bundle.putInt("index",position);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), JokeDetailActivity.class);
                    intent.putExtra("id",homeList.get(postion).getId());
                    intent.putExtra("title",homeList.get(postion).getTitle());
                    intent.putExtra("time",homeList.get(postion).getDateline());
                    startActivity(intent);
                }

            }

            @Override
            public void OnLike(int position) {

            }

            @Override
            public void OnBtDelete(int position) {

            }
        });
    }

    @Override
    public void getData() {
        if (NetWorkUtil.isNetworkConnected(getActivity())) {
            try {
                JSONObject getHomeTypeObj = KeyUtil.getJson(getActivity());
                getHomeTypeObj.put("net", NetWorkUtil.netType(getActivity()));
                presenter.getHomeTypeList(RetrofitFactory.getRequestBody(getHomeTypeObj.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        requestHomeList(page);
    }

    private void requestHomeList(int page) {
        if (NetWorkUtil.isNetworkConnected(getActivity())) {
            try {
                JSONObject getHomeListObj = KeyUtil.getJson(getActivity());
                getHomeListObj.put("net", NetWorkUtil.netType(getActivity()));
                getHomeListObj.put("page", page);
                presenter.getHomeList(RetrofitFactory.getRequestBody(getHomeListObj.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            request_failed.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_home;
    }
    @OnClick({R.id.iv_news,R.id.activity_main_search})
    public  void onViewClicked(View view){
        switch (view.getId()){
            case R.id.iv_news:
                startActivity(new Intent(getActivity(), NewsActivity.class));
                break;
            case R.id.activity_main_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
        }

    }


    @Override
    public void setPresenter(HomeListPresenter presenter) {
        if (presenter == null) {
            this.presenter = new HomeListPresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void resultHomeList(HomeResultBean bean) {
        if (bean != null) {
            homeList.addAll(bean.getResult());
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void resultHomeDetailList(PictureResultBean bean) {

    }

    @Override
    public void resultHomeTypeList(HomeTypeBean bean) {
        if (bean != null) {
            homeTypeList.addAll(bean.getResult());
            setTypeText(headView);
        }

    }

    private void setTypeText(View headView) {
        if (homeTypeList.size() > 0) {
            RecyclerView home_xrl_image = headView.findViewById(R.id.home_xrl_image);
            ImageTypeAdapter adapter = new ImageTypeAdapter(getActivity(), homeTypeList);
            home_xrl_image.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            home_xrl_image.addItemDecoration(new GridSpaceItemDecoration(dp2px(getActivity(), 10), dp2px(getActivity(), 10), true));
            home_xrl_image.setAdapter(adapter);
            headView.findViewById(R.id.tv_hot_face).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), HotImageActivity.class));
                }
            });
            headView.findViewById(R.id.tv_joke).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), JokeActivity.class));
                }
            });
            headView.findViewById(R.id.tv_theme_face).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), ThemeImageActivity.class));
                }
            });
        }

    }

    class ImageTypeAdapter extends RecyclerView.Adapter<ImageTypeAdapter.MyHodler> {
        private List<HomeTypeBean.ResultBean> list;
        private Context mcontext;

        public ImageTypeAdapter(Context context, List<HomeTypeBean.ResultBean> list) {//获取数据源
            this.mcontext = context;
            this.list = list;
        }

        @Override
        public ImageTypeAdapter.MyHodler onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.item_home_type, parent, false);
            MyHodler hodler = new MyHodler(view);
            return hodler;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onBindViewHolder(final ImageTypeAdapter.MyHodler holder, final int position) {
            holder.itemView.setTag(position);
            holder.tv.setText(list.get(position).getCategory_name());
            GlideApp.with(mcontext).load(list.get(position).getImages()).into(holder.iv);
            holder.ll_doutu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FaceTypeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("title_list", (Serializable) homeTypeList);
                    bundle.putInt("index",position);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public void onViewDetachedFromWindow(ImageTypeAdapter.MyHodler holder) {
            super.onViewDetachedFromWindow(holder);
        }

        class MyHodler extends RecyclerView.ViewHolder {
            private ImageView iv;
            private TextView tv;
            private LinearLayout ll_doutu;

            public MyHodler(View itemView) {
                super(itemView);
                iv = (ImageView) itemView.findViewById(R.id.iv_doutu);
                tv = (TextView) itemView.findViewById(R.id.tv_doutu);
                ll_doutu = (LinearLayout) itemView.findViewById(R.id.ll_doutu);
            }
        }

    }

    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
