package com.pufei.gxdt.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    public static void setAdvBaiDu(final  Context context, final RelativeLayout layout) {
        final Activity activity  =(Activity) context;
        AdSettings.setKey(new String[]{"baidu", "中国"});
        String adPlaceID = "5831972";//重要：请填上你的代码位 ID,否则无法请求到广告
        final AdView adView = new AdView(context, adPlaceID);
        adView.setListener(new AdViewListener() {
            public void onAdSwitch() {

            }

            public void onAdShow(JSONObject info) {
                // 广告已经渲染出来
            }

            public void onAdReady(AdView adView) {
                // 资源已经缓存完毕，还没有渲染出来
            }

            public void onAdFailed(String reason) {
                Log.w("tb", "onAdFailed " + reason);
            }

            public void onAdClick(JSONObject info) {

            }

            @Override
            public void onAdClose(JSONObject arg0) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       layout.setVisibility(View.GONE);
                    }
                });
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        int winW = dm.widthPixels;
        int winH = dm.heightPixels;
        int width = Math.min(winW, winH);
        int height = width * 3 / 20;
        //将 adView 添加到父控件中（注：该父控件不一定为您的根控件，只要该控件能通过 addView添加广告视图即可）
        final RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(width,
                height);
        rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout.addView(adView, rllp);
            }
        });
    }
    public static  void setAdvTecent(final Context context , final RelativeLayout layout){
        final Activity activity  =(Activity) context;
        final BannerView bv;
        String posId ="5030634484990442";
        bv = new BannerView((Activity) context, ADSize.BANNER,"1106938548",posId);
        bv.setRefresh(30);
        bv.setADListener(new AbstractBannerADListener() {

            @Override
            public void onNoAD(AdError error) {
            }

            @Override
            public void onADReceiv() {
            }

            @Override
            public void onADClosed() {
                super.onADClosed();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        layout.setVisibility(View.GONE);
                    }
                });
            }
        });
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout.addView(bv);
                bv.loadAD();
            }
        });

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
                                                ImageView imageView =  layout.findViewById(R.id.iv_adv);
                                                imageView.setVisibility(View.VISIBLE);
                                                GlideApp.with(context).load(advBean.getResult().getData().getImage()).error(R.mipmap.newloding).into(imageView);
                                                imageView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if(advBean.getResult().getData().getLink()!=null){
                                                            Intent intent = new Intent();
                                                            intent.setAction("android.intent.action.VIEW");
                                                            Uri content_url = Uri.parse(advBean.getResult().getData().getLink()+"");
                                                            intent.setData(content_url);
                                                            context.startActivity(intent);
                                                        }
                                                    }
                                                });
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
