package com.pufei.gxdt.module.home.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.home.adapter.MyfourPagerAdapder;
import com.pufei.gxdt.module.home.fragment.ImageFragment;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.widgets.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangwenzhang on 2017/2/22.
 */
public class ImageActivity extends FragmentActivity {
    @BindView(R.id.activity_iamge_back)
    ImageView activityIamgeBack;
    @BindView(R.id.activity_iamge_vp)
    ViewPager activityIamgeVp;
    @BindView(R.id.fragment_image_tv1)
    TextView fragmentImageTv1;
    @BindView(R.id.fragment_image_tv2)
    TextView fragmentImageTv2;
    @BindView(R.id.activity_image_menu)
    ImageView activityImageMenu;
    @BindView(R.id.activity_image_down)
    ImageView activityImageDown;
    @BindView(R.id.activity_image_bar)
    TextView activityImageBar;
    private JSONArray jsonObject;
    private int position1;
    private List<Fragment> fragmentList;
    private MyfourPagerAdapder adapder;
    private List<String> list;
    private String URL;
    private static String path = Environment.getExternalStorageDirectory().getPath() + "/斗图大师";
    private static AlertDialog sharedialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        //StatusBarUtil.StatusBarLightMode(this);
        //fristStart();
        StatusBarCompat.compat(this, getResources().getColor(R.color.black));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //ButterKnife.inject(this);
/// TODO: 2017/2/22  
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle = intent.getExtras();
        try {
            jsonObject = new JSONArray(bundle.getString("url"));
            position1 = bundle.getInt("position");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addfragment();
        adapder = new MyfourPagerAdapder(getSupportFragmentManager(), fragmentList, list);
        activityIamgeVp.setAdapter(adapder);
        int a = position1;
        URL = list.get(position1);
        fragmentImageTv2.setText("/" + fragmentList.size());
        fragmentImageTv1.setText((position1 + 1) + "");
        activityIamgeVp.setCurrentItem(a);
        activityIamgeVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fragmentImageTv1.setText((position + 1) + "");
                URL = list.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void addfragment() {
        fragmentList = new ArrayList<>();
        list = new ArrayList<>();
        for (int i = 0; i < jsonObject.length(); i++) {
            try {
                fragmentList.add(new ImageFragment(i, jsonObject.get(i).toString(), jsonObject.length()));
                list.add(jsonObject.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.activity_iamge_back)
    public void onClick() {
        AppManager.getAppManager().finishActivity();
    }


    @OnClick({R.id.activity_image_menu, R.id.activity_image_down})
    public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.activity_image_menu:
                    android.util.Log.e("分享-------", "11111111111111");
                    showShare();
                    break;
                case R.id.activity_image_down:
                    android.util.Log.e("分享-------", "11111111111111");
                    ToastUtils.showShort(ImageActivity.this,"正在下载");
                    new Task().execute(URL);
                    break;
        }
    }

    private void showShare() {//分享
        if (URL != null) {
            try {
                new ShareAction(ImageActivity.this)
                        .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setShareboardclickCallback(shareBoardlistener).open();
            } catch (NullPointerException e) {
                ToastUtils.showShort(ImageActivity.this, "选择的内容为空，请重试");
                e.printStackTrace();
            }
        }
    }

    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {
        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            if (share_media != null && URL != null) {
                if (share_media == SHARE_MEDIA.WEIXIN) {
                    UMImage image = new UMImage(ImageActivity.this, URL);
                    image.compressStyle = UMImage.CompressStyle.SCALE;
                    image.compressStyle = UMImage.CompressStyle.QUALITY;
                    new ShareAction(ImageActivity.this).withMedia(image)
                            .setPlatform(share_media)
                            .setCallback(umShareListener).share();
                } else {
                    UMImage image = new UMImage(ImageActivity.this, URL);
                    image.compressStyle = UMImage.CompressStyle.SCALE;
                    image.compressStyle = UMImage.CompressStyle.QUALITY;
                    new ShareAction(ImageActivity.this).withMedia(image)
                            .setPlatform(share_media)
                            .setCallback(umShareListener).share();
                }
            }
        }
    };
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            shareDialog(ImageActivity.this);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(ImageActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
//            if (App.userBean!=null){
//                MissionUtils.getInstance().getDayMission(App.token,"3","image");
//            }
            Log.d("plat", "platform" + platform);
            sharedialog.dismiss();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            sharedialog.dismiss();
            Toast.makeText(ImageActivity.this, platform + " 分享失败啦" + t.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            sharedialog.dismiss();
            Toast.makeText(ImageActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    class Task extends AsyncTask<String, Integer, Void> {//异步任务

        protected Void doInBackground(String... params) {
            GetImageInputStream(params[0]);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    public void GetImageInputStream(String imageurl) {//下载图片
        java.net.URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(4000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream = connection.getInputStream();
            SavaImage(inputStream, path);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  void shareDialog(Activity activity){
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.img_animation);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        sharedialog=new AlertDialog.Builder(activity, R.style.TransDialogStyle).create();
        if (!activity.isFinishing()){
            sharedialog.show();
        }
        Window window=sharedialog.getWindow();
        window.setContentView(R.layout.share_dialog);
        ImageView imageView= (ImageView) window.findViewById(R.id.share_dialog_image);
        imageView.setAnimation(animation);
    }
    public void SavaImage(InputStream inputStream, final String path) {//保存图片
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创建它
        if (!file.exists()) {
            file.mkdir();
        }
        final String fileName = System.currentTimeMillis() + ".gif";
        File filena = new File(file, fileName);
        try {
            int i = 0;
            fileOutputStream = new FileOutputStream(filena);
            byte[] bytes = new byte[2048];
            while ((i = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, i);
            }
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            MediaStore.Images.Media.insertImage(ImageActivity.this.getContentResolver(),//将图片插入系统图库
                    file.getAbsolutePath(), fileName, null);
            MediaScannerConnection.scanFile(ImageActivity.this, new String[] {path+fileName}, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//保存成功，通知系统更新相册
        Uri uri = Uri.fromFile(filena);
        intent.setData(uri);
        ImageActivity.this.sendBroadcast(intent);
        ImageActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(ImageActivity.this,"图片已下载到"+path+"/"+fileName);
            }
        });
    }
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final int statusHeight = getStatusBarHeight();
            ImageActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) activityImageBar.getLayoutParams();
                    params.height=statusHeight;
                    //params.setMargins(0, DensityUtils.dp2px(MainActivity.this,statusHeight), 0, 0);
                    activityImageBar.setLayoutParams(params);
                }
            });
        }
    }

    protected int getStatusBarHeight() {
        try {
           /* Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());*/
            int statusBarHeight1 = -1;
//获取status_bar_height资源的ID
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
            }
            return statusBarHeight1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
