package com.pufei.gxdt.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.mobads.AdSettings;
import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.google.gson.Gson;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.home.model.AdvBean;
import com.pufei.gxdt.widgets.GlideApp;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.comm.util.AdError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by tb on 2018/6/2.
 */

public class AdvUtil {
    public static void setAdvBaiDu(Context context, RelativeLayout layout) {
        AdSettings.setKey(new String[]{"baidu", "中国"});
        String adPlaceID = "5831972";//重要：请填上你的代码位 ID,否则无法请求到广告
        AdView adView = new AdView(context, adPlaceID);
        adView.setListener(new AdViewListener() {
            public void onAdSwitch() {
                Log.w("", "onAdSwitch");
            }

            public void onAdShow(JSONObject info) {
                // 广告已经渲染出来
                Log.w("tb", "onAdShow " + info.toString());
            }

            public void onAdReady(AdView adView) {
                // 资源已经缓存完毕，还没有渲染出来
                Log.w("tb", "onAdReady " + adView);
            }

            public void onAdFailed(String reason) {
                Log.w("tb", "onAdFailed " + reason);
            }

            public void onAdClick(JSONObject info) {
                // Log.w("tb", "onAdClick " + info.toString());

            }

            @Override
            public void onAdClose(JSONObject arg0) {
                Log.w("tb", "onAdClose");
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        int winW = dm.widthPixels;
        int winH = dm.heightPixels;
        int width = Math.min(winW, winH);
        int height = width * 3 / 20;
        //将 adView 添加到父控件中（注：该父控件不一定为您的根控件，只要该控件能通过 addView添加广告视图即可）
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(width,
                height);
        rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layout.addView(adView, rllp);

    }
    public static  void setAdvTecent(Context context , RelativeLayout layout){
        BannerView bv;
        String posId ="5030634484990442";
        bv = new BannerView((Activity) context, ADSize.BANNER,"1106938548",posId);
        // 注意：如果开发者的banner不是始终展示在屏幕中的话，请关闭自动刷新，否则将导致曝光率过低。
        // 并且应该自行处理：当banner广告区域出现在屏幕后，再手动loadAD。
        bv.setRefresh(30);
        bv.setADListener(new AbstractBannerADListener() {

            @Override
            public void onNoAD(AdError error) {
                Log.i(
                        "tb",
                        String.format("Banner onNoAD，eCode = %d, eMsg = %s", error.getErrorCode(),
                                error.getErrorMsg()));
            }

            @Override
            public void onADReceiv() {
                Log.i("tb", "ONBannerReceive");
            }
        });
        layout.addView(bv);
        bv.loadAD();
    }
    public  static  void  getAdvHttp(final Context context, final  RelativeLayout layout,int position){
        try {
            JSONObject jsonObject = KeyUtil.getJson(context);
                jsonObject.put("position", position+"");
                OkhttpUtils.post(UrlString.GET_ADV, jsonObject.toString(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        LogUtils.i("tb",result);
                        try {
                            JSONObject resultObj = new JSONObject(result);
                            if(resultObj.optJSONObject("result").optJSONObject("data")!=null){
                                final AdvBean advBean = new Gson().fromJson(result, AdvBean.class);
                                    if("2".equals(advBean.getResult().getType())){
                                        setAdvBaiDu(context,layout);
                                    }else if("3".equals(advBean.getResult().getType())){
                                        setAdvTecent(context,layout);
                                    }else if("1".equals(advBean.getResult().getType())){
                                        Activity activity  =(Activity) context;
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                View view = LayoutInflater.from(context).inflate(R.layout.image_view,null);
                                                ImageView imageView =  view.findViewById(R.id.adv_image);
                                                GlideApp.with(context).load(advBean.getResult().getData().getImage()).into(imageView);
                                                layout.addView(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                                            }
                                        });
                                    }else{
                                        Activity activity  =(Activity) context;
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                layout.setVisibility(View.GONE);
                                            }
                                        });
                                    }

                            }else{
                                String advType = resultObj.optJSONObject("result").optString("type") ;
                                    if("2".equals(advType)){
                                        setAdvBaiDu(context,layout);
                                    }else if("3".equals(advType)){
                                        setAdvTecent(context,layout);
                                    }else if("1".equals(advType)){
                                        //layout.setVisibility(View.INVISIBLE);
                                        Activity activity  =(Activity) context;
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                layout.setVisibility(View.GONE);
                                            }
                                        });

                                    }else{
                                        Activity activity  =(Activity) context;
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                layout.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }


                    }
                });
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
