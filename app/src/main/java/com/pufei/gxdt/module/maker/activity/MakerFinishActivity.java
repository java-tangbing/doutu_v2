package com.pufei.gxdt.module.maker.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.MsgType;
import com.pufei.gxdt.db.DraftInfo;
import com.pufei.gxdt.db.DraftInfo_Table;
import com.pufei.gxdt.db.ImageDraft;
import com.pufei.gxdt.db.ImageDraft_Table;
import com.pufei.gxdt.db.TextDraft;
import com.pufei.gxdt.db.TextDraft_Table;
import com.pufei.gxdt.module.login.activity.LoginActivity;
import com.pufei.gxdt.module.maker.bean.MaterialBean;
import com.pufei.gxdt.module.maker.bean.RecommendTextBean;
import com.pufei.gxdt.module.maker.common.MakerEventMsg;
import com.pufei.gxdt.module.maker.presenter.EditImagePresenter;
import com.pufei.gxdt.module.maker.view.EditImageView;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.ImageUtils;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SystemInfoUtils;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.utils.UploadImageUtil;
import com.pufei.gxdt.widgets.GlideApp;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.http.Url;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class MakerFinishActivity extends BaseMvpActivity<EditImagePresenter> implements EditImageView {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_face_image)
    ImageView ivFaceImage;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    public static final String IMAGE_PATH = "image_path";
    public static final String IMAGE_ID = "IMAGE_ID";
    public static final String ORGINTABLE = "orgintable";
    public static final String TYPE = "TYPE";
    private String path;
    private String imageId;
    private String imageBase64;
    private String bgImageBase64;
    private DraftInfo info;
    private String id;
    private String uid;
    private String originTable;
    private int type;
    private ProgressDialog mProgressDialog;

    @Override
    public void initView() {
        llTitleLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);
    }

    @Override
    public void getData() {
        Intent intent = getIntent();
        path = intent.getStringExtra(IMAGE_PATH);
        imageId = intent.getStringExtra(IMAGE_ID);
        type = intent.getIntExtra(TYPE, 0);
        id = intent.getStringExtra("Id");
        uid = intent.getStringExtra("uid");
        originTable = intent.getStringExtra("originTable");
        Log.e("make finish  path",path+" ");
        info = new Select().from(DraftInfo.class).where(DraftInfo_Table.imageId.is(imageId)).and(DraftInfo_Table.isDraft.is(false)).querySingle();
        if (info != null) {
            if (path.contains("http:") || path.contains("https:")) {//合成图
                GlideApp.with(this).load(path).into(ivFaceImage);
                presenter.downloadImage(path, 0);
            } else {
                if (path.contains("gif") || path.contains("GIF")) {
                    GlideApp.with(this).load(new File(path)).into(ivFaceImage);
                    InputStream inputStream = null;
                    try {
                        inputStream = new FileInputStream(new File(path));
                        byte[] inputData = getBytes(inputStream);
                        imageBase64 = Base64.encodeToString(inputData, Base64.NO_WRAP);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Luban.with(this)
                            .load(new File(path))
                            .ignoreBy(100)
                            .setTargetDir(App.path1 + "/")
                            .filter(new CompressionPredicate() {
                                @Override
                                public boolean apply(String path) {
                                    return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                                }
                            })
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(File file) {
                                    SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>(info.width, info.height) {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                                            ivFaceImage.setImageBitmap(resource);
                                            imageBase64 = ImageUtils.bitmapToBase64(resource);
                                        }
                                    };
                                    GlideApp.with(MakerFinishActivity.this).asBitmap().load(file).into(simpleTarget);
                                }
                                @Override
                                public void onError(Throwable e) {
                                    Log.e("exception",e.getMessage());
                                }
                            }).launch();


                }
            }


            if (info.imagePath.contains("http:") || info.imagePath.contains("https:")) {//背景图
                    if(info.imagePath.contains(".gif") || info.imagePath.contains("GIF")) {
                        presenter.downloadImage(info.imagePath,1);
                    }else {
                        presenter.downloadGif(info.imagePath,App.path1 + "/" + System.currentTimeMillis() +".png");
                    }
                } else {
                    if (info.imagePath.contains("gif") || info.imagePath.contains("GIF")) {
                        try {
                            InputStream inputStream = new FileInputStream(new File(info.imagePath));
                            byte[] inputData = getBytes(inputStream);
                            bgImageBase64 = Base64.encodeToString(inputData, Base64.NO_WRAP);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Luban.with(this)
                                .load(new File(info.imagePath))
                                .ignoreBy(100)
                                .setTargetDir(App.path1 + "/")
                                .filter(new CompressionPredicate() {
                                    @Override
                                    public boolean apply(String path) {
                                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                                    }
                                })
                                .setCompressListener(new OnCompressListener() {
                                    @Override
                                    public void onStart() {
                                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>(info.width, info.height) {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                                                bgImageBase64 = ImageUtils.bitmapToBase64(resource);
                                            }
                                        };
                                        GlideApp.with(MakerFinishActivity.this).asBitmap().load(file).into(simpleTarget);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                    }
                                }).launch();

                    }

            }

        } else {
            ToastUtils.showShort(this, "空");
        }

    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MakerEventMsg msg) {
        if (msg.getType() == 12) {
            presenter.upLoadImage(RetrofitFactory.getRequestBody(msg.getUrl()));
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_maker_finish;
    }

    @OnClick({R.id.ll_title_left, R.id.btn_publish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.btn_publish:
                if (App.userBean != null) {
                    if (info != null) {
                        setRequestData();
                    }
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
//                    ToastUtils.showShort(this, "请先登录");
                }

                break;
        }
    }

    private void setRequestData() {
        if (imageBase64 != null && bgImageBase64 != null) {
            showLoading("上传中...");
            final List<ImageDraft> imageDrafts = new Select().from(ImageDraft.class).where(ImageDraft_Table.imageId.is(imageId)).and(ImageDraft_Table.isDraft.is(false)).queryList();
            final List<TextDraft> textDrafts = new Select().from(TextDraft.class).where(TextDraft_Table.imageId.is(imageId)).and(TextDraft_Table.isDraft.is(false)).queryList();
            UploadImageUtil.uploadImage(this, info, path, imageBase64, bgImageBase64, imageDrafts, textDrafts);
        }
    }

    @Override
    public void upLoadImageResult(ModifyResultBean response) {
        hideLoading();
        if(response.getCode() == 0 ) {
            if(!TextUtils.isEmpty(id)) {
                Map<String,String> map = new HashMap<>();
                map.put("deviceid", SystemInfoUtils.deviced(this));
                map.put("version", SystemInfoUtils.versionName(this));
                map.put("sign","sign");
                map.put("key","key");
                map.put("timestamp", (System.currentTimeMillis() / 1000) + "");
                map.put("os", "1");
                map.put("id",id);
                map.put("type","3");
                map.put("orgintable",originTable);
                map.put("option","edit");
                presenter.favoriteCounter(RetrofitFactory.getRequestBody(new Gson().toJson(map)));
            }
            ToastUtils.showShort(this, "发布成功");
        }else {
            ToastUtils.showShort(this, response.getMsg());
        }
    }

    @Override
    public void downloadImageResult(String base64, int type) {
        if (type == 0) {
            imageBase64 = base64;
        } else {
            bgImageBase64 = base64;
        }
    }

    @Override
    public void downloadGifResult(String path) {
        Luban.with(this)
                .load(new File(path))
                .ignoreBy(100)
                .setTargetDir(App.path1 +"/")
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        InputStream inputStream = null;
                        try {
                            inputStream = new FileInputStream(file);
                            byte[] inputData = getBytes(inputStream);
                            bgImageBase64 = Base64.encodeToString(inputData, Base64.NO_WRAP);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();
    }

    @Override
    public void recommentTextResult(RecommendTextBean response) {

    }

    @Override
    public void materialResult(MaterialBean response, int type) {

    }

    @Override
    public void requestErrResult(String msg) {
        hideLoading();
        ToastUtils.showShort(this,msg);
    }

    @Override
    public void requestSuccess(String msg) {

    }


    @Override
    public void setPresenter(EditImagePresenter presenter) {
        if (presenter == null) {
            this.presenter = new EditImagePresenter();
            this.presenter.attachView(this);
        }
    }

    void showLoading(@NonNull String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
