package com.pufei.gxdt.module.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.EventBean;
import com.pufei.gxdt.module.home.adapter.SearchDetailAdpater;
import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.model.RecommendBean;
import com.pufei.gxdt.module.home.model.ThemeResultBean;
import com.pufei.gxdt.module.home.presenter.ThemeImagePresenter;
import com.pufei.gxdt.module.home.view.ThemeImageView;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.ToastUtils;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tb on 2018/5/30.
 */

public class SearchDetailActivity extends BaseMvpActivity<ThemeImagePresenter> implements ThemeImageView {
    @BindView(R.id.activity_searchdetail_cancel)
    ImageView activitySearchdetailCancel;
    @BindView(R.id.activity_searchdetail_title)
    TextView activitySearchdetailTitle;
    @BindView(R.id.activity_searchdetail_rc)
    RecyclerView activitySearchdetailRc;
    @BindView(R.id.request_failed)
    LinearLayout requestFailed;
    private SearchDetailAdpater adapter;
    private List<RecommendBean.ResultBean> classiList = new ArrayList<>(); ;
    private String result;
    private String name;
    @Override
    public void initView() {
        adapter = new SearchDetailAdpater(this, classiList, Glide.with(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        activitySearchdetailRc.setLayoutManager(layoutManager);
        activitySearchdetailRc.setAdapter(adapter);
        adapter.setOnItemClickListener(new SearchDetailAdpater.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                Intent intent = new Intent(SearchDetailActivity.this, HomeImageActivity.class);
                Bundle bundle = new Bundle();
                countView(classiList.get(postion).getId(),1,"","click");
                try {
                    bundle.putString("category_id", classiList.get(postion).getId());
                    bundle.putString("hot",classiList.get(postion).getHot());
                    bundle.putString("eyes",classiList.get(postion).getView());
                    bundle.putString("title", classiList.get(postion).getCategory_name());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (IndexOutOfBoundsException e) {
                    ToastUtils.showShort(SearchDetailActivity.this, "点击失败 请重新点击");
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void getData() {
        Bundle bundle = getIntent().getExtras();
        result = bundle.getString("search_detail");
        name = bundle.getString("name");
        RecommendBean classiFicationBean = new Gson().fromJson(result, RecommendBean.class);
        if(classiFicationBean != null){
            classiList.addAll(classiFicationBean.getResult());
        }else {
            classiList.clear();
        }
        if (classiList!=null&&classiList.size()==0){
            requestFailed.setVisibility(View.VISIBLE);
        }
        if (name != null) {
            activitySearchdetailTitle.setText(name);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppManager.getAppManager().finishActivity();
            EventBus.getDefault().post(new EventBean("search_detail", null));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().post(new EventBean("search_detail", null));
        super.onDestroy();
    }

    @OnClick(R.id.activity_searchdetail_cancel)
    public void onViewClicked() {
        AppManager.getAppManager().finishActivity();
    }




    @Override
    public int getLayout() {
        return R.layout.activity_searchdetail;
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
}
