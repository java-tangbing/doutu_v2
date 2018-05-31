package com.pufei.gxdt.module.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpFragment;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.home.activity.HotImageActivity;
import com.pufei.gxdt.module.home.activity.PictureDetailActivity;
import com.pufei.gxdt.module.home.adapter.HotAdapter;
import com.pufei.gxdt.module.home.adapter.ImageTypeAdapter;
import com.pufei.gxdt.module.home.model.HomeResultBean;
import com.pufei.gxdt.module.home.model.HomeTypeBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.presenter.HomeListPresenter;
import com.pufei.gxdt.module.home.view.HomeListView;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.widgets.GlideApp;
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
    private ImageTypeAdapter adapter;
    private List<PictureResultBean.ResultBean> picturelist = new ArrayList<>();
    private int page = 1;
    public  static DouTuFragment newInstence(String id){
        DouTuFragment fragment = new DouTuFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        fragment.setArguments(bundle);
        return  fragment;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void initView() {
        xrl_doutu.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        xrl_doutu.addItemDecoration(new SpaceItemDecoration(dp2px(getActivity(), 10)));
        adapter = new ImageTypeAdapter(getActivity(), picturelist);
        xrl_doutu.setAdapter(adapter);
        srl_doutu.setRefreshHeader(new ClassicsHeader(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        srl_doutu.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        srl_doutu.setEnableLoadmore(true);
//        adapter.setOnItemClickListener(new HotAdapter.MyItemClickListener() {
//            @Override
//            public void setOnItemClickListener(View itemview, View view, int postion) {
//                Intent intent = new Intent(getActivity(), PictureDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("picture_index", postion);
//                bundle.putSerializable("picture_list", (Serializable) picturelist);
//                intent.putExtras(bundle);
//                startActivity(intent);
//
//            }

//            @Override
//            public void onDelete(int position) {
//
//            }
//
//            @Override
//            public void onAdd(int position) {
//
//            }
//        });
        srl_doutu.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        requestData(page);
                        refreshlayout.finishLoadmore();
                    }
                }, 2000);
            }

            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        adapter.notifyDataSetChanged();
                        requestData(page);
                        refreshlayout.finishRefresh();
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void getData() {
        requestData(page);
    }
    private void requestData(int page) {
        if (NetWorkUtil.isNetworkConnected(getActivity())) try {
            Bundle arguments = getArguments();
            String category_id = null;
            if (arguments != null) {
                category_id = arguments.getString("id");
            }
            JSONObject jsonObject = KeyUtil.getJson(getActivity());
            jsonObject.put("category_id", category_id);
            jsonObject.put("page", page + "");
            jsonObject.put("net", NetWorkUtil.netType(getActivity()));
            jsonObject.put("net", SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH));
            presenter.getHomeDetailList(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        else {
            request_failed.setVisibility(View.VISIBLE);
        }
    }

    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
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
            }
            picturelist.addAll(bean.getResult());
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void resultHomeTypeList(HomeTypeBean bean) {

    }
}
