package com.pufei.gxdt.module.home.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;


import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.module.home.adapter.JokeAdapter;
import com.pufei.gxdt.module.home.model.JokeResultBean;
import com.pufei.gxdt.module.home.presenter.JokePresenter;
import com.pufei.gxdt.module.home.view.JokeView;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tb on 2018/5/23.
 */

public class JokeActivity extends BaseMvpActivity<JokePresenter> implements JokeView {
    @BindView(R.id.ll_title_left)
    LinearLayout ll_left;
    @BindView(R.id.rl_joke)
    XRecyclerView rl_joke;
    private JokeAdapter jokeAdapter;
    @Override
    public void initView() {
        ll_left.setVisibility(View.VISIBLE);
    }

    @Override
    public void getData() {
        if (NetWorkUtil.isNetworkConnected(JokeActivity.this)) {
            requestJoke();
        }
    }
    private void requestJoke() {
        try {
            JSONObject jsonObject = KeyUtil.getJson(this);
            jsonObject.put("page", 1 + "");
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
        JokeResultBean jokeListBean = bean;
        jokeAdapter = new JokeAdapter(JokeActivity.this, bean.getResult());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rl_joke.setLayoutManager(layoutManager);
        rl_joke.setAdapter(jokeAdapter);

    }
}
