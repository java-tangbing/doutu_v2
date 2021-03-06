package com.pufei.gxdt.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.update.model.UpdateBean;
import com.pufei.gxdt.module.update.service.UpdateService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.WIFI_SERVICE;

/**
 *
 */
public class StartUtils {
    private static StartUtils startUtils;
    private static Activity activity;
    private ArrayList<AppInfo> appList = new ArrayList();
    private SharedPreferences setting;
    private String SHARE_APP_TAG = "fristduqu";
    private String newVersion = "";
    private String oldVersion = "";
    private String des;
    private AlertDialog dialog;
    private int newcode;
    private int oldcode;

    public StartUtils() {
    }

    public static StartUtils getInstance(Activity activity1) {
        activity = activity1;
        if (startUtils == null) {
            synchronized (StartUtils.class) {
                if (startUtils == null) {
                    startUtils = new StartUtils();
                }
            }
        }
        return startUtils;
    }


    public void detection() {//获取服务器版本号
        if (!NetWorkUtil.isNetworkConnected(App.AppContext)) {
            Toast.makeText(App.AppContext, "当前网络不可用，请检查网络情况", Toast.LENGTH_LONG).show();
            return;
        }
        JSONObject jsonObject = KeyUtil.getJson(activity);
        try {
            OkhttpUtils.post(Contents.Update, jsonObject.toString(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(StartUtils.class.getSimpleName(), e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    UpdateBean updateBean = null;
                    try {
                        updateBean = new Gson().fromJson(result, UpdateBean.class);
                        newVersion = updateBean.getResult().getVersion();
                        final String upURl = updateBean.getResult().getLink();
                        final boolean update = updateBean.getResult().getUpdateOpen().equals("1");
                        final boolean isIgnore = updateBean.getResult().getIsIgnore().equals("1");
                        final boolean isForce = updateBean.getResult().getIsForce().equals("1");
                        final String channel = updateBean.getResult().getChannel();
                        des = updateBean.getResult().getDes();
                        newcode = Integer.parseInt(updateBean.getResult().getVersion_code());
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PackageManager pm = activity.getApplicationContext().getPackageManager();
                                PackageInfo pi = null;
                                try {
                                    pi = pm.getPackageInfo(activity.getApplicationContext().getPackageName(), 0);
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }
                                if (pi != null) {
                                    oldVersion = pi.versionName;
                                    oldcode = pi.versionCode;
                                }
                                if (update && newcode > oldcode) {
                                    Log.i("执行了么", "en");
                                    Update(upURl, isForce, newVersion, channel);
                                }
                            }
                        });
                    } catch (JsonSyntaxException e) {
                        //newVersion = "3.0";
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Update(final String URL, final boolean cance, String code, final String channel) {//更新弹窗
        if (!activity.isFinishing()) {
            dialog = new AlertDialog.Builder(activity, R.style.TransDialogStyle).create();
            Log.i("新版本", newVersion);
            dialog.setCancelable(!cance);
            dialog.show();
            Window window = dialog.getWindow();
            window.setContentView(R.layout.update);
            TextView textView = (TextView) window.findViewById(R.id.update_versition);
            TextView textView1 = (TextView) window.findViewById(R.id.update_size);
            textView.setText("发现新版本：V" + code);
            Button button1 = (Button) window.findViewById(R.id.update_bt_false);

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cance) {
                        AppManager.getAppManager().finishAllActivity();
                        AppManager.getAppManager().AppExit(App.AppContext);
                    } else {
                        dialog.dismiss();
                    }
                }
            });
            Button button2 = (Button) window.findViewById(R.id.update_bt_true);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (channel.equals("normal")) {
                        Intent intent = new Intent(activity, UpdateService.class);
                        intent.putExtra("apk_url", URL);
                        activity.startService(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(URL));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (intent.resolveActivity(activity.getPackageManager()) != null) {
                            activity.startActivity(Intent.createChooser(intent, "请选择浏览器"));
                        } else {
                            Toast.makeText(activity, "请下载浏览器", Toast.LENGTH_SHORT).show();
                        }
                    }
                    dialog.dismiss();
                    ToastUtils.showShort(activity, "开始更新");
                }
            });
            Button button3 = window.findViewById(R.id.update_bt_skip);
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cance) {
                        AppManager.getAppManager().finishAllActivity();
                        AppManager.getAppManager().AppExit(App.AppContext);
                    } else {
                        dialog.dismiss();
                        updateIgnore();
                    }
                }
            });
        }
    }

    public void duqu() throws JSONException {//向服务器发送用户手机配置信息
        setting = activity.getSharedPreferences(SHARE_APP_TAG, 0);//判断是否是第一次启动
        getAppList();
        TelephonyManager TelephonyMgr = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        PackageManager pm = activity.getApplicationContext().getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(activity.getApplicationContext().getPackageName(), 0);//获取用户应用安装列表
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        JSONArray js = new JSONArray();
        try {
            for (int i = 0; i < appList.size(); i++) {
                JSONObject params = new JSONObject();
                params.put("paname", appList.get(i).getPackagename());
                params.put("version", appList.get(i).getApp_version());
                params.put("appname", appList.get(i).getApp_name());
                js.put(params);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        int width = (wm.getDefaultDisplay().getWidth());//获取屏幕宽高
        int height = (wm.getDefaultDisplay().getHeight());
        String key = "h32nfow45e";
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String deviceid = TelephonyMgr.getDeviceId();//获取唯一辨识码
        String timestamp = Long.toString(System.currentTimeMillis());
        String os = "1";
        String vresion = pi.versionName;//获取版本号
        String net = "1";
        String screen = width + "*" + height;
        String brand = Build.MANUFACTURER + " " + Build.MODEL;//手机型号和手机厂商
        WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String mac = wifiInfo.getMacAddress();
        String wifiname = wifiInfo.getSSID();
        Log.e("StartUtils", mac);
        Log.e("StartUtils", wifiname);
        String sim_id = TelephonyMgr.getSimSerialNumber();
        if (sim_id == null) {
            sim_id = "";
        }
        String android_id = Settings.System.getString(activity.getContentResolver(), Settings.System.ANDROID_ID);
        if (android_id == null) {
            android_id = "";
        }
        String loc = "";
        String sign = md5(md5(timestamp + key + vresion));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sign", sign);
        jsonObject.put("key", key);
        jsonObject.put("deviceid", deviceid);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("os", os);
        jsonObject.put("version", vresion);
        jsonObject.put("net", net);
        jsonObject.put("screen", screen);
        jsonObject.put("brand", brand);
        jsonObject.put("loc", loc);
        jsonObject.put("sim_id", sim_id);
        jsonObject.put("android_id", android_id);
        jsonObject.put("cid", readDev0());
        jsonObject.put("serialno", getSerialno());
//        jsonObject.put("channel", AnalyticsConfig.getChannel(activity) + "");
        jsonObject.put("mac", mac);
        jsonObject.put("wifiname", wifiname);
        jsonObject.put("getSimSerialNumber", TelephonyMgr.getSimSerialNumber() + "");
        jsonObject.put("getLine1Number", "" + TelephonyMgr.getLine1Number());
        jsonObject.put("getSimOperator", "" + TelephonyMgr.getSimOperator());
        jsonObject.put("getSubscriberId", "" + TelephonyMgr.getSubscriberId());
        jsonObject.put("getSimOperatorName", "" + TelephonyMgr.getSimOperatorName());
        jsonObject.put("getNetworkOperator", "" + TelephonyMgr.getNetworkOperator());
        jsonObject.put("getNetworkOperatorName", "" + TelephonyMgr.getNetworkOperatorName());
        jsonObject.put("getNetworkType", "" + TelephonyMgr.getNetworkType());
        jsonObject.put("getSimState", "" + TelephonyMgr.getSimState());
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File("system/build.prop"));//手机配置文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String buildprop = getString(inputStream);
        jsonObject.put("pnamelist", js);
        jsonObject.put("systeminfo", buildprop);
        String json = jsonObject.toString();
        try {
            OkhttpUtils.post(Contents.friststart, json, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //ToastUtils.showShort(WebActivity.this,"第一次");
                    Log.i("发送成功没", "发送失败");
                    setting.edit().putBoolean("FIRST", true).apply();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i("发送成功没", response.body().string());
                    setting.edit().putBoolean("FIRST", false).apply();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getSerialno() {
        Class<?> c = null;
        String serialnum = null;
        try {
            c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
            return serialnum;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void getAppList() {//获取用户安装应用列表
        PackageManager pm = activity.getPackageManager();
        // Return a List of all packages that are installed on the device.
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {             // 判断系统/非系统应用
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) // 非系统应用
            {
                AppInfo info = new AppInfo();
                info.setApp_name(packageInfo.applicationInfo.loadLabel(pm).toString());
                info.setPackagename(packageInfo.packageName);
                info.setApp_icon(packageInfo.applicationInfo.loadIcon(pm));              // 获取该应用安装包的Intent，用于启动该应用
                try {
                    info.setApp_version(pm.getPackageInfo(packageInfo.packageName, 0).versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                appList.add(info);
            }
        }
    }

    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String readDev0() {
        String str1 = null;
        Object localOb;
        try {
            localOb = new FileReader("/sys/block/mmcblk0/device/type");
            localOb = new BufferedReader((Reader) localOb).readLine()
                    .toLowerCase().contentEquals("mmc");
            if (localOb != null) {
                str1 = "/sys/block/mmcblk0/device/";
            }
            localOb = new FileReader(str1 + "cid"); // nand ID
            str1 = new BufferedReader((Reader) localOb).readLine();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return str1;
    }

    public static String md5(String string) {//MD5加密
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public void updateIgnore() {
        if (!NetWorkUtil.isNetworkConnected(App.AppContext)) {
            Toast.makeText(App.AppContext, "当前网络不可用，请检查网络情况", Toast.LENGTH_LONG).show();
            return;
        }
        JSONObject jsonObject = KeyUtil.getJson(activity);
        try {
            OkhttpUtils.post(Contents.Ignore, jsonObject.toString(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(StartUtils.class.getSimpleName(), e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Log.i("Ignore", result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
