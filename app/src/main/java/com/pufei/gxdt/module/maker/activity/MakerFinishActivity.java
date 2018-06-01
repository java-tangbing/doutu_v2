package com.pufei.gxdt.module.maker.activity;

import android.annotation.SuppressLint;
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
import com.pufei.gxdt.module.maker.common.MakerEventMsg;
import com.pufei.gxdt.module.maker.presenter.EditImagePresenter;
import com.pufei.gxdt.module.maker.view.EditImageView;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.ImageUtils;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SystemInfoUtils;
import com.pufei.gxdt.utils.ToastUtils;
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
    private String orgintable;
    private int type;

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
        info = new Select().from(DraftInfo.class).where(DraftInfo_Table.imageId.is(imageId)).and(DraftInfo_Table.isDraft.is(false)).querySingle();
        if (info != null) {
            if (path.contains("http:") || path.contains("https:")) {//合成图
                GlideApp.with(this).load(path).into(ivFaceImage);
                presenter.downloadImage(path, 0);
            } else {
                if (path.contains("gif") || path.contains("GIF")) {
                    GlideApp.with(this).load(path).into(ivFaceImage);
                    InputStream inputStream = null;
                    try {
                        inputStream = new FileInputStream(new File(info.imagePath));
                        byte[] inputData = getBytes(inputStream);
                        imageBase64 = Base64.encodeToString(inputData, Base64.NO_WRAP);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>(info.width, info.height) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                            ivFaceImage.setImageBitmap(resource);
                            imageBase64 = ImageUtils.bitmapToBase64(resource);
                        }
                    };
                    GlideApp.with(this).asBitmap().load(new File(path)).into(simpleTarget);
                }
            }


            if (info.imagePath.contains("http:") || info.imagePath.contains("https:")) {//背景图
                presenter.downloadImage(info.imagePath, 1);
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
                    SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>(info.width, info.height) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                            bgImageBase64 = ImageUtils.bitmapToBase64(resource);
                        }
                    };
                    GlideApp.with(this).asBitmap().load(new File(info.imagePath)).into(simpleTarget);
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
                        //       presenter.upLoadImage(RetrofitFactory.getRequestBody(request));
                    }
                } else {
                    ToastUtils.showShort(this, "请先登录");
                }

                break;
        }
    }

    private void setRequestData() {
        final List<ImageDraft> imageDrafts = new Select().from(ImageDraft.class).where(ImageDraft_Table.imageId.is(imageId)).queryList();
        final List<TextDraft> textDrafts = new Select().from(TextDraft.class).where(TextDraft_Table.imageId.is(imageId)).queryList();
        final Map<String, Object> map = new HashMap<>();
        final List<Map<String, String>> mapList = new ArrayList<>();
        map.put("deviceid", SystemInfoUtils.deviced(this));
        map.put("version", SystemInfoUtils.versionName(this));
        map.put("timestamp", (System.currentTimeMillis() / 1000) + "");
        map.put("os", "1");
        if (type == 0 || type == 2) {
            map.put("orginid", "");
        } else {
            map.put("orginid", imageId);
        }
        map.put("orgintable", "design_images");
        map.put("height", info.height);
        map.put("width", info.width);
        map.put("title", "");
        map.put("make_url", imageBase64);
        map.put("url", bgImageBase64);
        map.put("sign", "sign");
        map.put("key", "");
        map.put("uid", "");
        map.put("id", "");
        map.put("auth", App.userBean.getAuth());

        if (path.contains(".gif") || path.contains(".GIF")) {
            map.put("image_type", "gif");
        } else {
            map.put("image_type", "png");
        }
        for (int i = 0; i < imageDrafts.size(); i++) {
            final ImageDraft draft = imageDrafts.get(i);
            final Map<String, String> map1 = new HashMap<>();
            final int finalI = i;
            SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>(draft.imageWidth, draft.imageHeight) {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                    map1.put("url", ImageUtils.bitmapToBase64(resource));
                    map1.put("index", "1");
                    map1.put("textName", "");
                    map1.put("centerX", draft.translationX + "");
                    map1.put("centerY", draft.translationY + "");
                    map1.put("height", draft.imageHeight + "");
                    map1.put("width", draft.imageWidth + "");
                    map1.put("textFontSize", "0");
                    map1.put("textFontColor", "");
                    map1.put("zoom", draft.scaleX + "");
                    map1.put("rolling", draft.rotation + "");
                    mapList.add(map1);

                    if (finalI == (imageDrafts.size() - 1)) {
                        int index = finalI;
                        for (int i = 0; i < textDrafts.size(); i++) {
                            TextDraft draft = textDrafts.get(i);
                            Map<String, String> map1 = new HashMap<>();
                            map1.put("url", "");
                            map1.put("index", index + "");
                            map1.put("textName", draft.text);
                            map1.put("centerX", draft.translationX + "");
                            map1.put("centerY", draft.translationY + "");
                            map1.put("height", "");
                            map1.put("width", "");
                            map1.put("textFontSize", "12");
                            map1.put("textFontColor", draft.textColor + "");
                            map1.put("zoom", draft.scaleX + "");
                            map1.put("rolling", draft.rotation + "");
                            mapList.add(map1);
                            index++;
                        }
                        if (mapList.size() > 0) {
                            map.put("attachment", mapList);
                        } else {
                            map.put("attachment", "");
                        }
                        EventBus.getDefault().post(new MakerEventMsg(12, new Gson().toJson(map)));
                    }

                }
            };
            GlideApp.with(this).asBitmap().load(draft.stickerImagePath).into(simpleTarget);
        }

        if (imageDrafts.size() == 0) {
            for (int i = 0; i < textDrafts.size(); i++) {
                TextDraft draft = textDrafts.get(i);
                Map<String, String> map1 = new HashMap<>();
                map1.put("url", "");
                map1.put("index", i + "");
                map1.put("textName", draft.text);
                map1.put("centerX", draft.translationX + "");
                map1.put("centerY", draft.translationY + "");
                map1.put("height", "");
                map1.put("width", "");
                map1.put("textFontSize", "12");
                map1.put("textFontColor", draft.textColor + "");
                map1.put("zoom", draft.scaleX + "");
                map1.put("rolling", draft.rotation + "");
                mapList.add(map1);
            }
            if (mapList.size() > 0) {
                map.put("attachment", mapList);
            } else {
                map.put("attachment", "");
            }
            EventBus.getDefault().post(new MakerEventMsg(12, new Gson().toJson(map)));
        }
    }

//    private String versionName() {
//        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//
//        PackageManager packageManager = getPackageManager();
//        String packageName = getPackageName();
//        try {
//            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
//            if (packageInfo != null) {
//                return packageInfo.versionName;
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    private String deviced() {
//        String IMEINumber = "";
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//            TelephonyManager telephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                IMEINumber = telephonyMgr.getImei();
//            } else {
//                IMEINumber = telephonyMgr.getDeviceId();
//            }
//        }
//        return IMEINumber;
//    }

    @Override
    public void upLoadImageResult(ModifyResultBean response) {
        ToastUtils.showShort(this, response.getMsg());
    }

    @Override
    public void downloadImageResult(String base64, int type) {
        Log.e("fdsf", base64 + " ");
        if (type == 0) {
            imageBase64 = base64;
        } else {
            bgImageBase64 = base64;
        }
    }

    @Override
    public void setPresenter(EditImagePresenter presenter) {
        if (presenter == null) {
            this.presenter = new EditImagePresenter();
            this.presenter.attachView(this);
        }
    }
}
