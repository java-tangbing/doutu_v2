package com.pufei.gxdt.module.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.home.activity.HomeImageActivity;
import com.pufei.gxdt.module.home.activity.JokeDetailActivity;
import com.pufei.gxdt.module.home.activity.PictureDetailActivity;
import com.pufei.gxdt.module.home.adapter.HomeImageAdapter;
import com.pufei.gxdt.module.home.adapter.HotAdapter;
import com.pufei.gxdt.module.home.adapter.JokeAdapter;
import com.pufei.gxdt.module.home.model.JokeResultBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.presenter.JokePresenter;
import com.pufei.gxdt.module.user.adapter.FavoriteJokeAdapter;
import com.pufei.gxdt.module.user.bean.MyImagesBean;
import com.pufei.gxdt.module.user.presenter.PublishPresenter;
import com.pufei.gxdt.module.user.view.PublishView;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
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


public class PublishActivity extends BaseMvpActivity<PublishPresenter> implements PublishView {

    @BindView(R.id.ll_title_left)
    LinearLayout ll_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.publish_xryv)
    XRecyclerView rl_publish;
    @BindView(R.id.fragment_publish_smart)
    SmartRefreshLayout fragmentJokeSmart;
    @BindView(R.id.request_failed)
    LinearLayout request_failed;
    private HomeImageAdapter jokeAdapter;
    private List<PictureResultBean.ResultBean> jokeList = new ArrayList<>();
    private int page = 1;

    @Override
    public void initView() {
        tv_title.setText("我的发布");
        ll_left.setVisibility(View.VISIBLE);
        jokeAdapter = new HomeImageAdapter(PublishActivity.this,jokeList);
        rl_publish.setLayoutManager(new GridLayoutManager(PublishActivity.this, 3));
        rl_publish.addItemDecoration(new SpaceItemDecoration(dp2px(PublishActivity.this, 10)));
        rl_publish.setAdapter(jokeAdapter);
        fragmentJokeSmart.setRefreshHeader(new ClassicsHeader(this).setSpinnerStyle(SpinnerStyle.Translate));
        fragmentJokeSmart.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Translate));
        fragmentJokeSmart.setEnableLoadmore(true);
        fragmentJokeSmart.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        requestJoke(page);
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
                        requestJoke(page);
                        try {
                            refreshlayout.finishRefresh();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);
            }
        });

        jokeAdapter.setOnItemClickListener(new HomeImageAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                Intent intent = new Intent(PublishActivity.this, PictureDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("picture_index", postion);
                bundle.putSerializable("picture_list", (Serializable) jokeList);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);

            }

            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onAdd(int position) {

            }
        });
    }

    @Override
    public void getData() {
        if (NetWorkUtil.isNetworkConnected(PublishActivity.this)) {
            requestJoke(page);
        }else{
            request_failed.setVisibility(View.VISIBLE);
        }
    }

    private void requestJoke(int page) {
        try {
            JSONObject jsonObject = KeyUtil.getJson(this);
            jsonObject.put("page", page + "");
            jsonObject.put("auth", SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH));
            presenter.getPublish(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getLayout() {
        return R.layout.activity_publish;
    }

    @OnClick(R.id.ll_title_left)
    public  void backLastActivity(){
        AppManager.getAppManager().finishActivity();
    }

    @Override
    public void resultPublish(PictureResultBean bean) {
        if(page == 1){
            jokeList.clear();
        }
        jokeList.addAll(bean.getResult());
        jokeAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(PublishPresenter presenter) {
        if (presenter == null) {
            this.presenter = new PublishPresenter();
            this.presenter.attachView(this);
        }
    }
    private  int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            page = 1;
            requestJoke(page);
        }
    }
}
