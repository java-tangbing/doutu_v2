package com.pufei.gxdt.module.update.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.RemoteViews;

import com.pufei.gxdt.BuildConfig;
import com.pufei.gxdt.MainActivity;
import com.pufei.gxdt.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by wangwenzhang on 2016/12/6.
 */
public class UpdateService extends Service {
    private final int NotificationID = 1000;
    private NotificationCompat.Builder builder;
    private int fileSize;
    int downLoadFileSize;
    //private HttpHandler<File> mDownLoadHelper;
    private NotificationManager mNotificationManager = null;
    private Notification mNotification;
    private String app_name = "搞笑斗图大师.apk";
    // 文件下载路径
    private String APK_url = null;
    RemoteViews contentView;
    // 文件保存路径(如果有SD卡就保存SD卡,如果没有SD卡就保存到手机包名下的路径)
    private String APK_dir = "";
    private URL url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 定义一个Handler，用于处理下载线程与UI间通讯
            switch (msg.what) {
                case 1:
                    break;
                case 2:
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        boolean b = getPackageManager().canRequestPackageInstalls();
//                        if (b) {
//                            installApk(new File(APK_dir, app_name), UpdateService.this);
//                        } else {
//                            //请求安装未知应用来源的权限
//                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 10010);
//                        }
//                    } else {
                    installApk(new File(APK_dir, app_name), UpdateService.this);
//                    }

                    stopSelf();
                    break;
                case -1:
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initAPKDir();// 创建保存路径

        //FragmentManager fragmentManager=getA
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 接收Intent传来的参数:
        APK_url = intent.getStringExtra("apk_url");
        //notificationInit();
        Log.i("什么个情况1", APK_url);
        new Task().execute(APK_url);
        Log.i("什么个情况", APK_url);
        return super.onStartCommand(intent, flags, startId);
    }

    class Task extends AsyncTask<String, Integer, Object> {
        @Override
        protected Object doInBackground(String... params) {

            String name = "my_package_channel"; // There are hardcoding only for show it's just strings
            String id = "my_package_channel_1"; // The user-visible name of the channel.
            String description = "my_package_first_channel"; // The user-visible description of the channel.

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);//点击进度条，进入程序
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
            mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
                if (channel != null) {
                    channel.setDescription(description);
                    channel.enableVibration(true);
                    channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mNotificationManager.createNotificationChannel(channel);
                }
                builder = new NotificationCompat.Builder(getApplicationContext(), id);
                builder.setContentTitle(getApplicationName())
                        .setSmallIcon(R.mipmap.ic_logo)
                        .setContentText("正在下载,请稍后...")
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setAutoCancel(true)
                        .setContentIntent(pIntent)
                        .setTicker("正在下载新版本")
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            } else {
                builder = new NotificationCompat.Builder(getApplicationContext());
                builder.setContentTitle(getApplicationName())
                        .setSmallIcon(R.mipmap.ic_logo)
                        .setContentText("正在下载,请稍后...")
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setAutoCancel(true)
                        .setContentIntent(pIntent)
                        .setTicker("正在下载新版本")
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                        .setPriority(Notification.PRIORITY_HIGH);
            }

