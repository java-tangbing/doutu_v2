package com.pufei.gxdt.module.user.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.home.activity.PictureDetailActivity;
import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.user.adapter.PublishAdapter;
import com.pufei.gxdt.module.user.presenter.PublishPresenter;
import com.pufei.gxdt.module.user.view.PublishView;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.utils.UmengStatisticsUtil;
import com.pufei.gxdt.widgets.MyFrontTextView;
import com.pufei.gxdt.widgets.SpaceItemDecoration;
import com.pufei.gxdt.widgets.popupwindow.CommonPopupWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class PublishActivity extends BaseMvpActivity<PublishPresenter> implements PublishView {

    @BindView(R.id.ll_title_left)
    LinearLayout ll_left;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.publish_xryv)
    XRecyclerView rl_publish;
    @BindView(R.id.fragment_publish_smart)
    SmartRefreshLayout fragmentJokeSmart;
    @BindView(R.id.request_failed)
    LinearLayout request_failed;
    @BindView(R.id.btn_refresh)
    Button btn_refresh;
    @BindView(R.id.no_data_failed)
    LinearLayout no_data_failed;
    @BindView(R.id.main_bg)
    LinearLayout main_bg;
    private PublishAdapter jokeAdapter;
    private List<PictureResultBean.ResultBean> jokeList = new ArrayList<>();
    private List<PictureResultBean.ResultBean> cashList = new ArrayList<>();
    private int page = 1;
    private int show = 1, index;
    private View headView;
    private AlertDialog sharedialog;
    private CommonPopupWindow popupWindow;

    @Override
    public void initView() {
        tv_title.setText("我的发布");
        ll_left.setVisibility(View.VISIBLE);
        LayoutInflater lif = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headView = lif.inflate(R.layout.publish_head, null);
        rl_publish.addHeaderView(headView);
        jokeAdapter = new PublishAdapter(PublishActivity.this, jokeList);
        final GridLayoutManager layoutManager = new GridLayoutManager(PublishActivity.this, 3);
        rl_publish.setLayoutManager(layoutManager);
        rl_publish.addItemDecoration(new SpaceItemDecoration(10, 3));
        rl_publish.setAdapter(jokeAdapter);
        fragmentJokeSmart.setRefreshHeader(new ClassicsHeader(this).setSpinnerStyle(SpinnerStyle.Translate));
        fragmentJokeSmart.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Translate));
        fragmentJokeSmart.setEnableLoadmore(false);
//        fragmentJokeSmart.setEnableLoadmoreWhenContentNotFull(true);
        rl_publish.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (layoutManager.findLastVisibleItemPosition() ==
                                layoutManager.getItemCount() - 1)
                        ) {
                    if (NetWorkUtil.isNetworkConnected(PublishActivity.this)) {
                        if (cashList.size() > 0) {
                            jokeList.addAll(cashList);
                            jokeAdapter.notifyDataSetChanged();
                            cashList.clear();
                            page++;
                            requestJoke(page);
                        }
                    } else {
                        ToastUtils.showShort(PublishActivity.this, "请检查网络设置");
                    }

                }
            }
        });
        fragmentJokeSmart.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
