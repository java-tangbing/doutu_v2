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
import com.pufei.gxdt.contents.EventBean;
import com.pufei.gxdt.module.home.adapter.SearchDetailAdpater;
import com.pufei.gxdt.module.home.model.RecommendBean;
import com.pufei.gxdt.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tb on 2018/5/30.
 */

public class SearchDetailActivity extends BaseActivity {
    @BindView(R.id.activity_searchdetail_cancel)
    ImageView activitySearchdetailCancel;
    @BindView(R.id.activity_searchdetail_title)
    TextView activitySearchdetailTitle;
    @BindView(R.id.activity_searchdetail_rc)
    RecyclerView activitySearchdetailRc;
    @BindView(R.id.request_failed)
    LinearLayout requestFailed;
    private SearchDetailAdpater adapter;
    private List<RecommendBean.ResultBean> classiList = new ArrayList<>();
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
                try {
                    bundle.putString("category_id", classiList.get(postion).getId());
                    //bundle.putString("hot", classiList.get(postion).getImgs().get(0).getDateline());
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
        classiList.addAll(classiFicationBean.getResult());
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
            finish();
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
        finish();
    }




    @Override
    public int getLayout() {
        return R.layout.activity_searchdetail;
    }
}
