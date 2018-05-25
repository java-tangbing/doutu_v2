package com.pufei.gxdt.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * 友盟自定义事件统计
 */

public class UmengStatisticsUtil {
    public static void statisticsEvent(Context context, String eventId, String key, String value) {
        HashMap<String,String> mobcMap1 = new HashMap<String,String>();
        mobcMap1.put(key,value);
        MobclickAgent.onEvent(context, eventId, mobcMap1);
    }
}
