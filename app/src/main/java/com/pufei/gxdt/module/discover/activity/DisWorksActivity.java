package com.pufei.gxdt.module.discover.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.module.discover.adapter.DisWorksdAdapter;
import com.pufei.gxdt.module.discover.bean.DisWorksBean;
import com.pufei.gxdt.module.discover.presenter.DisWorksPresenter;
import com.pufei.gxdt.module.discover.view.DisWorksView;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.widgets.viewpager.GridSpacingItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class DisWorksActivity extends BaseMvpActivity<DisWorksPresenter> implements DisWorksView {

    @BindView(R.id.dis_works_ry)
    RecyclerView recyclerView;
    private DisWorksdAdapter disWorksdAdapter;
    private List<DisWorksBean> mlist;

    @Override
    public void initView() {
        GridLayoutManager layoutManage = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManage);
        int spanCount = 3; //  columns
        int spacing = dp2px(5); // px
        boolean includeEdge = true;
        //间距
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


    }

    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    @Override
    public void getData() {
        mlist = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DisWorksBean bean = new DisWorksBean();
            mlist.add(bean);
        }
        disWorksdAdapter = new DisWorksdAdapter(mlist);
//        disWorksdAdapter.setOnItemClickListener(this);
//        discoverAdapter.addHeaderView(videoHeaderView);
        recyclerView.setAdapter(disWorksdAdapter);
        setAdapter();
    }

    private void setAdapter() {
//        JSONObject jsonObject = KeyUtil.getJson(this);
//        try {
//            jsonObject.put("id", id);
//            jsonObject.put("orginid", orginid);//orginid 原始图id
//            jsonObject.put("orgintable", orgintable);//orgintable 数据�
//            jsonObject.put("uid", uid);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if (NetWorkUtil.isNetworkConnected(this)) {
//            presenter.discoverEditImage(RetrofitFactory.getRequestBody(jsonObject.toString()));
//        } else {
//            ToastUtils.showShort(this, "请检查网络设置");
//        }

    }

    @Override
    public int getLayout() {
        return R.layout.activity_disworks;
    }


    @Override
    public void setPresenter(DisWorksPresenter presenter) {
        if (presenter == null) {
            this.presenter = new DisWorksPresenter();
            this.presenter.attachView(this);
        }
    }
}
