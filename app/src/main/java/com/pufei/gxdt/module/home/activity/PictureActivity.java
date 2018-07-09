package com.pufei.gxdt.module.home.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.home.adapter.HotAdapter;
import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.model.ThemeResultBean;
import com.pufei.gxdt.module.home.presenter.ThemeImagePresenter;
import com.pufei.gxdt.module.home.view.ThemeImageView;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;
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

/**
 * Created by tb on 2018/5/23.
 */

public class PictureActivity extends BaseMvpActivity <ThemeImagePresenter> implements ThemeImageView{
    @BindView(R.id.request_failed)
    LinearLayout request_failed;
    @BindView(R.id.rl_theme)
    XRecyclerView rl_theme;
    @BindView(R.id.srl_theme_detail)
    SmartRefreshLayout srl_theme_detail;
    @BindView(R.id.tv_theme_name)
    TextView tv_theme_name;
    @BindView(R.id.tv_content)
    TextView tv_content;
    private HotAdapter adapter;
    private List<PictureResultBean.ResultBean> list = new ArrayList<>();
    private List<PictureResultBean.ResultBean> cashList = new ArrayList<>();
    private int page = 1;
    private String id,title,timeString;

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        title = bundle.getString("title");
        timeString = bundle.getString("desc");
        final GridLayoutManager layoutManager = new GridLayoutManager(PictureActivity.this, 3);
        rl_theme.setLayoutManager(layoutManager);
        adapter = new HotAdapter(PictureActivity.this, list);
        rl_theme.setPullRefreshEnabled(false);
        rl_theme.addItemDecoration(new SpaceItemDecoration(0,3));
        rl_theme.setAdapter(adapter);
        srl_theme_detail.setRefreshHeader(new ClassicsHeader(this).setSpinnerStyle(SpinnerStyle.Translate));
        srl_theme_detail.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Translate));
        srl_theme_detail.setEnableLoadmore(false);
        rl_theme.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (layoutManager.findLastVisibleItemPosition() ==
                                layoutManager.getItemCount() - 1)
                        ) {
                    if(cashList.size()>0){
                        list.addAll(cashList);
                        adapter.notifyDataSetChanged();
                        cashList.clear();
                        page++;
                        requestThemeDetail(page);
                    }
                }
            }
        });
        srl_theme_detail.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
//                refreshlayout.getLayout().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        page++;
//                        requestThemeDetail(page);
//                        try {
//                            refreshlayout.finishLoadmore();
//                        } catch (NullPointerException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, 2000);
            }

            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        requestThemeDetail(page);
                        try {
                            refreshlayout.finishRefresh();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);
            }
        });

        adapter.setOnItemClickListener(new HotAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                Intent intent = new Intent(PictureActivity.this, PictureDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("picture_index", postion);
                bundle.putSerializable("picture_list", (Serializable) list);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void getData() {
        tv_theme_name.setText(title);
        tv_content.setText(timeString);
        if (NetWorkUtil.isNetworkConnected(PictureActivity.this)) {
            requestThemeDetail(page);
        }else{
            request_failed.setVisibility(View.VISIBLE);
        }
    }
    private void requestThemeDetail(int page){
        if (NetWorkUtil.isNetworkConnected(PictureActivity.this)) {
            JSONObject jsonObject = KeyUtil.getJson(this);
            try {
                jsonObject.put("category_id", id);
                jsonObject.put("page", String.valueOf(page));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            presenter.getThemeDetail(RetrofitFactory.getRequestBody(jsonObject.toString()));
        }else{
            ToastUtils.showShort(this,"请检查网络设置");
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_picture;
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

    }

    @Override
    public void resultThemeImageDetail(PictureResultBean bean) {
        if(bean!=null){
            if(page == 1){
                list.clear();
                list.addAll(bean.getResult());
                adapter.notifyDataSetChanged();
                page++;
                requestThemeDetail(page);
            }else{
                cashList.addAll(bean.getResult());
            }

        }

    }

    @Override
    public void resultAddFavorite(FavoriteBean bean) {

    }

    @Override
    public void resultCancleFavorite(FavoriteBean bean) {

    }

    @Override
    public void resultCountView(FavoriteBean bean) {

    }

    @Override
    public void requestErrResult(String msg) {

    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

}
