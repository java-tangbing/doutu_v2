package com.pufei.gxdt.module.discover.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.module.discover.adapter.DiscoverAdapter;
import com.pufei.gxdt.module.discover.adapter.DiscoverDetailedAdapter;
import com.pufei.gxdt.module.discover.bean.DiscoverEditImageBean;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import com.pufei.gxdt.module.discover.presenter.DiscoverPresenter;
import com.pufei.gxdt.module.view.DiscoverView;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.widgets.GlideApp;
import com.pufei.gxdt.widgets.viewpager.DividerGridItemDecoration;
import com.pufei.gxdt.widgets.viewpager.GridSpacingItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DiscoverDetailedActivity extends BaseMvpActivity<DiscoverPresenter> implements DiscoverView {
    @BindView((R.id.dis_detailed_original_iv))
    ImageView originalImageView;

    @BindView((R.id.dis_detailed_count_iv))
    TextView countTextView;
    @BindView(R.id.dis_det_ry)
    RecyclerView recyclerView;
    private List<DiscoverEditImageBean.ResultBean> mlist;
    private DiscoverDetailedAdapter discoverDetailedAdapter;

    @Override
    public void initView() {
        GridLayoutManager layoutManage = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManage);
        int spanCount = 2; //  columns
        int spacing = dp2px(10); // px
        boolean includeEdge = true;
        //间距
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        //边框线
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
    }

    /**
     * convert dp to its equivalent px
     */
    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    public void getData() {
        mlist = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            DiscoverEditImageBean.ResultBean bean = new DiscoverEditImageBean().ResultBean();
//            mlist.add(bean);
//        }
        discoverDetailedAdapter = new DiscoverDetailedAdapter(mlist);
//        discoverAdapter.setOnItemClickListener(this);
//        discoverAdapter.addHeaderView(videoHeaderView);
        recyclerView.setAdapter(discoverDetailedAdapter);
        setAdapter();
    }

    private void setAdapter() {

        JSONObject jsonObject = KeyUtil.getJson(this);
        try {

            jsonObject.put("orginid", "");//orginid 原始图id
            jsonObject.put("orgintable", "");//orgintable 数据表
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.discoverEditImage(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } else {
            ToastUtils.showShort(this, "请检查网络设置");
        }

    }

    @Override
    public int getLayout() {
        return R.layout.activity_discover_det;
    }

    @Override
    public void setPresenter(DiscoverPresenter presenter) {
        if (presenter == null) {
            this.presenter = new DiscoverPresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void getDiscoverHotList(DiscoverListBean bean) {

    }

    @Override
    public void getDiscoverDetailed(DiscoverEditImageBean bean) {
        if (bean.getMsg() == "success") {
            GlideApp.with(this).load(bean.getResult().getOrgin_url()).into(originalImageView);
            countTextView.setText(bean.getResult().getCount());
        }
    }
}
