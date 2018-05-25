package com.pufei.gxdt.module.home.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.module.home.adapter.ThemeImageAdpater;
import com.pufei.gxdt.module.home.model.ThemeResultBean;
import com.pufei.gxdt.module.home.presenter.ThemeImagePresenter;
import com.pufei.gxdt.module.home.view.ThemeImageView;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
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
    private ThemeImageAdpater adpater;
    private List<ThemeResultBean.ResultBean> list = new ArrayList<>();
    @Override
    public void initView() {
        tv_title.setText("主题表情");
        ll_left.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adpater = new ThemeImageAdpater(ThemeImageActivity.this,list);
        recyclerView.setAdapter(adpater);
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
            list.addAll(bean.getResult());
            adpater.notifyDataSetChanged();
        }

    }
    @OnClick(R.id.ll_title_left)
    public void  finishActivity(){
        AppManager.getAppManager().finishActivity();
    }

}
