package com.pufei.gxdt.module.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.home.adapter.HomeImageAdapter;
import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.model.ThemeResultBean;
import com.pufei.gxdt.module.home.presenter.ThemeImagePresenter;
import com.pufei.gxdt.module.home.view.ThemeImageView;
import com.pufei.gxdt.module.login.activity.LoginActivity;
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
 * Created by tb on 2018/5/28.
 */

public class HomeImageActivity extends BaseMvpActivity<ThemeImagePresenter> implements ThemeImageView {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_title_left)
    LinearLayout ll_left;
    @BindView(R.id.ll_title_right)
    LinearLayout ll_right;
    @BindView(R.id.iv_title_right)
    ImageView iv_right;
    @BindView(R.id.srf_home_image)
    SmartRefreshLayout srf_home_image;
    @BindView(R.id.xrl_home_image)
    XRecyclerView xRecyclerView;
    @BindView(R.id.request_failed)
    LinearLayout request_failed;
    @BindView(R.id.btn_refresh)
    Button btn_refresh;
    private HomeImageAdapter adapter;
    private List<PictureResultBean.ResultBean> list = new ArrayList<>();
    private List<PictureResultBean.ResultBean> cashList = new ArrayList<>();
    private PictureResultBean resultBean;
    private int page = 1;
    View headView;
    @Override
    public void setPresenter(ThemeImagePresenter presenter) {
        if (presenter == null) {
            this.presenter = new ThemeImagePresenter();
            this.presenter.attachView(this);
        }
    }


    @Override
    public void initView() {
        tv_title.setText("");
        LayoutInflater lif = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headView = lif.inflate(R.layout.theme_head, null);
        xRecyclerView.addHeaderView(headView);
        RelativeLayout  relativeLayout = headView.findViewById(R.id.your_original_layout);
        AdvUtil.getInstance().getAdvHttp(this,relativeLayout,2);
        TextView tv_top_title = headView.findViewById(R.id.tv_top_title);
        TextView tv_hot = headView.findViewById(R.id.tv_hot);
        TextView tv_eyes = headView.findViewById(R.id.tv_eyes);
        tv_top_title.setText(getIntent().getExtras().getString("title"));
        tv_hot.setText(getIntent().getExtras().getString("hot"));
        tv_eyes.setText(getIntent().getExtras().getString("eyes"));

        ll_left.setVisibility(View.VISIBLE);
        ll_right.setVisibility(View.VISIBLE);
        ll_right.setBackgroundResource(R.mipmap.com_bt_ttab_star_normal);
        final GridLayoutManager layoutManager = new GridLayoutManager(HomeImageActivity.this, 3);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.addItemDecoration(new SpaceItemDecoration(10,3));
        adapter = new HomeImageAdapter(HomeImageActivity.this, list);
        xRecyclerView.setAdapter(adapter);
        srf_home_image.setRefreshHeader(new ClassicsHeader(this).setSpinnerStyle(SpinnerStyle.Translate));
        srf_home_image.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Translate));
        srf_home_image.setEnableLoadmore(false);
        xRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (layoutManager.findLastVisibleItemPosition() ==
                                layoutManager.getItemCount() - 1)
                        ) {
                    if(NetWorkUtil.isNetworkConnected(HomeImageActivity.this)){
                        if(cashList.size()>0){
                            list.addAll(cashList);
                            adapter.notifyDataSetChanged();
                            page++;
                            requestHomeImage();
                        }
                    }else{
                        ToastUtils.showShort(HomeImageActivity.this,"请检查网络设置");
                    }

                }
            }
        });
        srf_home_image.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
