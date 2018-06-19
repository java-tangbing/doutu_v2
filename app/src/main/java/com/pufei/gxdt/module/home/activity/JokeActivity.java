package com.pufei.gxdt.module.home.activity;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.home.adapter.JokeAdvAdapter;
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
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;
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

public class JokeActivity extends BaseMvpActivity<JokePresenter> implements JokeView, NativeExpressAD.NativeExpressADListener{
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
//    @BindView(R.id.container)
//    RelativeLayout container;
    private JokeAdvAdapter jokeAdapter;
    private List<JokeResultBean.ResultBean> jokeList = new ArrayList<>();
    private int page = 1;
    private NativeExpressADView nativeExpressADView;
    private NativeExpressAD nativeExpressAD;
    private List<NativeExpressADView> adLists = new ArrayList<>();
    @Override
    public void initView() {
        refreshAd();
        tv_title.setText("笑话段子");
        AdvUtil.getInstance().getAdvHttp(this,your_original_layout,4);
        ll_left.setVisibility(View.VISIBLE);
        jokeAdapter = new JokeAdvAdapter(JokeActivity.this,jokeList,adLists);
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

        jokeAdapter.setOnItemClickListener(new JokeAdvAdapter.MyItemClickListener() {
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

    @Override
    public void requestErrResult(String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (nativeExpressADView != null) {
            nativeExpressADView.destroy();
        }
    }

    //广告
    private void refreshAd() {
        nativeExpressAD = new NativeExpressAD(this, new ADSize(ADSize.FULL_WIDTH, 100), Contents.TENCENT_ID, Contents.NativeExpressPosID, this); // 传入Activity
        // 注意：如果您在联盟平台上新建原生模板广告位时，选择了“是”支持视频，那么可以进行个性化设置（可选）
//        nativeExpressAD.setVideoOption(new VideoOption.Builder()
//                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI环境下可以自动播放视频
//                .setAutoPlayMuted(true) // 自动播放时为静音
//                .build()); //
        nativeExpressAD.loadAD(10);
    }

    @Override
    public void onNoAD(AdError adError) {
        Log.i("tb", "onError: " + adError.getErrorMsg()+"..."+adError.getErrorCode());
    }

    @Override
    public void onADLoaded(List<NativeExpressADView> adList) {
        Log.i("tb", "onADLoaded: " + adList.size());
        // 释放前一个展示的NativeExpressADView的资源
          adLists.clear();
//        if (nativeExpressADView != null) {
//            nativeExpressADView.destroy();
//        }
//        nativeExpressADView = adList.get(0);
//        for(int i = 0;i<adList.size();i++){
            adLists.addAll(adList);
            jokeAdapter.notifyDataSetChanged();
//        }

//        if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
//            nativeExpressADView.setMediaListener(mediaListener);
//        }
        // 广告可见才会产生曝光，否则将无法产生收益。
//        container.addView(nativeExpressADView);
//        nativeExpressADView.render();
    }

    @Override
    public void onRenderFail(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADExposure(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADClicked(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADClosed(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

    }
/*    广告视频监听*/
//    private NativeExpressMediaListener mediaListener = new NativeExpressMediaListener() {
//        @Override
//        public void onVideoInit(NativeExpressADView nativeExpressADView) {
//            Log.i("tb", "onVideoInit: ");
//        }
//
//        @Override
//        public void onVideoLoading(NativeExpressADView nativeExpressADView) {
//            Log.i("tb", "onVideoLoading");
//        }
//
//        @Override
//        public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {
//            Log.i("tb", "onVideoReady");
//        }
//
//        @Override
//        public void onVideoStart(NativeExpressADView nativeExpressADView) {
//            Log.i("tb", "onVideoStart: ");
//        }
//
//        @Override
//        public void onVideoPause(NativeExpressADView nativeExpressADView) {
//            Log.i(TAG, "onVideoPause: ");
//        }
//
//        @Override
//        public void onVideoComplete(NativeExpressADView nativeExpressADView) {
//            Log.i("tb", "onVideoComplete: ");
//        }
//
//        @Override
//        public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {
//            Log.i(TAG, "onVideoError");
//        }
//
//        @Override
//        public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {
//            Log.i(TAG, "onVideoPageOpen");
//        }
//
//        @Override
//        public void onVideoPageClose(NativeExpressADView nativeExpressADView) {
//            Log.i(TAG, "onVideoPageClose");
//        }
//    };

}
