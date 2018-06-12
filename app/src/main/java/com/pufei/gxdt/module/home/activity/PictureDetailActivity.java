package com.pufei.gxdt.module.home.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.contents.Contents;
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
import com.pufei.gxdt.utils.EvenMsg;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.LogUtils;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.OkhttpUtils;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.utils.UrlString;
import com.pufei.gxdt.widgets.GlideApp;
import com.pufei.gxdt.widgets.popupwindow.CommonPopupWindow;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;
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
import ja.burhanrashid52.photoeditor.PhotoEditor;
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
    private static AlertDialog sharedialog;
    private CommonPopupWindow popupWindow;

    @Override
    public void initView() {
        AdvUtil.getInstance().getAdvHttp(this, your_original_layout, 3);
        Bundle bundle = this.getIntent().getExtras();
        index = bundle.getInt("picture_index");
        pictureList = (List<PictureResultBean.ResultBean>) bundle.getSerializable("picture_list");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rl_picture.setLayoutManager(linearLayoutManager);
        adapter = new OtherPictureAdapter(PictureDetailActivity.this, pictureList);
        rl_picture.setAdapter(adapter);
        adapter.setOnItemClickListener(new OtherPictureAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                GlideApp.with(PictureDetailActivity.this).load(pictureList.get(postion).getUrl()).placeholder(R.mipmap.loading).into(iv_picture);
                index = postion;
                URL = pictureList.get(postion).getUrl();
                refreshPictureData();
            }
        });

    }

    @Override
    public void getData() {
        joinPicture();
        URL = pictureList.get(index).getUrl();
        refreshPictureData();
    }

    private void setUserData(PictureDetailBean pictureDetailBeans) {
        if (pictureDetailBeans != null) {
            tv_eyes.setText(pictureDetailBeans.getResult().getView());
            tv_hot.setText(pictureDetailBeans.getResult().getHot());
            tv_change_img.setText(pictureDetailBeans.getResult().getCount());
        }
    }

    private void joinPicture() {
        GlideApp.with(this).load(pictureList.get(index).getUrl()).placeholder(R.mipmap.loading).into(iv_picture);
    }

    private void refreshPictureData() {
        if ("0".equals(pictureList.get(index).getIsSaveImg())) {
            activity_home1_shoucang.setBackgroundResource(R.mipmap.com_bt_ttab_star_normal);
        } else {
            activity_home1_shoucang.setBackgroundResource(R.mipmap.com_bt_ttab_star_select);
        }
        getImageDetail();
    }

    @OnClick({R.id.look_edit_image_iv, R.id.iv_report, R.id.ib_dowm_load, R.id.activity_home1_shoucang, R.id.tv_share_qq, R.id.tv_share_wx, R.id.activity_finish})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.look_edit_image_iv:
                Intent intent = new Intent(this, EditImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("picture_bean", (Serializable) pictureDetailBean);
                intent.putExtras(bundle);
                intent.putExtra(EditImageActivity.EDIT_TYPE, EditImageActivity.EDIT_TYPE_EDIT);
                startActivity(intent);
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GetImageInputStream(URL);
                    }
                }).start();
                break;
            case R.id.activity_home1_shoucang:
                if (App.userBean != null) {
                    if ("0".equals(pictureList.get(index).getIsSaveImg())) {//加收藏
                        if (NetWorkUtil.isNetworkConnected(PictureDetailActivity.this)) {
                            try {
                                JSONObject jsonObject = KeyUtil.getJson(this);
                                jsonObject.put("auth", SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH));
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
                                jsonObject.put("auth", SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH));
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
                    String result = response.body().string();
                    LogUtils.i("tb", result);
                    PictureDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(popupWindow.isShowing()){
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
                ToastUtils.showShort(this, "收藏成功");
                Intent mIntent = new Intent();
                this.setResult(1, mIntent);
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
                ToastUtils.showShort(this, "取消收藏成功");
                Intent mIntent = new Intent();
                this.setResult(1, mIntent);
            } else {
                pictureList.get(index).setIsSaveImg("1");
                ToastUtils.showShort(this, bean.getMsg());
            }
        }

    }

    public void GetImageInputStream(String imageurl) {//下载图片
        java.net.URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(4000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
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
        //文件夹不存在，则创建它
        if (!file.exists()) {
            file.mkdir();
        }
        String[] a = URL.split("/");
        final String fileName = a[a.length - 1];
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
            MediaStore.Images.Media.insertImage(PictureDetailActivity.this.getContentResolver(),//将图片插入系统图库
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//保存成功，通知系统更新相册
        Uri uri = Uri.fromFile(filena);
        intent.setData(uri);
        PictureDetailActivity.this.sendBroadcast(intent);
        PictureDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(PictureDetailActivity.this, "图片已保存到" + path + "/" + fileName);
            }
        });
    }

    private void openPermissin() {
        Acp.getInstance(this)
                .request(new AcpOptions.Builder().setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).build(),
                        new AcpListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            shareDialog(PictureDetailActivity.this);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtils.showShort(PictureDetailActivity.this, "分享成功");
            sharedialog.dismiss();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            sharedialog.dismiss();
            ToastUtils.showShort(PictureDetailActivity.this, "分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            sharedialog.dismiss();
            ToastUtils.showShort(PictureDetailActivity.this, "分享取消");
        }
    };

    public void shareDialog(Activity activity) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
