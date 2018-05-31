package com.pufei.gxdt.module.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.discover.activity.DiscoverDetailedActivity;
import com.pufei.gxdt.module.news.adapter.NewsPictureAdapter;

import com.pufei.gxdt.module.news.bean.NewsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NewsPictureActivity extends BaseMvpActivity implements BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.news_picture_rv)
    RecyclerView recyclerView;
    NewsPictureAdapter newsPictureAdapter;
    private List<NewsBean.ResultBean> mlist;

    @Override
    public void initView() {
        LinearLayoutManager layoutManage = new LinearLayoutManager(this);
        layoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManage);
    }

    @Override
    public void getData() {
        mlist = new ArrayList<>();
        newsPictureAdapter = new NewsPictureAdapter(mlist);
//        newsSystemAdapter.setOnItemClickListener(this);
//        discoverAdapter.addHeaderView(videoHeaderView);
        newsPictureAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(newsPictureAdapter);
//        setAdapter();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_news_picture;
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
//        this.presenter = presenter;

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.news_picture_item:
//                LinearLayout linearLayout = (LinearLayout) view.getParent();
//                LinearLayout itemLinearLayout = linearLayout.findViewById(R.id.news_picture_item);
//                adapter.getViewByPosition( recyclerView, position, R.id.news_picture_item_user);//获取其他子控件
                Intent intent = new Intent(this, DiscoverDetailedActivity.class);
                Bundle bundle = new Bundle();   //得到一个 bundle对象
                bundle.putInt("account", mlist.get(position).getItemType());
//                bundle.putCharSequence("password", pwd);
                intent.putExtras(bundle);  //将 bundle对象的值放入 intent , 以便下一个页面的 intent 接收
                startActivity(intent);
                break;
        }
    }
}
