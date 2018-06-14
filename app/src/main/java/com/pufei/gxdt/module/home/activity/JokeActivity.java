package com.pufei.gxdt.module.home.activity;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.module.home.adapter.JokeAdapter;
import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.JokeDetailBean;
import com.pufei.gxdt.module.home.model.JokeResultBean;
import com.pufei.gxdt.module.home.presenter.JokePresenter;
import com.pufei.gxdt.module.home.view.JokeView;
import com.pufei.gxdt.utils.AdvUtil;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.TimeUtils;
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
 * Created by tb on 2018/5/23.
 */

public class JokeActivity extends BaseMvpActivity<JokePresenter> implements JokeView{
    @BindView(R.id.ll_title_left)
    LinearLayout ll_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rl_joke)
    XRecyclerView rl_joke;
    @BindView(R.id.fragment_joke_smart)
    SmartRefreshLayout fragmentJokeSmart;
    @BindView(R.id.request_failed)
    LinearLayout request_failed;
    @BindView(R.id.your_original_layout)
    RelativeLayout your_original_layout;
    private JokeAdapter jokeAdapter;
    private List<JokeResultBean.ResultBean> jokeList = new ArrayList<>();
    private int page = 1;
    @Override
    public void initView() {
        tv_title.setText("笑话段子");
        AdvUtil.getInstance().getAdvHttp(this,your_original_layout,4);
        ll_left.setVisibility(View.VISIBLE);
        jokeAdapter = new JokeAdapter(JokeActivity.this,jokeList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rl_joke.setLayoutManager(layoutManager);
        rl_joke.setAdapter(jokeAdapter);
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

        jokeAdapter.setOnItemClickListener(new JokeAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                    try {
                        countView(jokeList.get(postion).getId(),2,"","click");
                        Intent intent = new Intent(JokeActivity.this, JokeDetailActivity.class);
                        intent.putExtra("id",jokeList.get(postion).getId());
                        intent.putExtra("title",jokeList.get(postion).getTitle());
                        intent.putExtra("time",jokeList.get(postion).getDateline());
                        startActivity(intent);
                    } catch (NullPointerException e) {
                        jokeList.remove(postion);
                        notify();
                        e.printStackTrace();
                    }
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
        if (NetWorkUtil.isNetworkConnected(JokeActivity.this)) {
            requestJoke(page);
        }else{
            request_failed.setVisibility(View.VISIBLE);
        }
    }
    private void requestJoke(int page) {
        try {
            JSONObject jsonObject = KeyUtil.getJson(this);
            jsonObject.put("page", page + "");
            presenter.getJokeList(RetrofitFactory.getRequestBody(jsonObject.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    @Override
    public int getLayout() {
        return R.layout.activity_joke;
    }
    @OnClick(R.id.ll_title_left)
    public  void backLastActivity(){
        AppManager.getAppManager().finishActivity();
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
        if(page == 1){
            jokeList.clear();
        }
        jokeList.addAll(bean.getResult());
        jokeAdapter.notifyDataSetChanged();

    }

    @Override
    public void resultJokeDetail(JokeDetailBean bean) {

    }

    @Override
    public void resultCountView(FavoriteBean bean) {

    }
}
