package com.pufei.gxdt.module.user.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpFragment;
import com.pufei.gxdt.module.home.activity.JokeDetailActivity;
import com.pufei.gxdt.module.home.adapter.JokeAdapter;
import com.pufei.gxdt.module.home.model.JokeDetailBean;
import com.pufei.gxdt.module.home.model.JokeResultBean;
import com.pufei.gxdt.module.user.presenter.FavoritePresenter;
import com.pufei.gxdt.module.user.view.FavoriteView;
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

public class FavoriteJokeFragment extends BaseMvpFragment<FavoritePresenter> implements FavoriteView {
    @BindView(R.id.ll_title_left)
    LinearLayout ll_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.fav_joke_xryv)
    XRecyclerView rl_joke_xryv;
    @BindView(R.id.fragment_fav_joke_smart)
    SmartRefreshLayout fragmentJokeSmart;
    @BindView(R.id.request_failed)
    LinearLayout request_failed;
    private JokeAdapter jokeAdapter;
    private List<JokeResultBean.ResultBean> jokeList = new ArrayList<>();
    private int page = 1;

    @Override
    public void initView() {
        jokeAdapter = new JokeAdapter(getActivity(), jokeList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());//布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rl_joke_xryv.setLayoutManager(layoutManager);
        rl_joke_xryv.setAdapter(jokeAdapter);
        fragmentJokeSmart.setRefreshHeader(new ClassicsHeader(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        fragmentJokeSmart.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
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
                if (jokeList.get(postion).getType() == 0) {
                    try {
                        Intent intent = new Intent(getActivity(), JokeDetailActivity.class);
                        intent.putExtra("id", jokeList.get(postion).getId());
                        intent.putExtra("title", jokeList.get(postion).getTitle());
                        intent.putExtra("time", jokeList.get(postion).getDateline());
                        startActivity(intent);
                    } catch (NullPointerException e) {
                        jokeList.remove(postion);
                        notify();
                        e.printStackTrace();
                    }
                } else {
                    if (!TextUtils.isEmpty(jokeList.get(postion).getAdvert_url())) {
//                        Intent intent = new Intent(activity, WebAdvertActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("URL", jokeList.get(postion).getAdvert_url());
//                        bundle.putString("source", "start");
//                        intent.putExtras(bundle);
//                        startActivity(intent);
                    } else if (!TextUtils.isEmpty(jokeList.get(postion).getDown_url())) {
                        // AgentUtils.getAgentUtils().getAgent(requestFailed, activity, jokeList.get(postion).getDown_url());

                    }
                }

            }

            @Override
            public void OnLike(int position) {
                //showShare("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=false&word=%E6%99%AF%E7%94%9C&step_word=&hs=0&pn=40&spn=0&di=100938213560&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=1636489337%2C340249600&os=4078026530%2C4243386462&simid=0%2C0&adpicid=0&ln=3946&fr=&fmq=1482737443249_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=star&bdtype=11&oriquery=&objurl=http%3A%2F%2Fpic.yesky.com%2FuploadImages%2F2016%2F324%2F13%2F9973OGM66IW9.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Frtv_z%26e3Byjfhy_z%26e3Bv54AzdH3FkkfAzdH3Fpi6jw1-nnm8ca-8-8_z%26e3Bip4s&gsm=0&rpstart=0&rpnum=0");
            }

            @Override
            public void OnBtDelete(int position) {
                Toast.makeText(getActivity(), "已收藏", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void getData() {
        if (NetWorkUtil.isNetworkConnected(getActivity())) {
            requestJoke(page);
        } else {
            request_failed.setVisibility(View.VISIBLE);
        }
    }

    private void requestJoke(int page) {
        try {
            JSONObject jsonObject = KeyUtil.getJson(getActivity());
            jsonObject.put("page", page + "");
            presenter.getJokeList(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_fav_joke;
    }

    @OnClick(R.id.ll_title_left)
    public void backLastActivity() {
        AppManager.getAppManager().finishActivity();
    }

    @Override
    public void resultJokeList(JokeResultBean bean) {
        if (page == 1) {
            jokeList.clear();
        }
        jokeList.addAll(bean.getResult());
        jokeAdapter.notifyDataSetChanged();
    }

    @Override
    public void resultJokeDetail(JokeDetailBean bean) {

    }

    @Override
    public void setPresenter(FavoritePresenter presenter) {
        if (presenter == null) {
            this.presenter = new FavoritePresenter();
            this.presenter.attachView(this);
        }
    }
}