            mNotification = new Notification();
            contentView = new RemoteViews(getPackageName(), R.layout.update_item);
            builder.setContent(contentView);//通知栏中进度布局
            mNotification = builder.build();
            mNotification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
            mNotification.tickerText = "正在下载新版本";
            mNotificationManager.notify(NotificationID, mNotification);
            URL url;
            HttpURLConnection connection = null;
            fileSize = 0;
            downLoadFileSize = 0;
            try {
                url = new URL(params[0]);
                Log.i("什么个情况2", APK_url);
                connection = (HttpURLConnection) url.openConnection();
                Log.i("什么个情况3", APK_url);
                connection.setConnectTimeout(4000); //超时设置
                Log.i("什么个情况4", APK_url);
                //connection.setDoInput(true);
                Log.i("什么个情况5", APK_url);
                fileSize = connection.getContentLength();
                Log.i("什么个情况6", APK_url);
                //connection.setUseCaches(false); //设置不使用缓存
                Log.i("什么个情况7", APK_url);
                InputStream inputStream = connection.getInputStream();
                Log.i("什么个情况8", APK_url);
                File filena = new File(APK_dir, app_name);
                try {
                    int i = 0;
                    FileOutputStream fileOutputStream = new FileOutputStream(filena);
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    byte[] bytes = new byte[2048];
                    Log.i("什么个情况3", APK_url);
                    while ((i = bis.read(bytes)) != -1) {
                        fileOutputStream.write(bytes, 0, i);
                        int b = (int) ((downLoadFileSize / (float) fileSize) * 100);
                        downLoadFileSize += i;
                        int a = (int) ((downLoadFileSize / (float) fileSize) * 100);
                        if ((a - b) >= 1) {
                            publishProgress(a);
                        }
                        //sendMsg(1);// 更新进度条
                    }
                    Log.i("什么个情况4", APK_url);
                    sendMsg(2);// 下载完成
                    contentView.setTextViewText(R.id.textView4, "下载完成 准备安装");
                    mNotificationManager.notify(NotificationID, mNotification);
                    Log.i("什么个情况9", APK_url);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            /*builder.setProgress(100,values[0],false);
            builder.setContentInfo(getPercent(values[0],100));
            mNotificationManager.notify(NotificationID, builder.build());*/
            Log.i("进度", values[0] + "");
            contentView.setTextViewText(R.id.textView4, "正在下载:" + values[0] + "%");
            contentView.setProgressBar(R.id.content_view_progress, 100, values[0], false);
            mNotificationManager.notify(NotificationID, mNotification);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

    private void initAPKDir() {
        /**
         * 创建路径的时候一定要用[/],不能使用[\],但是创建文件夹加文件的时候可以使用[\].
         * [/]符号是Linux系统路径分隔符,而[\]是windows系统路径分隔符 Android内核是Linux.
         */
        if (isHasSdcard())// 判断是否插入SD卡
        {
            APK_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Club/download/";// 保存到SD卡路径下
        } else {
            APK_dir = getApplicationContext().getFilesDir().getAbsolutePath() + "/Club/download/";// 保存到app的包名路径下
        }
        File destDir = new File(APK_dir);
        if (!destDir.exists()) {// 判断文件夹是否存在
            destDir.mkdirs();
        }
    }

    /**
     * @Description:判断是否插入SD卡
     */
    private boolean isHasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param x     当前值
     * @param total 总值
     *              [url=home.php?mod=space&uid=7300]@return[/url] 当前百分比
     * @Description:返回百分之值
     */
    private String getPercent(int x, int total) {
        String result = "";// 接受百分比的值
        double x_double = x * 1.0;
        double tempresult = x_double / total;
        // 百分比格式，后面不足2位的用0补齐 ##.00%
        DecimalFormat df1 = new DecimalFormat("0.00%");
        result = df1.format(tempresult);
        return result;
    }

    private String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    private void sendMsg(int flag) {
        Message msg = new Message();
        msg.what = flag;
        handler.sendMessage(msg);
    }

    private void notificationInit() {
        //通知栏内显示下载进度条
        Intent intent = new Intent(this, MainActivity.class);//点击进度条，进入程序
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotification = new Notification();
        //mNotification.icon=R.drawable.ic_launcher;
        mNotification.tickerText = "开始下载";
        mNotification.contentView = new RemoteViews(getPackageName(), R.layout.update_item);//通知栏中进度布局
        mNotification.contentIntent = pIntent;
//  mNotificationManager.notify(0,mNotification);
    }

    public static void installApk(File file, Context context) {
        //L.i("msg", "版本更新获取sd卡的安装包的路径=" + file.getAbsolutePath());
        Intent openFile = getFileIntent(context, file);
        context.startActivity(openFile);
    }

    public static Intent getFileIntent(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
//        Uri uri = Uri.fromFile(file);
//        String type = getMIMEType(file);
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(uri, type);
        return intent;
    }

    public static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        // 取得扩展名
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length());
        if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else {
            // /*如果无法直接打开，就跳出软件列表给用户选择 */
            type = "*/*";
        }
        return type;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
