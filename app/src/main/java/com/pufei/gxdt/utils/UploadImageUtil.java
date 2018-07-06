package com.pufei.gxdt.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.db.DraftInfo;
import com.pufei.gxdt.db.ImageDraft;
import com.pufei.gxdt.db.TextDraft;
import com.pufei.gxdt.module.maker.activity.MakerFinishActivity;
import com.pufei.gxdt.module.maker.common.MakerEventMsg;
import com.pufei.gxdt.widgets.GlideApp;
import com.umeng.commonsdk.debug.E;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadImageUtil {

    public static void uploadImage(Context context, DraftInfo info, String makeUrl, String imageBase64, String bgImageBase64, final List<ImageDraft> imageDrafts, final List<TextDraft> textDrafts) {
        final Map<String, Object> map = new HashMap<>();
        final List<Map<String, String>> mapList = new ArrayList<>();
        map.put("deviceid", SystemInfoUtils.deviced(context));
        map.put("version", SystemInfoUtils.versionName(context));
        map.put("timestamp", (System.currentTimeMillis() / 1000) + "");
        map.put("os", "1");
        if (info.originImageId.length() >= 10) {
            map.put("orginid", "");
        } else {
            map.put("orginid", info.originImageId);
        }
        map.put("uid", info.uid);
        map.put("id", info.originId);
        map.put("orgintable", info.originTable);
        map.put("height", info.height);
        map.put("width", info.width);
        map.put("title", "");
        map.put("make_url", imageBase64);
        map.put("url", bgImageBase64);
        map.put("sign", "sign");
        map.put("key", "");
        map.put("auth", App.userBean.getAuth());
        if (makeUrl.contains(".gif") || makeUrl.contains(".GIF")) {
            map.put("image_type", "gif");
        } else {
            map.put("image_type", "jpg");
        }
        Log.e("imageDrafts",imageDrafts.size()+"");
        for (int i = 0; i < imageDrafts.size(); i++) {
            final ImageDraft draft = imageDrafts.get(i);
            final Map<String, String> map1 = new HashMap<>();
            if(draft.type ==0) {
                if(draft.stickerImagePath.contains("http") || draft.stickerImagePath.contains("https")) {
                    map1.put("url", draft.stickerImagePath);
                }else {
                    String base64 = ImageUtils.fileToBase64(new File(draft.stickerImagePath));
                    map1.put("url", base64);
                }
            }else {
                map1.put("url",draft.stickerImagePath);
            }
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
        }
        for (int i = 0; i < textDrafts.size(); i++) {
            TextDraft draft = textDrafts.get(i);
            Map<String, String> map1 = new HashMap<>();
            map1.put("url", "");
            map1.put("index", i + "");
            map1.put("textName", draft.text);
            map1.put("centerX", draft.translationX + "");
            map1.put("centerY", draft.translationY + "");
            map1.put("height", draft.height + "");
            map1.put("width", draft.width + "");
            map1.put("textFontSize", draft.textSize + "");
            map1.put("textFont", draft.textFont);
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

    @SuppressLint("StaticFieldLeak")
    public static void getImageBase64(final Context context, final String bgPath, final String makeUrl, final OnGetBase64Listener listener) {
        final List<String> base64List = new ArrayList<>();
        new AsyncTask<String, String, Exception>() {
            @Override
            protected Exception doInBackground(String... strings) {
                RequestManager rc = GlideApp.with(context);
                FutureTarget<File> bgFuture = null;
                FutureTarget<File> mkFuture = null;
                if(bgPath.contains("http:") || bgPath.contains("https:")) {
                    bgFuture = rc.downloadOnly().load(bgPath).submit();
                }else {
                    bgFuture = rc.downloadOnly().load(new File(bgPath)).submit();
                }
                if(!TextUtils.isEmpty(makeUrl)) {
                    mkFuture = rc.downloadOnly().load(new File(makeUrl)).submit();
                }
                try{

                    File bgFiles = bgFuture.get();
                    String bg64 = ImageUtils.fileToBase64(bgFiles);
                    base64List.add(bg64);
                    if(!TextUtils.isEmpty(makeUrl) && mkFuture != null) {
                        File mkFile = mkFuture.get();
                        String mk64 = ImageUtils.fileToBase64(mkFile);
                        base64List.add(mk64);
                    }
                }catch (Exception e) {
                    Log.e("MakerFinishActivity",e.getMessage());
                    Log.e("MakerFinishActivity","Glide Download Pic Failed!");
                    return new Exception();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception e) {
                if(e == null) {
                    listener.getBase64Success(base64List);
                }else {
                    listener.getBase64Failed(e);
                }
            }
        }.execute();
    }

    public interface OnGetBase64Listener {
        void getBase64Success(List<String> base64List);
        void getBase64Failed(Exception e);
    }
}
