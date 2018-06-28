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
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.home.activity.HomeImageActivity;
import com.pufei.gxdt.module.home.activity.JokeDetailActivity;
import com.pufei.gxdt.module.home.activity.ThemeImageActivity;
import com.pufei.gxdt.module.home.adapter.JokeAdapter;
import com.pufei.gxdt.module.home.model.JokeDetailBean;
import com.pufei.gxdt.module.home.model.JokeResultBean;
import com.pufei.gxdt.module.user.adapter.FavoriteAdapter;
import com.pufei.gxdt.module.user.bean.MyImagesBean;
import com.pufei.gxdt.module.user.presenter.FavoritePresenter;
import com.pufei.gxdt.module.user.view.FavoriteView;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;
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

public class FavoritePkgFragment extends BaseMvpFragment<FavoritePresenter> implements FavoriteView {
    @BindView(R.id.fav_pkg_xryv)
    XRecyclerView rl_pkg_xryv;
    @BindView(R.id.fragment_fav_pkg_smart)
    SmartRefreshLayout fragmentPkgSmart;
    @BindView(R.id.request_failed)
    LinearLayout request_failed;
    @BindView(R.id.no_data_failed)
    LinearLayout no_data_failed;
    private FavoriteAdapter jokeAdapter;
    private List<MyImagesBean.ResultBean> jokeList = new ArrayList<>();
    private int page = 1;

    @Override
    public void initView() {
        jokeAdapter = new FavoriteAdapter(getActivity(), jokeList, 3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());//布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rl_pkg_xryv.setLayoutManager(layoutManager);
        rl_pkg_xryv.setAdapter(jokeAdapter);
        fragmentPkgSmart.setRefreshHeader(new ClassicsHeader(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        fragmentPkgSmart.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        fragmentPkgSmart.setEnableLoadmore(true);
        fragmentPkgSmart.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
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
        jokeAdapter.setOnItemClickListener(new FavoriteAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                Intent intent = new Intent(getActivity(), HomeImageActivity.class);
                intent.putExtra("category_id", jokeList.get(postion).getId());
                intent.putExtra("title", jokeList.get(postion).getCategory_name());
                intent.putExtra("eyes", jokeList.get(postion).getImgs().get(0).getView());
                intent.putExtra("hot", jokeList.get(postion).getImgs().get(0).getHot());
                FavoritePkgFragment.this.startActivityForResult(intent, 1);
            }

            @Override
            public void OnLike(int position) {

            }

            @Override
            public void OnBtDelete(int position) {

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
            jsonObject.put("auth", SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH));
            jsonObject.put("type", 2);
            presenter.getFavoritePkgList(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_fav_pkg;
    }


    @Override
    public void setPresenter(FavoritePresenter presenter) {
        if (presenter == null) {
            this.presenter = new FavoritePresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void resultJokeList(MyImagesBean bean) {
        if(bean != null){
            if (page == 1) {
                jokeList.clear();
                if(bean.getResult()!=null&&bean.getResult().size()==0){
                    no_data_failed.setVisibility(View.VISIBLE);
                }
            }
            jokeList.addAll(bean.getResult());
            jokeAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void resultJokeDetail(MyImagesBean bean) {

    }

    @Override
    public void requestErrResult(String msg) {
        ToastUtils.showShort(getActivity(), msg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            page = 1;
            requestJoke(page);
        }
    }
}