//                refreshlayout.getLayout().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        page++;
//                        requestHomeImage(page);
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
                        requestHomeImage();
                        try {
                            refreshlayout.finishRefresh();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);
            }
        });
        adapter.setOnItemClickListener(new HomeImageAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                countView(list.get(postion).getId(),3,list.get(postion).getOrgintable(),"click");
                Intent intent = new Intent(HomeImageActivity.this, PictureDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("picture_index", postion);
                bundle.putSerializable("picture_list", (Serializable) list);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        });
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
    @Override
    public void getData() {
        if(NetWorkUtil.isNetworkConnected(HomeImageActivity.this)){
            requestHomeImage();
        }else{
            headView.setVisibility(View.GONE);
            request_failed.setVisibility(View.VISIBLE);
            btn_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(NetWorkUtil.isNetworkConnected(HomeImageActivity.this)) {
                        headView.setVisibility(View.VISIBLE);
                        request_failed.setVisibility(View.GONE);
                        RelativeLayout  relativeLayout = headView.findViewById(R.id.your_original_layout);
                        AdvUtil.getInstance().getAdvHttp(HomeImageActivity.this,relativeLayout,2);
                        page = 1;
                        requestHomeImage();
                    }else{
                        ToastUtils.showShort(HomeImageActivity.this,"请检查网络设置");
                    }
                }
            });
        }
    }
    private void requestHomeImage(){
        if (NetWorkUtil.isNetworkConnected(HomeImageActivity.this)) {
            try {
                JSONObject jsonObject = KeyUtil.getJson(this);
                jsonObject.put("category_id", getIntent().getExtras().getString("category_id"));
                jsonObject.put("page", page + "");
                jsonObject.put("auth", SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH));
                presenter.getThemeDetail(RetrofitFactory.getRequestBody(jsonObject.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            ToastUtils.showShort(HomeImageActivity.this,"请检查网络设置");
        }
    }
    @OnClick({R.id.ll_title_left,R.id.ll_title_right})
    public  void viewClicked(View view){
        switch (view.getId()){
            case R.id.ll_title_left:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.ll_title_right:
                if(App.userBean!=null){
                    if (resultBean!=null){
                        if("0".equals(resultBean.getIsSave())){//加收藏
                            try {
                                JSONObject jsonObject = KeyUtil.getJson(this);
                                jsonObject.put("auth", SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH));
                                jsonObject.put("type", 2 + "");
                                jsonObject.put("url",  getIntent().getExtras().getString("category_id"));
                                presenter.addFavorite(RetrofitFactory.getRequestBody(jsonObject.toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{//取消收藏
                            try {
                                JSONObject jsonObject = KeyUtil.getJson(this);
                                jsonObject.put("auth", SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH));
                                jsonObject.put("type", 2 + "");
                                jsonObject.put("id", getIntent().getExtras().getString("category_id"));
                                presenter.cancleFavorite(RetrofitFactory.getRequestBody(jsonObject.toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                }else{
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
        }

    }

    @Override
    public int getLayout() {
        return R.layout.activity_home_image;
    }

    @Override
    public void resultThemeImage(ThemeResultBean bean) {

    }

    @Override
    public void resultThemeImageDetail(PictureResultBean bean) {
        if(bean!=null){
            if(page == 1){
                list.clear();
                resultBean = bean;
                if("0".equals(bean.getIsSave())){
                    ll_right.setBackgroundResource(R.mipmap.com_bt_ttab_star_normal);
                }else{
                    ll_right.setBackgroundResource(R.mipmap.com_bt_ttab_star_select);
                }
                list.addAll(bean.getResult());
                adapter.notifyDataSetChanged();
                page++;
                requestHomeImage();
            }else{
                cashList.clear();
                cashList.addAll(bean.getResult());
            }

        }

    }

    @Override
    public void resultAddFavorite(FavoriteBean bean) {
        if("0".equals(bean.getCode())){
            ll_right.setBackgroundResource(R.mipmap.com_bt_ttab_star_select);
            resultBean.setIsSave("1");
            ToastUtils.showShort(this,"收藏成功");
            Intent mIntent = new Intent();
            this.setResult(1, mIntent);
        }else{
            ToastUtils.showShort(this,bean.getMsg());
        }
    }

    @Override
    public void resultCancleFavorite(FavoriteBean bean) {
        if("0".equals(bean.getCode())){
            ll_right.setBackgroundResource(R.mipmap.com_bt_ttab_star_normal);
            resultBean.setIsSave("0");
            ToastUtils.showShort(this,"取消收藏成功");
            Intent mIntent = new Intent();
            this.setResult(1, mIntent);
        }else{
            ToastUtils.showShort(this,bean.getMsg());
        }
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
            requestHomeImage();
        }
    }
}
