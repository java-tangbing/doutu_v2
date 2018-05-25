package com.pufei.gxdt.module.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.module.home.adapter.ThemeImageAdpater;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.model.ThemeResultBean;
import com.pufei.gxdt.module.home.presenter.ThemeImagePresenter;
import com.pufei.gxdt.module.home.view.ThemeImageView;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

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
    private ThemeImageAdpater adpater;
    private List<ThemeResultBean.ResultBean> list = new ArrayList<>();
    @Override
    public void initView() {
        tv_title.setText("主题表情");
        ll_left.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adpater = new ThemeImageAdpater(ThemeImageActivity.this,list);
        recyclerView.setAdapter(adpater);
        refresh_theme.setLoadmoreFinished(true);
        refresh_theme.setRefreshHeader(new ClassicsHeader(ThemeImageActivity.this).setSpinnerStyle(SpinnerStyle.Translate));
        refresh_theme.setEnableLoadmore(false);
        refresh_theme.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
                refreshlayout.setLoadmoreFinished(true);
            }

            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestTheme();
                        refreshlayout.finishRefresh();
                    }
                }, 2000);
            }
        });
        adpater.setOnItemClickListener(new ThemeImageAdpater.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                Intent intent = new Intent(ThemeImageActivity.this, PictureActivity.class);
                Bundle bundle = new Bundle();
                try {
                    bundle.putString("id", list.get(postion).getId());
                    bundle.putString("time", list.get(postion).getDateline());
                    bundle.putString("title", list.get(postion).getCategory_name());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void getData() {
        if (NetWorkUtil.isNetworkConnected(ThemeImageActivity.this)) {
            requestTheme();
        }else{
            request_failed.setVisibility(View.VISIBLE);
        }
    }
    private void requestTheme(){
        JSONObject jsonObject = KeyUtil.getJson(this);
        presenter.getThemeImage(RetrofitFactory.getRequestBody(jsonObject.toString()));
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
            list.clear();
            list.addAll(bean.getResult());
            adpater.notifyDataSetChanged();
        }

    }

    @Override
    public void resultThemeImageDetail(PictureResultBean bean) {

    }

    @OnClick(R.id.ll_title_left)
    public void  finishActivity(){
        AppManager.getAppManager().finishActivity();
    }

}