//                refreshlayout.getLayout().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        page++;
//                        requestJoke(page);
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

        jokeAdapter.setOnItemClickListener(new PublishAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                Intent intent = new Intent(PublishActivity.this, PictureDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("picture_index", postion);
                bundle.putSerializable("picture_list", (Serializable) jokeList);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);

            }
        });
        jokeAdapter.setOnItemLongClickListener(new PublishAdapter.MyItemLongClickListener() {


            @Override
            public void setOnItemLongClickListener(View itemview, View view, int postion) {
                index = postion;
                showDialog(jokeList.get(postion).getUrl(), Integer.parseInt(jokeList.get(postion).getIs_show()), jokeList.get(postion).getId());
            }
        });
    }

    private void showDialog(final String url, final int isShow, final String imageId) {
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.publish_dialog)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)
                .setAnimationStyle(R.style.anim_menu_pop)
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(final View view, int layoutResId) {
                        MyFrontTextView textView = view.findViewById(R.id.tv_isShow);
                        if (isShow == 1) {
                            textView.setText("设为不可见 ");
                            show = 0;
                        } else {
                            textView.setText("设为公开可见");
                            show = 1;
                        }
                        view.findViewById(R.id.rl_is_see).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requstShowPublish(show, imageId);
                                popupWindow.dismiss();
                            }
                        });
                        view.findViewById(R.id.rl_delete).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requstDeletePublish(imageId);
                                popupWindow.dismiss();
                            }
                        });
                        view.findViewById(R.id.rl_share_wx).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ActivityCompat.checkSelfPermission(PublishActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    openPermissin();
                                } else {
                                    WXshowShare(url, SHARE_MEDIA.WEIXIN);
                                }
                            }
                        });
                        view.findViewById(R.id.rl_share_qq).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ActivityCompat.checkSelfPermission(PublishActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    openPermissin();
                                } else {
                                    QQshowShare(url, SHARE_MEDIA.QQ);
                                }
                            }
                        });

                    }
                })
                .setOutsideTouchable(true)
                .create();
        popupWindow.showAtLocation(this.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }

    @Override
    public void getData() {
        if (NetWorkUtil.isNetworkConnected(PublishActivity.this)) {
            requestJoke(page);
        } else {
            headView.setVisibility(View.GONE);
            request_failed.setVisibility(View.VISIBLE);
            main_bg.setBackgroundColor(getResources().getColor(R.color.select_color22));
            btn_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetWorkUtil.isNetworkConnected(PublishActivity.this)) {
                        headView.setVisibility(View.VISIBLE);
                        request_failed.setVisibility(View.GONE);
                        main_bg.setBackgroundColor(getResources().getColor(R.color.white));
                        page = 1;
                        requestJoke(page);
                    } else {
                        ToastUtils.showShort(PublishActivity.this, "请先打开网络连接");
                    }
                }
            });
        }
    }

    private void requestJoke(int page) {
        if (NetWorkUtil.isNetworkConnected(PublishActivity.this)) {
            try {
                JSONObject jsonObject = KeyUtil.getJson(this);
                jsonObject.put("page", page + "");
                presenter.getPublish(RetrofitFactory.getRequestBody(jsonObject.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            ToastUtils.showShort(this, "请检查网络设置");
        }

    }

    private void requstDeletePublish(String id) {
        if (NetWorkUtil.isNetworkConnected(PublishActivity.this)) {
            try {
                JSONObject jsonObject = KeyUtil.getJson(this);
                jsonObject.put("id", id);
                presenter.delMyDesignImages(RetrofitFactory.getRequestBody(jsonObject.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            ToastUtils.showShort(this, "请检查网络设置");
        }
    }

    private void requstShowPublish(int isShow, String id) {
        if (NetWorkUtil.isNetworkConnected(PublishActivity.this)) {
            try {
                JSONObject jsonObject = KeyUtil.getJson(this);
                jsonObject.put("id", id);
                jsonObject.put("is_show", isShow + "");
                presenter.setMyDesignImages(RetrofitFactory.getRequestBody(jsonObject.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            ToastUtils.showShort(this, "请检查网络设置");
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_publish;
    }

    @OnClick(R.id.ll_title_left)
    public void backLastActivity() {
        AppManager.getAppManager().finishActivity();
    }

    @Override
    public void resultPublish(PictureResultBean bean) {
        if (bean != null) {
            if (page == 1) {
                jokeList.clear();
                if (bean.getResult() != null && bean.getResult().size() == 0) {
                    no_data_failed.setVisibility(View.VISIBLE);
                    headView.setVisibility(View.GONE);
                    main_bg.setBackgroundColor(getResources().getColor(R.color.select_color22));
                } else {
                    jokeList.addAll(bean.getResult());
                    jokeAdapter.notifyDataSetChanged();
                    page++;
                    requestJoke(page);
                }
            } else {
                cashList.addAll(bean.getResult());
            }

        }

    }

    @Override
    public void requestErrResult(String msg) {
        ToastUtils.showShort(this, msg);
    }

    @Override
    public void setMyDesignImagesResult(FavoriteBean bean) {
        if (bean != null) {
            if (show == 1) {
                jokeList.get(index).setIs_show("1");
            } else {
                jokeList.get(index).setIs_show("0");
            }
            jokeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void delMyDesignImagesResult(FavoriteBean bean) {
        if (bean != null) {
            jokeList.remove(index);
            jokeAdapter.notifyItemRemoved(index);
            jokeAdapter.notifyDataSetChanged();
            if (jokeList.size() == 0) {
                headView.setVisibility(View.GONE);
                no_data_failed.setVisibility(View.VISIBLE);
                main_bg.setBackgroundColor(getResources().getColor(R.color.select_color22));
            }
        }
    }

    @Override
    public void setPresenter(PublishPresenter presenter) {
        if (presenter == null) {
            this.presenter = new PublishPresenter();
            this.presenter.attachView(this);
        }
    }

    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    private void openPermissin() {
        Acp.getInstance(this)
                .request(new AcpOptions.Builder().setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {

                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                ToastUtils.showShort(PublishActivity.this, "请求权限失败,请手动开启！");
                            }
                        });
    }

    private void QQshowShare(String URL, SHARE_MEDIA share_media) {//分享
        if (share_media.equals(SHARE_MEDIA.WEIXIN)) {
            UmengStatisticsUtil.statisticsEvent(this, "16");
        } else if (share_media.equals(SHARE_MEDIA.QQ)) {
            UmengStatisticsUtil.statisticsEvent(this, "14");
        }

        if (URL != null) {
            UMImage image = null;
            if (URL.contains("http")) {
                image = new UMImage(this, URL);
            } else {
                image = new UMImage(this, BitmapFactory.decodeFile(URL));
            }
            try {
                new ShareAction(this).withMedia(image)
                        .setPlatform(share_media)
                        .setCallback(umShareListener).share();
            } catch (NullPointerException e) {
                ToastUtils.showShort(PublishActivity.this, "选择的内容为空，请重试");
                e.printStackTrace();
            }
        }
    }

    private void WXshowShare(String URL, SHARE_MEDIA share_media) {//分享
        if (share_media != null && URL != null) {
            if (URL.contains("http")) {
                UMEmoji image = new UMEmoji(this, URL);
                image.compressStyle = UMEmoji.CompressStyle.SCALE;
                image.compressStyle = UMEmoji.CompressStyle.QUALITY;
                image.setThumb(new UMEmoji(this, URL));
                new ShareAction(this).withMedia(image)
                        .setPlatform(share_media)
                        .setCallback(umShareListener).share();
            } else {
                UMEmoji image = new UMEmoji(this, new File(URL));
                image.setThumb(new UMEmoji(this, new File(URL)));
                new ShareAction(this).withMedia(image)
                        .setPlatform(share_media)
                        .setCallback(umShareListener).share();
            }
        } else {
            ToastUtils.showLong(this, "选中图片错误，请重新选择");
        }

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            shareDialog(PublishActivity.this);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            hideAlertDialog(sharedialog);
            if (platform.equals(SHARE_MEDIA.WEIXIN)) {
                UmengStatisticsUtil.statisticsEvent(PublishActivity.this, "17");
            } else if (platform.equals(SHARE_MEDIA.QQ)) {
                UmengStatisticsUtil.statisticsEvent(PublishActivity.this, "15");
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            hideAlertDialog(sharedialog);
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            hideAlertDialog(sharedialog);
        }
    };

    public void shareDialog(Activity activity) {
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.img_animation);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
        sharedialog = new AlertDialog.Builder(activity, R.style.TransDialogStyle).create();
        if (!activity.isFinishing()) {
            sharedialog.show();
        }
        Window window = sharedialog.getWindow();
        window.setContentView(R.layout.share_dialog);
        ImageView imageView = (ImageView) window.findViewById(R.id.share_dialog_image);
        imageView.setAnimation(animation);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            page = 1;
            requestJoke(page);
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public void hideAlertDialog(AlertDialog mProgressDialog) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            Context context = ((ContextWrapper) mProgressDialog.getContext()).getBaseContext();
            if (context instanceof Activity) {
                if (!((Activity) context).isFinishing())
                    mProgressDialog.dismiss();
            } else {
                mProgressDialog.dismiss();
            }
            mProgressDialog = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideAlertDialog(sharedialog);
    }
}
