package com.pufei.gxdt.module.sign.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.module.sign.adapter.PictureAdapter1;
import com.pufei.gxdt.module.sign.model.PictureBeanRe;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.DensityUtils;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.utils.UrlString;
import com.pufei.gxdt.widgets.MyFrontTextView;
import com.pufei.gxdt.widgets.SpaceItemDecoration;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangwenzhang on 2017/8/2.
 */

public class BasePictureActivity extends AppCompatActivity {
    /* @InjectView(R.id.activity_basepicture_cancel)
     protected LinearLayout activityBasepictureCancel;
     @InjectView(R.id.activity_basepicture_tv)
     protected MyFrontTextView activityBasepictureTv;
     @InjectView(R.id.activity_basepicture_rc)
     protected RecyclerView activityBasepictureRc;*/
    protected PictureAdapter1 adapter1;
    protected List<String> list;
    protected String URL;
    /*  @InjectView(R.id.activity_basepicture_bianji)
      protected MyFrontTextView activityBasepictureBianji;
      @InjectView(R.id.activity_basepicture_delete)
      ImageView activityBasepictureDelete;*/
    public static List<PictureBeanRe.ResultBean> imagelist = new ArrayList<>();
    protected List<PictureBeanRe.ResultBean> enshriList;
    @BindView(R.id.activity_basepicture_cancel)
    protected LinearLayout activityBasepictureCancel;
    @BindView(R.id.activity_basepicture_delete)
    protected ImageView activityBasepictureDelete;
    @BindView(R.id.activity_basepicture_tv)
    protected MyFrontTextView activityBasepictureTv;
    @BindView(R.id.activity_basepicture_bianji)
    protected MyFrontTextView activityBasepictureBianji;
    @BindView(R.id.activity_basepicture_rc)
    protected XRecyclerView activityBasepictureRc;
    @BindView(R.id.data_empty_title)
    MyFrontTextView dataEmptyTitle;
    @BindView(R.id.data_empty)
    protected LinearLayout dataEmpty;
    private TextView tvtitle, time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentview());
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initView();
        initData();
        initListener();
    }

    public void addHeader() {
        LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerView = lif.inflate(R.layout.title_time, null);
        tvtitle = (TextView) headerView.findViewById(R.id.title_time_title);
        time = (TextView) headerView.findViewById(R.id.title_time_time);
        time.setVisibility(View.GONE);
        tvtitle.setText(getName());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerView.setLayoutParams(layoutParams);
        activityBasepictureRc.addHeaderView(headerView);
        tvtitle.setText(getName());
    }

    public void initView() {
        //addHeader();
        enshriList = new ArrayList<>();
        adapter1 = new PictureAdapter1(this, enshriList);
        dataEmptyTitle.setText(getDataEmpty());
        activityBasepictureRc.setPullRefreshEnabled(false);
        activityBasepictureRc.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        activityBasepictureRc.addItemDecoration(new SpaceItemDecoration(DensityUtils.dp2px(this, 5)));
        activityBasepictureRc.setAdapter(adapter1);
    }

    public int getContentview() {
        return R.layout.activity_basepicture;
    }

    public String getName() {
        return "我的收藏";
    }

    public void initData() {

    }

    public String getDataEmpty() {
        return "您还没有收藏表情哦";
    }

    public void initListener() {

    }

    protected ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {
        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            if (share_media != null && URL != null) {
                if (share_media == SHARE_MEDIA.WEIXIN) {
                    if (URL.contains("http")) {
                        UMImage image = new UMImage(BasePictureActivity.this, URL);
                        image.compressStyle = UMImage.CompressStyle.SCALE;
                        image.compressStyle = UMImage.CompressStyle.QUALITY;
                        new ShareAction(BasePictureActivity.this).withMedia(image)
                                .setPlatform(share_media)
                                .setCallback(umShareListener).share();
                    } else {
                        UMImage image = new UMImage(BasePictureActivity.this, BitmapFactory.decodeFile(URL));
                        image.setThumb(new UMImage(BasePictureActivity.this, BitmapFactory.decodeFile(URL)));
                        new ShareAction(BasePictureActivity.this).withMedia(image)
                                .setPlatform(share_media)
                                .setCallback(umShareListener).share();
                    }
                } else {
                    if (URL.contains("http")) {
                        UMImage image = new UMImage(BasePictureActivity.this, URL);
                        image.compressStyle = UMImage.CompressStyle.SCALE;
                        image.compressStyle = UMImage.CompressStyle.QUALITY;
                        new ShareAction(BasePictureActivity.this).withMedia(image)
                                .setPlatform(share_media)
                                .setCallback(umShareListener).share();
                    } else {
                        try {
                            UMImage image = new UMImage(BasePictureActivity.this, new File(URL));
                            image.compressStyle = UMImage.CompressStyle.SCALE;
                            image.compressStyle = UMImage.CompressStyle.QUALITY;
                            image.setThumb(new UMEmoji(BasePictureActivity.this, new File(URL)));
                            new ShareAction(BasePictureActivity.this).withMedia(image)
                                    .setPlatform(share_media)
                                    .setCallback(umShareListener).share();
                        } catch (Exception e) {
                            ToastUtils.showLong(BasePictureActivity.this, "选中图片错误，请重新选择");
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
    };
    private AlertDialog sharedialog;

    public void ShareDialog(Activity activity) {
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.img_animation);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
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

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            ShareDialog(BasePictureActivity.this);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            sharedialog.dismiss();
//            if (App.token != null) {
//                MissionUtils.getInstance().getDayMission(App.token, "3", "image");
//            }
            Toast.makeText(BasePictureActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            Log.d("plat", "platform" + platform);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            sharedialog.dismiss();
            Toast.makeText(BasePictureActivity.this, platform + " 分享失败啦" + t.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            sharedialog.dismiss();
            Toast.makeText(BasePictureActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedialog != null) {
            sharedialog.dismiss();
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /* @OnClick(R.id.activity_basepicture_cancel)
            public void onViewClicked() {
                finish();
            }*/
    public void showShare() {//分享
        if (URL != null) {
            try {
                new ShareAction(this)
                        .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setShareboardclickCallback(shareBoardlistener).open();
            } catch (NullPointerException e) {
                ToastUtils.showShort(this, "选择的内容为空，请重试");
                e.printStackTrace();
            }
        }
    }

    public List<String> getImagePathFromSD(String filepath1) {
        // 图片列表
        List<String> imagePathList = new ArrayList<String>();
        // 得到sd卡内image文件夹的路径   File.separator(/)
        try {

            // 得到该路径文件夹下所有的文件
            File fileAll = new File(filepath1);
            File[] files = fileAll.listFiles();
            // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (checkIsImageFile(file.getPath())) {
                    imagePathList.add(file.getPath());
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        // 返回得到的图片列表
        return imagePathList;
    }

    private boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg") || FileEnd.equals("bmp")) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }

    private Boolean is = false;

    @OnClick({R.id.activity_basepicture_cancel, R.id.activity_basepicture_delete, R.id.activity_basepicture_bianji})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_basepicture_cancel:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.activity_basepicture_delete:
                update();
                break;
            case R.id.activity_basepicture_bianji:
                if (is) {
                    activityBasepictureCancel.setVisibility(View.VISIBLE);
                    activityBasepictureBianji.setText("编辑");
                    activityBasepictureDelete.setVisibility(View.GONE);
                    is = false;
                    adapter1.setA(0);
                    adapter1.notifyDataSetChanged();
                } else {
                    activityBasepictureCancel.setVisibility(View.GONE);
                    activityBasepictureDelete.setVisibility(View.VISIBLE);
                    activityBasepictureBianji.setText("取消");
                    is = true;
                    adapter1.setA(1);
                    adapter1.notifyDataSetChanged();
                }
                break;
        }
    }

    public String getType() {
        return "1";
    }

    public void update() {
        for (int i = 0; i < imagelist.size(); i++) {
            PictureBeanRe.ResultBean resultBean = imagelist.get(i);
            if (resultBean.getUrl().contains("http")) {
//                deleteUrl(resultBean.getId(), getType());
            } else {
                android.util.Log.e("删除的结果----", "-------------------------- -1");
                new File(resultBean.getUrl()).delete();
            }
        }
        enshriList.clear();
        initData();
        adapter1.notifyDataSetChanged();
        /*if (list.size()==0){
            nodate.setVisibility(View.VISIBLE);
        }*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String data) {
        if (data.equals(UrlString.Make_UP)) {
            update();
        }

    }

//    public void deleteUrl(String id, final String type) {
//        android.util.Log.e("删除的结果----", "-------------------------- 0");
//        JSONObject jsonObject = KeyUtil.getJson(this);
//        try {
//            jsonObject.put("auth", App.token);
//            jsonObject.put("id", id);
//            jsonObject.put("type", type);
//            android.util.Log.e("删除的结果----", "-------------------------- 1");
//            OkhttpUtils.post(UrlString.delteimage, jsonObject.toString(), new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    android.util.Log.e("删除的结果----", response.body().string());
//                    BasePictureActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (type.contains("1")) {
//                                EventBus.getDefault().post(new EventBean(Constants.USER_PICYURE, null));
//                            } else {
//                                EventBus.getDefault().post(new EventBean(Constants.USER_MAKE, null));
//                            }
//                            if (enshriList.size() == 0) {
//                                dataEmpty.setVisibility(View.VISIBLE);
//                            } else {
//                                dataEmpty.setVisibility(View.GONE);
//                            }
//                        }
//                    });
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
