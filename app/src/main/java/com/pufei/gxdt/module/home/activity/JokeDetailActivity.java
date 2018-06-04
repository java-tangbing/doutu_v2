package com.pufei.gxdt.module.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.module.home.adapter.JokeDetalAdpater;
import com.pufei.gxdt.module.home.model.JokeDetailBean;
import com.pufei.gxdt.module.home.model.JokeResultBean;
import com.pufei.gxdt.module.home.presenter.JokePresenter;
import com.pufei.gxdt.module.home.view.JokeView;
import com.pufei.gxdt.utils.AdvUtil;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.LogUtils;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.TimeUtils;
import com.pufei.gxdt.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tb on 2018/5/23.
 */

public class JokeDetailActivity extends BaseMvpActivity<JokePresenter> implements JokeView {
    @BindView(R.id.activity_jokedetail_ry)
    XRecyclerView activityJokedetailRy;
    @BindView(R.id.ll_title_left)
    LinearLayout ll_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    private JokeDetalAdpater adpater;
    private List<String> list = new ArrayList<>();
    private List<String> imagelist = new ArrayList<>();
    private TextView title,time,advert_title,advert_describe;
    JSONArray jsonObject ;
    @Override
    public void initView() {
        tv_title.setText("笑话详情");
        ll_left.setVisibility(View.VISIBLE);
        LayoutInflater lif = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerView = lif.inflate(R.layout.title_time_joke, null);
        title = (TextView) headerView.findViewById(R.id.title_time_title);
        time = (TextView) headerView.findViewById(R.id.title_time_time);
        View  footView = lif.inflate(R.layout.adver_layout,null);
        RelativeLayout relativeLayout = (RelativeLayout)footView.findViewById(R.id.your_original_layout);
        AdvUtil.getAdvHttp(this,relativeLayout,4);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT );
        headerView.setLayoutParams(layoutParams);
        footView.setLayoutParams(layoutParams);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        activityJokedetailRy.setLayoutManager(layoutManager);
        activityJokedetailRy.addHeaderView(headerView);
        activityJokedetailRy.addFootView(footView);
        activityJokedetailRy.setPullRefreshEnabled(false);
        adpater = new JokeDetalAdpater(this, list, imagelist);
        activityJokedetailRy.setAdapter(adpater);
        adpater.setOnItemClickListener(new JokeDetalAdpater.MyItemOnclick() {
            @Override
            public void OnImage(int position) {
                Intent intent = new Intent(JokeDetailActivity.this, ImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", jsonObject.toString());
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void OnlongImage(int position) {

            }
        });
    }

    @Override
    public void getData() {
        String titleString = getIntent().getExtras().getString("title");
        String timeString = getIntent().getExtras().getString("time");
        if (TextUtils.isEmpty(timeString)){
            timeString = String.valueOf(System.currentTimeMillis()/1000);
        }
        long time1 = System.currentTimeMillis()-Long.valueOf(timeString)*1000;
        time.setText(TimeUtils.longTime(time1));
        title.setText(titleString);
        if (NetWorkUtil.isNetworkConnected(JokeDetailActivity.this)) {
            String id  = getIntent().getExtras().getString("id");
            requestJokeDetail(id);
        }else{
            ToastUtils.showShort(JokeDetailActivity.this,"网络错误，请检查网络后重试");

        }
    }
    private void requestJokeDetail(String id){
        try {
            JSONObject jsonObject = KeyUtil.getJson(this);
            jsonObject.put("id", id );
            presenter.getJokDetail(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_joke_detail;
    }

    @Override
    public void setPresenter(JokePresenter presenter) {
        if (presenter == null) {
            this.presenter = new JokePresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void resultJokeList(JokeResultBean bean) {

    }

    @Override
    public void resultJokeDetail(JokeDetailBean bean) {
        if(bean!=null){
            toJson(bean.getResult().getContent());
            adpater.notifyDataSetChanged();
        }

    }
    private void toJson(List<String> content){
        jsonObject = new JSONArray();
        for (int i = 0; i < content.size(); i++) {
            String name = content.get(i);
            if (name.length() > 3) {
                if (name.contains("jpg")) {
                    list.add(name);
                    jsonObject.put(name);
                    imagelist.add(name);
                } else {
                    list.add(name);
                }
            }
        }
    }
    @OnClick(R.id.ll_title_left)
    public  void finishActivity(){
        AppManager.getAppManager().finishActivity();
    }
}
