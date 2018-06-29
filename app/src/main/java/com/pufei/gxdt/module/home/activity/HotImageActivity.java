package com.pufei.gxdt.module.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.home.adapter.HotAdapter;
import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.PictureDetailBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.presenter.ImageTypePresenter;
import com.pufei.gxdt.module.home.view.ImageTypeView;
import com.pufei.gxdt.utils.AdvUtil;
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

/**
 * Created by tb on 2018/5/24.
 */

public class HotImageActivity extends BaseMvpActivity<ImageTypePresenter> implements ImageTypeView{
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
    @BindView(R.id.btn_refresh)
    Button btn_refresh;
    @BindView(R.id.your_original_layout)
    RelativeLayout your_original_layout;
    private List<PictureResultBean.ResultBean> picturelist = new ArrayList<>();
    private List<PictureResultBean.ResultBean> cashList = new ArrayList<>();
    private HotAdapter adapter;
    private  int page = 1;

    @Override
    public void initView() {
        AdvUtil.getInstance().getAdvHttp(this,your_original_layout,5);
        title.setText("热门表情");
        ll_left.setVisibility(View.VISIBLE);
        final GridLayoutManager layoutManager = new GridLayoutManager(HotImageActivity.this, 3);
        hotXryv.setLayoutManager(layoutManager);
        hotXryv.addItemDecoration(new SpaceItemDecoration(10,3));
        adapter = new HotAdapter(HotImageActivity.this, picturelist,true);
        hotXryv.setAdapter(adapter);

        adapter.setOnItemClickListener(new HotAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                countView(picturelist.get(postion).getId(),3,picturelist.get(postion).getOrgintable(),"click");
                Intent intent = new Intent(HotImageActivity.this, PictureDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("picture_index", postion);
                bundle.putSerializable("picture_list", (Serializable) picturelist);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }

        });
        fragmentHotSmart.setRefreshHeader(new ClassicsHeader(HotImageActivity.this).setSpinnerStyle(SpinnerStyle.Translate));
        fragmentHotSmart.setRefreshFooter(new ClassicsFooter(HotImageActivity.this).setSpinnerStyle(SpinnerStyle.Translate));
        fragmentHotSmart.setEnableLoadmore(false);
        hotXryv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (layoutManager.findLastVisibleItemPosition() ==
                                layoutManager.getItemCount() - 1)
                        ) {
                    if(NetWorkUtil.isNetworkConnected(HotImageActivity.this)){
                        if(cashList.size()>0){
                            picturelist.addAll(cashList);
                            adapter.notifyDataSetChanged();
                            page++;
                            requestHot();
                        }
                    }else{
                        ToastUtils.showShort(HotImageActivity.this,"请检查网络设置");
                    }

                }
            }
        });
        fragmentHotSmart.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
//                refreshlayout.getLayout().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        page++;
//                        requestHot(page);
//                        refreshlayout.finishLoadmore();
//                    }
//                }, 2000);
            }

            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        adapter.notifyDataSetChanged();
                        requestHot();
                        refreshlayout.finishRefresh();
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void getData() {
        if (NetWorkUtil.isNetworkConnected(HotImageActivity.this)) {
            requestHot();
        }else{
            requestFailed.setVisibility(View.VISIBLE);
            btn_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(NetWorkUtil.isNetworkConnected(HotImageActivity.this)) {
                        requestFailed.setVisibility(View.GONE);
                        AdvUtil.getInstance().getAdvHttp(HotImageActivity.this, your_original_layout, 5);
                        page = 1;
                        requestHot();
                    }else{
                        ToastUtils.showShort(HotImageActivity.this,"请检查网络设置");
                    }
                }
            });
        }
    }
    private void requestHot(){
        if (NetWorkUtil.isNetworkConnected(HotImageActivity.this)) {
            try {
                JSONObject jsonObject = KeyUtil.getJson(this);
                jsonObject.put("page", page + "");
                jsonObject.put("auth", SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH));
                presenter.getHotImage(RetrofitFactory.getRequestBody(jsonObject.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            ToastUtils.showShort(this,"请检查网络设置");
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
               picturelist.addAll(bean.getResult());
               adapter.notifyDataSetChanged();
               page++;
               requestHot();
           }else {
              cashList.clear();
              cashList.addAll(bean.getResult());
           }

       }

    }

    @Override
    public void resultImageDetail(PictureDetailBean bean) {

    }

    @Override
    public void resultAddFavorite(FavoriteBean bean) {

    }

    @Override
    public void resultCancleFavorite(FavoriteBean bean) {

    }

    @Override
    public void resultCountView(FavoriteBean bean) {

    }

    @Override
    public void requestErrResult(String msg) {

    }

    private void countView(String id,int type,String orgintable,String option){
        if(NetWorkUtil.isNetworkConnected(this)){
            try {
                JSONObject countViewObj = KeyUtil.getJson(this);
                countViewObj.put("id", id);
                countViewObj.put("type", type+"");
                countViewObj.put("orgintable", orgintable+"");
                countViewObj.put("option", option+"");
                countViewObj.put("url", "");
                presenter.getCountView(RetrofitFactory.getRequestBody(countViewObj.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            page = 1;
            requestHot();
        }
    }
}
