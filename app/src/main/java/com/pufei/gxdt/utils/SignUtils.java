package com.pufei.gxdt.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.pufei.gxdt.app.App;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by wangwenzhang on 2017/8/11.
 */

public class SignUtils {
    private static SharedPreferences setting;
    private static SignUtils signUtils;
    public static SignUtils getInstance(Context context){
        if (signUtils == null)
        {
            synchronized (DialogUtil.class)
            {
                if (signUtils == null)
                {
                    setting=context.getSharedPreferences(UrlString.SHARE_TAG,0);
                    signUtils = new SignUtils();
                }
            }
        }
        return signUtils;
    }
    public   void setTime(long time){
        setting.edit().putLong(App.token,time).apply();
    }
    public   boolean getTime() {
        long time=setting.getLong(App.token,0);
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = new Date(time);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }
    public static boolean IsToday(long day) {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = new Date(day);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

}
