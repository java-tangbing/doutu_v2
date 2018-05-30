package com.pufei.gxdt.module.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.module.home.adapter.HotAdapter;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.presenter.ImageTypePresenter;
import com.pufei.gxdt.module.home.view.ImageTypeView;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
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

/**
 * Created by tb on 2018/5/24.
 */

public class HotImageActivity extends BaseMvpActivity<ImageTypePresenter> implements ImageTypeView {
    @BindView(R.id.hot_xryv)
    XRecyclerView hotXryv;
    @BindView(R.id.fragment_hot_smart)
    SmartRefreshLayout fragmentHotSmart;
    @BindView(R.id.ll_title_left)
    LinearLayout ll_left;
    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.request_failed)
    LinearLayout requestFailed;
    private List<PictureResultBean.ResultBean> picturelist = new ArrayList<>();
    private HotAdapter adapter;
    private  int page = 1;

    @Override
    public void initView() {
        title.setText("热门表情");
        ll_left.setVisibility(View.VISIBLE);
        hotXryv.setLayoutManager(new GridLayoutManager(HotImageActivity.this, 3));
        hotXryv.addItemDecoration(new SpaceItemDecoration(dp2px(HotImageActivity.this, 10)));
        adapter = new HotAdapter(HotImageActivity.this, picturelist);
        hotXryv.setAdapter(adapter);

        adapter.setOnItemClickListener(new HotAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                Intent intent = new Intent(HotImageActivity.this, PictureDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("picture_index", postion);
                bundle.putSerializable("picture_list", (Serializable) picturelist);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onDelete(int position) {
            }

            @Override
            public void onAdd(int position) {
            }
        });
        fragmentHotSmart.setRefreshHeader(new ClassicsHeader(HotImageActivity.this).setSpinnerStyle(SpinnerStyle.Translate));
        fragmentHotSmart.setRefreshFooter(new ClassicsFooter(HotImageActivity.this).setSpinnerStyle(SpinnerStyle.Translate));
        fragmentHotSmart.setEnableLoadmore(true);
        fragmentHotSmart.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        requestHot(page);
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
                        requestHot(page);
                        refreshlayout.finishRefresh();
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void getData() {
        if (NetWorkUtil.isNetworkConnected(HotImageActivity.this)) {
            requestHot(page);
        }else{
            requestFailed.setVisibility(View.VISIBLE);
        }
    }
    private void requestHot(int page ){
        try {
            JSONObject jsonObject = KeyUtil.getJson(this);
            jsonObject.put("page", page + "");
            presenter.getHotImage(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_hot;
    }

    @Override
    public void setPresenter(ImageTypePresenter presenter) {
        if (presenter == null) {
            this.presenter = new ImageTypePresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void resultHotImage(PictureResultBean bean) {
       if(bean!=null){
           if(page == 1){
               picturelist.clear();
           }
           picturelist.addAll(bean.getResult());
           adapter.notifyDataSetChanged();
       }

    }
    private  int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
    @OnClick(R.id.ll_title_left)
    public  void finishActivity(){
        AppManager.getAppManager().finishActivity();
    }

}
