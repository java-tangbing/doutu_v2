package com.pufei.gxdt.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.db.DraftInfo;
import com.pufei.gxdt.db.ImageDraft;
import com.pufei.gxdt.db.TextDraft;
import com.pufei.gxdt.module.maker.common.MakerEventMsg;
import com.pufei.gxdt.widgets.GlideApp;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadImageUtil {

    public static void uploadImage(Context context, DraftInfo info, String makeUrl, String imageBase64, String bgImageBase64, final List<ImageDraft> imageDrafts, final List<TextDraft> textDrafts) {
//        final List<ImageDraft> imageDrafts = new Select().from(ImageDraft.class).where(ImageDraft_Table.imageId.is(imageId)).queryList();
//        final List<TextDraft> textDrafts = new Select().from(TextDraft.class).where(TextDraft_Table.imageId.is(imageId)).queryList();
        final Map<String, Object> map = new HashMap<>();
        final List<Map<String, String>> mapList = new ArrayList<>();
        map.put("deviceid", SystemInfoUtils.deviced(context));
        map.put("version", SystemInfoUtils.versionName(context));
        map.put("timestamp", (System.currentTimeMillis() / 1000) + "");
        map.put("os", "1");
        if(info.originImageId.length() >= 10) {
            map.put("orginid", "");
        }else {
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
            GlideApp.with(context).asBitmap().load(draft.stickerImagePath).into(simpleTarget);
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
                Log.e("text",draft.text);
            }
            if (mapList.size() > 0) {
                map.put("attachment", mapList);
            } else {
                map.put("attachment", "");
            }
            EventBus.getDefault().post(new MakerEventMsg(12, new Gson().toJson(map)));
        }
    }
}
