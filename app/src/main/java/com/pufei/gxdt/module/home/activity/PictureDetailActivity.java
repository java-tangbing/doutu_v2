package com.pufei.gxdt.module.home.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.discover.activity.DiscoverDetailedActivity;
import com.pufei.gxdt.module.home.adapter.OtherPictureAdapter;
import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.PictureDetailBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.presenter.ImageTypePresenter;
import com.pufei.gxdt.module.home.view.ImageTypeView;
import com.pufei.gxdt.module.login.activity.LoginActivity;
import com.pufei.gxdt.module.maker.activity.EditImageActivity;
import com.pufei.gxdt.utils.AdvUtil;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.OkhttpUtils;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.utils.UmengStatisticsUtil;
import com.pufei.gxdt.utils.UrlString;
import com.pufei.gxdt.widgets.GlideApp;
import com.pufei.gxdt.widgets.SpaceItemDecoration;
import com.pufei.gxdt.widgets.popupwindow.CommonPopupWindow;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 表情操作
 * Created by tb on 2018/5/23.
 */

public class PictureDetailActivity extends BaseMvpActivity<ImageTypePresenter> implements ImageTypeView {
    @BindView(R.id.iv_now_picture)
    ImageView iv_picture;
    @BindView(R.id.rl_picture)
    RecyclerView rl_picture;
    @BindView(R.id.tv_eyes)
    TextView tv_eyes;
    @BindView(R.id.tv_hot)
    TextView tv_hot;
    @BindView(R.id.tv_change_img)
    TextView tv_change_img;
    @BindView(R.id.activity_home1_shoucang)
    ImageButton activity_home1_shoucang;
    @BindView(R.id.your_original_layout)
    RelativeLayout your_original_layout;
    private int index;
    private String URL;
    private String path = Environment.getExternalStorageDirectory().getPath() + "/斗图大师";
    private List<PictureResultBean.ResultBean> pictureList = new ArrayList<>();
    private PictureDetailBean.ResultBean pictureDetailBean;
    private OtherPictureAdapter adapter;
    private AlertDialog sharedialog;
    private CommonPopupWindow popupWindow;

