package com.pufei.gxdt.module.home.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.home.adapter.OtherPictureAdapter;
import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.PictureDetailBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.presenter.ImageTypePresenter;
import com.pufei.gxdt.module.home.view.ImageTypeView;
import com.pufei.gxdt.module.login.activity.LoginActivity;
import com.pufei.gxdt.module.maker.activity.EditImageActivity;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.widgets.GlideApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**表情操作
 * Created by tb on 2018/5/23.
 */

public class PictureDetailActivity extends BaseMvpActivity<ImageTypePresenter> implements ImageTypeView{
    @BindView(R.id.iv_now_picture)
    ImageView iv_picture;
    @BindView(R.id.rl_picture)
    RecyclerView rl_picture;
    @BindView(R.id.tv_eyes)
    TextView tv_eyes;
    @BindView(R.id.tv_hot)
    TextView tv_hot;
    @BindView(R.id.tv_change_img)
    TextView tv_change_img;
    private int index;
    private String URL;
    private String path = Environment.getExternalStorageDirectory().getPath() + "/斗图大师";
    private List<PictureResultBean.ResultBean> pictureList = new ArrayList<>();
    private PictureDetailBean.ResultBean pictureDetailBean;
    private OtherPictureAdapter adapter;
    @Override
    public void initView() {
        Bundle bundle = this.getIntent().getExtras();
        index = bundle.getInt("picture_index");
        pictureList = (List<PictureResultBean.ResultBean>) bundle.getSerializable("picture_list");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rl_picture.setLayoutManager(linearLayoutManager);
        adapter = new OtherPictureAdapter(PictureDetailActivity.this,pictureList);
        rl_picture.setAdapter(adapter);
        adapter.setOnItemClickListener(new OtherPictureAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                GlideApp.with(PictureDetailActivity.this).load(pictureList.get(postion).getUrl()).placeholder(R.mipmap.loading).into(iv_picture);
                index = postion;
                URL = pictureList.get(postion).getUrl();
                refreshPictureData();
            }
        });

    }

    @Override
    public void getData() {
        joinPicture();
        URL = pictureList.get(index).getUrl();
        refreshPictureData();
    }
    private void setUserData(PictureDetailBean  pictureDetailBeans){
        if(pictureDetailBeans!=null){
            tv_eyes.setText(pictureDetailBeans.getResult().getView());
            tv_hot.setText(pictureDetailBeans.getResult().getHot());
            tv_change_img.setText(pictureDetailBeans.getResult().getCount());
        }
    }
    private void joinPicture(){
        GlideApp.with(this).load(pictureList.get(index).getUrl()).placeholder(R.mipmap.loading).into(iv_picture);
    }
    private void refreshPictureData(){
        getImageDetail();
    }
    @OnClick({R.id.look_edit_image_iv,R.id.ib_dowm_load,R.id.activity_home1_shoucang,R.id.tv_share_qq,R.id.tv_share_wx})
    public void viewClick(View view){
        switch (view.getId()){
            case R.id.look_edit_image_iv:
                Intent intent = new Intent(this, EditImageActivity.class);
                Bundle bundle =  new Bundle();
                bundle.putSerializable("picture_bean",(Serializable)pictureDetailBean);
                intent.putExtras(bundle);
                intent.putExtra(EditImageActivity.EDIT_TYPE,EditImageActivity.EDIT_TYPE_EDIT);
                startActivity(intent);
                break;
            case R.id.tv_share_qq:
                break;
            case R.id.tv_share_wx:
                break;
            case  R.id.ib_dowm_load:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GetImageInputStream(URL);
                    }
                }).start();
                break;
            case R.id.activity_home1_shoucang:
                if(App.userBean!=null){
                    if (NetWorkUtil.isNetworkConnected(PictureDetailActivity.this)) {
                        try {
                            JSONObject jsonObject = KeyUtil.getJson(this);
                            jsonObject.put("auth", SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH));
                            jsonObject.put("type", 1 + "");
                            jsonObject.put("url", URL);
                            presenter.addFavorite(RetrofitFactory.getRequestBody(jsonObject.toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        ToastUtils.showShort(PictureDetailActivity.this,"无网络连接");
                    }
                }else{
                    startActivity(new Intent(this, LoginActivity.class));
                }

                break;
        }
    }
    private  void getImageDetail(){
        if (NetWorkUtil.isNetworkConnected(PictureDetailActivity.this)) {
            try{
                JSONObject jsonObject = KeyUtil.getJson(this);
                jsonObject.put("id",pictureList.get(index).getId());
                jsonObject.put("orginid",pictureList.get(index).getOrginid());
                jsonObject.put("orgintable",pictureList.get(index).getOrgintable());
                presenter.getImageDetailList(RetrofitFactory.getRequestBody(jsonObject.toString()));
            }catch (JSONException e){
                e.printStackTrace();
            }

        }else{
            ToastUtils.showShort(PictureDetailActivity.this,"无网络连接");
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_look;
    }

    @Override
    public void setPresenter(ImageTypePresenter presenter) {
        if (presenter == null) {
            this.presenter = new ImageTypePresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void resultHotImage(PictureResultBean bean) {

    }

    @Override
    public void resultImageDetail(PictureDetailBean bean) {
        if(bean!=null){
            pictureDetailBean = bean.getResult();
            setUserData(bean);
        }

    }

    @Override
    public void resultAddFavorite(FavoriteBean bean) {
        if(bean!=null){
            if("0".equals(bean.getCode())){
                ToastUtils.showShort(this,"收藏成功");
            }else {
                ToastUtils.showShort(this,bean.getMsg());
            }

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

    public void SavaImage(InputStream inputStream, final String path) {//保存图片
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创建它
        if (!file.exists()) {
            file.mkdir();
        }
        String [] a=URL.split("/");
        final String fileName = a[a.length-1];
        //final String fileName = System.currentTimeMillis() + ".gif";
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
            MediaStore.Images.Media.insertImage(PictureDetailActivity.this.getContentResolver(),//将图片插入系统图库
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//保存成功，通知系统更新相册
        Uri uri = Uri.fromFile(filena);
        intent.setData(uri);
        PictureDetailActivity.this.sendBroadcast(intent);
        PictureDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(PictureDetailActivity.this, "图片已保存到" + path+"/"+fileName);
            }
        });
    }
}
