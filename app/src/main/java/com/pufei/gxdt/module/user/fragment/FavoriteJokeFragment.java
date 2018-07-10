package com.pufei.gxdt.module.user.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpFragment;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.home.activity.HotImageActivity;
import com.pufei.gxdt.module.home.activity.JokeDetailActivity;
import com.pufei.gxdt.module.home.activity.PictureDetailActivity;
import com.pufei.gxdt.module.home.adapter.JokeAdapter;
import com.pufei.gxdt.module.home.model.JokeDetailBean;
import com.pufei.gxdt.module.home.model.JokeResultBean;
import com.pufei.gxdt.module.user.adapter.FavoriteAdapter;
import com.pufei.gxdt.module.user.adapter.FavoriteJokeAdapter;
import com.pufei.gxdt.module.user.bean.MyImagesBean;
import com.pufei.gxdt.module.user.presenter.FavoritePresenter;
import com.pufei.gxdt.module.user.view.FavoriteView;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.widgets.SpaceItemDecoration;
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

public class FavoriteJokeFragment extends BaseMvpFragment<FavoritePresenter> implements FavoriteView {
    @BindView(R.id.fav_joke_xryv)
    XRecyclerView rl_joke_xryv;
    @BindView(R.id.fragment_fav_joke_smart)
    SmartRefreshLayout fragmentJokeSmart;
    @BindView(R.id.request_failed)
    LinearLayout request_failed;
    @BindView(R.id.no_data_failed)
    LinearLayout no_data_failed;
    @BindView(R.id.main_bg)
    LinearLayout main_bg;
    @BindView(R.id.btn_refresh)
    Button btn_refresh;
    private FavoriteJokeAdapter jokeAdapter;
    private List<MyImagesBean.ResultBean> cashList = new ArrayList<>();
    private List<MyImagesBean.ResultBean> jokeList = new ArrayList<>();
    private int page = 1;

    @Override
    public void initView() {
        jokeAdapter = new FavoriteJokeAdapter(getActivity(), jokeList,1);
        final  GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        rl_joke_xryv.setLayoutManager(layoutManager);
        rl_joke_xryv.addItemDecoration(new SpaceItemDecoration(10,3));
        rl_joke_xryv.setAdapter(jokeAdapter);
        fragmentJokeSmart.setRefreshHeader(new ClassicsHeader(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        fragmentJokeSmart.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        fragmentJokeSmart.setEnableLoadmore(false);
        rl_joke_xryv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (layoutManager.findLastVisibleItemPosition() ==
                                layoutManager.getItemCount() - 1)
                        ) {
                    if(NetWorkUtil.isNetworkConnected(getActivity())){
                        if(cashList.size()>0){
                            jokeList.addAll(cashList);
                            jokeAdapter.notifyDataSetChanged();
                            cashList.clear();
                            page++;
                            requestJoke(page);
                        }
                    }else{
                        ToastUtils.showShort(getActivity(),"请检查网络设置");
                    }

                }
            }
        });
        fragmentJokeSmart.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
//                refreshlayout.getLayout().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        page++;
//                        requestJoke(page);
//                        try {
//                            refreshlayout.finishLoadmore();
//                        } catch (NullPointerException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, 2000);
            }

            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        requestJoke(page);
                        try {
                            refreshlayout.finishRefresh();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);
            }
        });

        jokeAdapter.setOnItemClickListener(new FavoriteJokeAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                    try {
                        Intent intent = new Intent(getActivity(), PictureDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("picture_index", postion);
                        bundle.putSerializable("picture_list", (Serializable) jokeList);
                        intent.putExtras(bundle);
                        startActivityForResult(intent,1);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

        });
    }

    @Override
    public void getData() {
        if (NetWorkUtil.isNetworkConnected(getActivity())) {
            requestJoke(page);
        } else {
            request_failed.setVisibility(View.VISIBLE);
            main_bg.setBackgroundColor(getResources().getColor(R.color.select_color22));
            btn_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetWorkUtil.isNetworkConnected(getActivity())) {
                        request_failed.setVisibility(View.GONE);
                        main_bg.setBackgroundColor(getResources().getColor(R.color.white));
                        page = 1;
                        requestJoke(page);
                    }else{
                        ToastUtils.showShort(getActivity(),"请先打开网络连接");
                    }
                }
            });
        }
    }

    private void requestJoke(int page) {
        if (NetWorkUtil.isNetworkConnected(getActivity())) {
            try {
                JSONObject jsonObject = KeyUtil.getJson(getActivity());
                jsonObject.put("page", page + "");
                jsonObject.put("type", 1);
                presenter.getFavoriteJokeList(RetrofitFactory.getRequestBody(jsonObject.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            ToastUtils.showShort(getActivity(),"请检查网络设置");
        }

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_fav_joke;
    }

//    @OnClick(R.id.ll_title_left)
//    public void backLastActivity() {
//        AppManager.getAppManager().finishActivity();
//    }

    @Override
    public void setPresenter(FavoritePresenter presenter) {
        if (presenter == null) {
            this.presenter = new FavoritePresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void resultJokeList(MyImagesBean bean) {
        if(bean != null){
            if (page == 1) {
                jokeList.clear();
                if(bean.getResult()!=null&&bean.getResult().size()==0){
                    no_data_failed.setVisibility(View.VISIBLE);
                    main_bg.setBackgroundColor(Color.parseColor("#F0F0F0"));
                }else{
                    jokeList.addAll(bean.getResult());
                    jokeAdapter.notifyDataSetChanged();
                    page++;
                    requestJoke(page);
                }
            }else{
                cashList.addAll(bean.getResult());
            }

        }

    }
    private  int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
    @Override
    public void resultJokeDetail(MyImagesBean bean) {

    }

    @Override
    public void requestErrResult(String msg) {
        ToastUtils.showShort(getActivity(), msg);
    }
}
