package com.pufei.gxdt.module.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.module.home.adapter.ThemeImageAdpater;
import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.model.ThemeResultBean;
import com.pufei.gxdt.module.home.presenter.ThemeImagePresenter;
import com.pufei.gxdt.module.home.view.ThemeImageView;
import com.pufei.gxdt.utils.AdvUtil;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
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
import butterknife.OnClick;

/**
 * Created by tb on 2018/5/24.
 */

public class ThemeImageActivity extends BaseMvpActivity<ThemeImagePresenter> implements ThemeImageView{
    @BindView(R.id.request_failed)
    LinearLayout request_failed;
    @BindView(R.id.ll_title_left)
    LinearLayout ll_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rl_theme)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_theme)
    SmartRefreshLayout refresh_theme;
    @BindView(R.id.your_original_layout)
    RelativeLayout your_original_layout;
    private ThemeImageAdpater adpater;
    private List<ThemeResultBean.ResultBean> list = new ArrayList<>();
    private int page = 1;
    @Override
    public void initView() {
        tv_title.setText("主题表情");
        AdvUtil.getInstance().getAdvHttp(this,your_original_layout,6);
        ll_left.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adpater = new ThemeImageAdpater(ThemeImageActivity.this,list);
        recyclerView.setAdapter(adpater);
        refresh_theme.setRefreshHeader(new ClassicsHeader(this).setSpinnerStyle(SpinnerStyle.Translate));
        refresh_theme.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Translate));
        refresh_theme.setEnableLoadmore(true);
        refresh_theme.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        requestTheme(page);
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
                        requestTheme(page);
                        refreshlayout.finishRefresh();
                    }
                }, 2000);
            }
        });
        adpater.setOnItemClickListener(new ThemeImageAdpater.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                countView(list.get(postion).getId(),1,"","click");
                Intent intent = new Intent(ThemeImageActivity.this, HomeImageActivity.class);
                intent.putExtra("category_id", list.get(postion).getId());
                intent.putExtra("title", list.get(postion).getCategory_name());
                intent.putExtra("eyes",  list.get(postion).getImgs().get(0).getView());
                intent.putExtra("hot",  list.get(postion).getImgs().get(0).getHot());
                startActivity(intent);
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
                presenter.getCountView(RetrofitFactory.getRequestBody(countViewObj.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    public void getData() {
        requestTheme(page);
    }
    private void requestTheme(int page){
        if (NetWorkUtil.isNetworkConnected(ThemeImageActivity.this)) {
            try{
                JSONObject jsonObject = KeyUtil.getJson(this);
                jsonObject.put("page",page+"");
                presenter.getThemeImage(RetrofitFactory.getRequestBody(jsonObject.toString()));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }else{
            request_failed.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getLayout() {
        return R.layout.activity_theme_image;
    }

    @Override
    public void setPresenter(ThemeImagePresenter presenter) {
        if (presenter == null) {
            this.presenter = new ThemeImagePresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void resultThemeImage(ThemeResultBean bean) {
        if (bean!=null){
            if(page == 1){
                list.clear();
            }
            list.addAll(bean.getResult());
            adpater.notifyDataSetChanged();
        }

    }

    @Override
    public void resultThemeImageDetail(PictureResultBean bean) {

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

    @OnClick(R.id.ll_title_left)
    public void  finishActivity(){
        AppManager.getAppManager().finishActivity();
    }

}
