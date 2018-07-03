package com.pufei.gxdt.module.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpFragment;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.home.adapter.ImageTypeAdapter;
import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.HomeResultBean;
import com.pufei.gxdt.module.home.model.HomeTypeBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.presenter.HomeListPresenter;
import com.pufei.gxdt.module.home.view.HomeListView;
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
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * Created by tb on 2018/5/23.
 */

public class DouTuFragment extends BaseMvpFragment<HomeListPresenter> implements HomeListView {
    @BindView(R.id.srl_doutu)
    SmartRefreshLayout srl_doutu;
    @BindView(R.id.xrl_doutu)
    XRecyclerView xrl_doutu;
    @BindView(R.id.request_failed)
    LinearLayout request_failed;
    @BindView(R.id.main_bg)
    LinearLayout main_bg;
    @BindView(R.id.btn_refresh)
    Button btn_refresh;
    private ImageTypeAdapter adapter;
    private List<PictureResultBean.ResultBean> picturelist = new ArrayList<>();
    private List<PictureResultBean.ResultBean> cashList = new ArrayList<>();
    private int page = 1;
    public  static DouTuFragment newInstence(String id){
        DouTuFragment fragment = new DouTuFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        fragment.setArguments(bundle);
        return  fragment;

    }
    @Override
    public void initView() {
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        xrl_doutu.setLayoutManager(layoutManager);
        xrl_doutu.addItemDecoration(new SpaceItemDecoration(10,3));
        adapter = new ImageTypeAdapter(getActivity(),DouTuFragment.this, picturelist);
        xrl_doutu.setAdapter(adapter);
        srl_doutu.setRefreshHeader(new ClassicsHeader(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        srl_doutu.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        srl_doutu.setEnableLoadmore(false);
        srl_doutu.setEnableLoadmoreWhenContentNotFull(true);
        xrl_doutu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (layoutManager.findLastVisibleItemPosition() ==
                                layoutManager.getItemCount() - 1)
                        ) {
                    if(cashList.size()>0){
                        picturelist.addAll(cashList);
                        adapter.notifyDataSetChanged();
                        page++;
                        requestData();
                    }
                }
            }
        });
        srl_doutu.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
//                refreshlayout.getLayout().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        page++;
//                        requestData(page);
//                        try {
//                            refreshlayout.finishLoadmore();
//                        } catch (NullPointerException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, 2000);
            }

            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        requestData();
                        refreshlayout.finishRefresh();
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void getData() {
        if (NetWorkUtil.isNetworkConnected(getActivity())) {
            xrl_doutu.setVisibility(View.VISIBLE);
            requestData();
        } else {
            xrl_doutu.setVisibility(View.GONE);
            request_failed.setVisibility(View.VISIBLE);
            main_bg.setBackgroundColor(getResources().getColor(R.color.select_color22));
            btn_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetWorkUtil.isNetworkConnected(getActivity())){
                        xrl_doutu.setVisibility(View.VISIBLE);
                        main_bg.setBackgroundColor(getResources().getColor(R.color.white));
                        page = 1;
                        requestData();
                        request_failed.setVisibility(View.GONE);
                    }else {
                        ToastUtils.showShort(getActivity(),"请先打开网络连接");
                    }
                }
            });
        }
    }
    private void requestData() {
        if (NetWorkUtil.isNetworkConnected(getActivity())) {
            try {
                Bundle arguments = getArguments();
                String category_id = null;
                if (arguments != null) {
                    category_id = arguments.getString("id");
                }
                JSONObject jsonObject = KeyUtil.getJson(getActivity());
                jsonObject.put("category_id", category_id);
                jsonObject.put("page", page + "");
                jsonObject.put("net", NetWorkUtil.netType(getActivity()));
                jsonObject.put("auth", SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH));
                presenter.getHomeDetailList(RetrofitFactory.getRequestBody(jsonObject.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            ToastUtils.showShort(getActivity(),"请检查网络设置");
        }
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_doutu;
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

    }

    @Override
    public void resultHomeDetailList(PictureResultBean bean) {
        if (bean != null) {
            if (page == 1) {
                picturelist.clear();
                picturelist.addAll(bean.getResult());
                adapter.notifyDataSetChanged();
                page++;
                requestData();
            }else{
                cashList.clear();
                cashList.addAll(bean.getResult());
            }

        }

    }

    @Override
    public void resultHomeTypeList(HomeTypeBean bean) {

    }

    @Override
    public void resultCountView(FavoriteBean bean) {

    }

    @Override
    public void requestErrResult(String msg) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            page = 1;
            requestData();
        }
    }

}
