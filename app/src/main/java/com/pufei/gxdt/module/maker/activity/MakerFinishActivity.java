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
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.db.DraftInfo;
import com.pufei.gxdt.db.DraftInfo_Table;
import com.pufei.gxdt.db.ImageDraft;
import com.pufei.gxdt.db.ImageDraft_Table;
import com.pufei.gxdt.db.TextDraft;
import com.pufei.gxdt.db.TextDraft_Table;
import com.pufei.gxdt.module.maker.presenter.EditImagePresenter;
import com.pufei.gxdt.module.maker.view.EditImageView;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.ImageUtils;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.widgets.GlideApp;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String path;
    private String imageId;
    private String imageBase64;
    private DraftInfo info;
    private String orgintable;

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
        info = new Select().from(DraftInfo.class).where(DraftInfo_Table.imageId.is(imageId)).querySingle();
        if(info != null) {
            if(path.contains("http:") || path.contains("https:")) {
                GlideApp.with(this).load(path).into(ivFaceImage);
            }else {
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
                if(info != null) {
                    String request = setRequestData();
                    presenter.upLoadImage(RetrofitFactory.getRequestBody(request));
                }
                break;
        }
    }

    private String setRequestData() {
        List<ImageDraft> imageDrafts = new Select().from(ImageDraft.class).where(ImageDraft_Table.imageId.is(imageId)).queryList();
        List<TextDraft> textDrafts = new Select().from(TextDraft.class).where(TextDraft_Table.imageId.is(imageId)).queryList();

        Map<String, Object> map = new HashMap<>();
        final List<Map<String,String>> mapList = new ArrayList<>();
        map.put("deviceid", deviced());
        map.put("version", versionName());
        map.put("timestamp", (System.currentTimeMillis() / 1000) + "");
        map.put("os", "1");
        map.put("orginid", "");
        map.put("orgintable", "design_images");
        map.put("height", info.height);
        map.put("width", info.width);
        map.put("title", "");
        map.put("url", imageBase64);
        map.put("sign","sign");
        map.put("key","");
        map.put("auth", App.userBean.getAuth());
        for (int i = 0; i < imageDrafts.size(); i++) {
            final ImageDraft draft = imageDrafts.get(i);
            final Map<String, String> map1 = new HashMap<>();
            final int finalI = i;
            SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>(info.width, info.height) {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                    map1.put("url", ImageUtils.bitmapToBase64(resource));
                    map1.put("index", finalI +"");
                    map1.put("textName", "");
                    map1.put("centerX", draft.translationX+"");
                    map1.put("centerY", draft.translationY+"");
                    map1.put("height", draft.imageHeight+"");
                    map1.put("width", draft.imageWidth+"");
                    map1.put("textFontSize", "");
                    map1.put("textFontColor", "");
                    map1.put("zoom", draft.scaleX+"");
                    map1.put("rolling", draft.rotation+"");
                    mapList.add(map1);
                }
            };
            GlideApp.with(this).asBitmap().load(draft.stickerImagePath).into(simpleTarget);

        }
        int index = imageDrafts.size();
        for (int i = 0; i < textDrafts.size(); i++) {
            TextDraft  draft = textDrafts.get(i);
            Map<String, String> map1 = new HashMap<>();
            map1.put("url", "");
            map1.put("index", index+"");
            map1.put("textName", draft.text);
            map1.put("centerX", draft.translationX+"");
            map1.put("centerY", draft.translationY+"");
            map1.put("height", "");
            map1.put("width", "");
            map1.put("textFontSize", "");
            map1.put("textFontColor", draft.textColor+"");
            map1.put("zoom", draft.scaleX+"");
            map1.put("rolling", draft.rotation+"");
            mapList.add(map1);
            index++;
        }
        map.put("attachment",mapList);
        return new Gson().toJson(map);
    }

    private String versionName() {
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        PackageManager packageManager = getPackageManager();
        String packageName = getPackageName();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            if (packageInfo != null) {
                return packageInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String deviced() {
        String IMEINumber = "";
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                IMEINumber = telephonyMgr.getImei();
            } else {
                IMEINumber = telephonyMgr.getDeviceId();
            }
        }
        return IMEINumber;
    }

    @Override
    public void upLoadImageResult(String response) {

    }

    @Override
    public void downloadImageResult(String base64) {
        imageBase64 = base64;
    }

    @Override
    public void setPresenter(EditImagePresenter presenter) {
        if (presenter == null) {
            this.presenter = new EditImagePresenter();
            this.presenter.attachView(this);
        }
    }

    private void base64Gif() {
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(""));
            URL url = new URL("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