    @Override
    public void initView() {
        AdvUtil.getInstance(this).getAdvHttp(this, your_original_layout, 3);
        Bundle bundle = this.getIntent().getExtras();
        index = bundle.getInt("picture_index");
        pictureList = (List<PictureResultBean.ResultBean>) bundle.getSerializable("picture_list");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rl_picture.addItemDecoration(new SpaceItemDecoration(10,0));
        rl_picture.setLayoutManager(linearLayoutManager);
        adapter = new OtherPictureAdapter(PictureDetailActivity.this, pictureList);
        rl_picture.setAdapter(adapter);
        adapter.setOnItemClickListener(new OtherPictureAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                countView(pictureList.get(index).getId(), 3, pictureList.get(index).getOrgintable(), "click");
                URL = pictureList.get(postion).getUrl();
                GlideApp.with(PictureDetailActivity.this).load(URL).placeholder(R.mipmap.loading).into(iv_picture);
                index = postion;
                refreshPictureData();
            }
        });

    }

    @Override
    public void getData() {
        joinPicture();
        URL = pictureList.get(index).getUrl();
        refreshPictureData();
        countView(pictureList.get(index).getId(), 3, pictureList.get(index).getOrgintable(), "click");

    }

    private void countView(String id, int type, String orgintable, String option) {
        if (NetWorkUtil.isNetworkConnected(this)) {
            try {
                JSONObject countViewObj = KeyUtil.getJson(this);
                countViewObj.put("id", id);
                countViewObj.put("type", type + "");
                countViewObj.put("orgintable", orgintable + "");
                countViewObj.put("option", option + "");
                countViewObj.put("url", URL);
                presenter.getCountView(RetrofitFactory.getRequestBody(countViewObj.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void setUserData(PictureDetailBean pictureDetailBeans) {
        if (pictureDetailBeans != null) {
//            tv_eyes.setText(pictureDetailBeans.getResult().getView());
//            tv_hot.setText(pictureDetailBeans.getResult().getHot());
            tv_change_img.setText(pictureDetailBeans.getResult().getCount());
        }
    }

    private void joinPicture() {
        if(pictureList != null){
            if(pictureList.get(index)!=null){
                GlideApp.with(this).load(pictureList.get(index).getUrl()).placeholder(R.mipmap.loading).into(iv_picture);
            }
        }
    }

    private void refreshPictureData() {
        if ("0".equals(pictureList.get(index).getIsSaveImg())) {
            activity_home1_shoucang.setBackgroundResource(R.mipmap.com_bt_ttab_star_normal);
        } else {
            activity_home1_shoucang.setBackgroundResource(R.mipmap.com_bt_ttab_star_select);
        }
        getImageDetail();
    }

    @OnClick({R.id.tv_change_img, R.id.look_edit_image_iv, R.id.iv_report, R.id.ib_dowm_load, R.id.activity_home1_shoucang, R.id.tv_share_qq, R.id.tv_share_wx, R.id.activity_finish})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_change_img:
                if (Integer.parseInt(pictureDetailBean.getCount()) != 0) {
                    Intent intent0 = new Intent(this, DiscoverDetailedActivity.class);
                    Bundle bundle0 = new Bundle();
                    bundle0.putString("id", pictureList.get(index).getId());
                    bundle0.putString("orginid", pictureList.get(index).getOrginid());
                    bundle0.putString("orgintable", pictureList.get(index).getOrgintable());
                    bundle0.putString("uid", pictureDetailBean.getUid());
                    intent0.putExtras(bundle0);
                    startActivity(intent0);
                }
                break;
            case R.id.look_edit_image_iv:
                Intent intent = new Intent(this, EditImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("picture_bean", (Serializable) pictureDetailBean);
                intent.putExtras(bundle);
                intent.putExtra(EditImageActivity.EDIT_TYPE, EditImageActivity.EDIT_TYPE_EDIT);
                startActivity(intent);
                UmengStatisticsUtil.statisticsEvent(this, "18");
                break;
            case R.id.iv_report:
                popupWindow = new CommonPopupWindow.Builder(this)
                        .setView(R.layout.menu_pictruedetail)
                        .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setBackGroundLevel(0.5f)
                        .setAnimationStyle(R.style.anim_menu_pop)
                        .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                            @Override
                            public void getChildView(final View view, int layoutResId) {
                                view.findViewById(R.id.menu_pictruedetail_jubao).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        reportImage();
                                        UmengStatisticsUtil.statisticsEvent(PictureDetailActivity.this, "10");
                                    }
                                });
                                view.findViewById(R.id.menu_pictruedetail_cance).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        popupWindow.dismiss();
                                    }
                                });

                            }
                        })
                        .setOutsideTouchable(true)
                        .create();
                popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_share_qq:
                if (ActivityCompat.checkSelfPermission(PictureDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    openPermissin();
                } else {
                    QQshowShare(URL, SHARE_MEDIA.QQ);
                }
                break;
            case R.id.tv_share_wx:
                if (ActivityCompat.checkSelfPermission(PictureDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    openPermissin();
                } else {
                    WXshowShare(URL, SHARE_MEDIA.WEIXIN);
                }
                break;
            case R.id.ib_dowm_load:
                if (URL != null) {
                    UmengStatisticsUtil.statisticsEvent(PictureDetailActivity.this, "11");
                    if (ActivityCompat.checkSelfPermission(PictureDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        openPermissin();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                GetImageInputStream(URL);
                            }
                        }).start();
                    }
                }
                break;
            case R.id.activity_home1_shoucang:
                if (App.userBean != null) {
                    if ("0".equals(pictureList.get(index).getIsSaveImg())) {//加收�
                        if (NetWorkUtil.isNetworkConnected(PictureDetailActivity.this)) {
                            try {
                                JSONObject jsonObject = KeyUtil.getJson(this);
                                jsonObject.put("type", 1 + "");
                                jsonObject.put("url", URL);
                                presenter.addFavorite(RetrofitFactory.getRequestBody(jsonObject.toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtils.showShort(PictureDetailActivity.this, "无网络连接");
                        }

                    } else {//取消收藏
                        if (NetWorkUtil.isNetworkConnected(PictureDetailActivity.this)) {
                            try {
                                JSONObject jsonObject = KeyUtil.getJson(this);
                                jsonObject.put("type", 1 + "");
                                jsonObject.put("id", URL);
                                presenter.cancleFavorite(RetrofitFactory.getRequestBody(jsonObject.toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtils.showShort(PictureDetailActivity.this, "无网络连接");
                        }
                    }
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case R.id.activity_finish:
                AppManager.getAppManager().finishActivity();
                break;
        }
    }

    private void reportImage() {
        JSONObject jsonObject = KeyUtil.getJson(this);
        try {
            jsonObject.put("type", "1");
            jsonObject.put("link", URL);
            OkhttpUtils.post(UrlString.REPORT, jsonObject.toString(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    PictureDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (popupWindow.isShowing()) {
                                popupWindow.dismiss();
                            }
                            ToastUtils.showShort(PictureDetailActivity.this, "举报成功");
                        }
                    });
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void getImageDetail() {
        if (NetWorkUtil.isNetworkConnected(PictureDetailActivity.this)) {
            try {
                JSONObject jsonObject = KeyUtil.getJson(this);
                jsonObject.put("id", pictureList.get(index).getId());
                jsonObject.put("orginid", pictureList.get(index).getOrginid());
                jsonObject.put("orgintable", pictureList.get(index).getOrgintable());
                presenter.getImageDetailList(RetrofitFactory.getRequestBody(jsonObject.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            ToastUtils.showShort(PictureDetailActivity.this, "无网络连接");
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_look;
    }

    @Override
    public void setPresenter(ImageTypePresenter presenter) {
        if (presenter == null) {
            this.presenter = new ImageTypePresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void resultHotImage(PictureResultBean bean) {

    }

    @Override
    public void resultImageDetail(PictureDetailBean bean) {
        if (bean != null) {
            pictureDetailBean = bean.getResult();
            setUserData(bean);
        }

    }

    @Override
    public void resultAddFavorite(FavoriteBean bean) {
        if (bean != null) {
            if ("0".equals(bean.getCode())) {
                pictureList.get(index).setIsSaveImg("1");
                activity_home1_shoucang.setBackgroundResource(R.mipmap.com_bt_ttab_star_select);
                Intent mIntent = new Intent();
                this.setResult(1, mIntent);
                UmengStatisticsUtil.statisticsEvent(PictureDetailActivity.this, "12");
            } else {
                pictureList.get(index).setIsSaveImg("0");
                ToastUtils.showShort(this, bean.getMsg());
            }

        }
    }

    @Override
    public void resultCancleFavorite(FavoriteBean bean) {
        if (bean != null) {
            if ("0".equals(bean.getCode())) {
                pictureList.get(index).setIsSaveImg("0");
                activity_home1_shoucang.setBackgroundResource(R.mipmap.com_bt_ttab_star_normal);
                Intent mIntent = new Intent();
                this.setResult(1, mIntent);
                UmengStatisticsUtil.statisticsEvent(PictureDetailActivity.this, "13");
            } else {
                pictureList.get(index).setIsSaveImg("1");
                ToastUtils.showShort(this, bean.getMsg());
            }
        }

    }

    @Override
    public void resultCountView(FavoriteBean bean) {

    }

    @Override
    public void requestErrResult(String msg) {

    }

    public void GetImageInputStream(String imageurl) {//下载图片
        java.net.URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(4000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓�
            InputStream inputStream = connection.getInputStream();
            SavaImage(inputStream, path);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SavaImage(InputStream inputStream, final String path) {//保存图片
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创建�
        if (!file.exists()) {
            file.mkdir();
        }
        String fileName = null;
        if (URL.contains("gif") || URL.contains("GIF")) {
            fileName = System.currentTimeMillis() + ".gif";
        } else {
            fileName = System.currentTimeMillis() + ".jpg";
        }
        //final String fileName = System.currentTimeMillis() + ".gif";
        File filena = new File(file, fileName);
        try {
            int i = 0;
            fileOutputStream = new FileOutputStream(filena);
            byte[] bytes = new byte[2048];
            while ((i = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, i);
            }
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            MediaStore.Images.Media.insertImage(PictureDetailActivity.this.getContentResolver(),//将图片插入系统图�
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//保存成功，通知系统更新相册
//        Uri uri = Uri.fromFile(filena);
//        intent.setData(uri);
//        PictureDetailActivity.this.sendBroadcast(intent);
        final String finalFileName = fileName;
        PictureDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(PictureDetailActivity.this, "图片已保存到" + path + "/" + finalFileName);
            }
        });
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
                                ToastUtils.showShort(PictureDetailActivity.this, "请求权限失败,请手动开启！");
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
                ToastUtils.showShort(PictureDetailActivity.this, "选择的内容为空，请重试");
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
            shareDialog(PictureDetailActivity.this);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            countView(pictureList.get(index).getId(), 3, pictureList.get(index).getOrgintable(), "share");
            hideAlertDialog(sharedialog);
            if (platform.equals(SHARE_MEDIA.WEIXIN)) {
                UmengStatisticsUtil.statisticsEvent(PictureDetailActivity.this, "17");
            } else if (platform.equals(SHARE_MEDIA.QQ)) {
                UmengStatisticsUtil.statisticsEvent(PictureDetailActivity.this, "15");
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
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运�
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();//防止内存泄漏
    }
}
