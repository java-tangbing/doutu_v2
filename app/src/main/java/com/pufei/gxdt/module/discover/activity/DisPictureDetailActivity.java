package com.pufei.gxdt.module.discover.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.Contents;
import com.pufei.gxdt.module.discover.adapter.DisOtherPictureAdapter;
import com.pufei.gxdt.module.discover.adapter.DisPictureAdapter;
import com.pufei.gxdt.module.discover.bean.DisPicDetBean;
import com.pufei.gxdt.module.discover.bean.DiscoverEditImageBean;
import com.pufei.gxdt.module.discover.bean.DiscoverListBean;
import com.pufei.gxdt.module.discover.presenter.DisPicDetPresenter;
import com.pufei.gxdt.module.discover.view.DisPicDetView;
import com.pufei.gxdt.module.home.activity.PictureDetailActivity;
import com.pufei.gxdt.module.home.model.FavoriteBean;
import com.pufei.gxdt.module.home.model.PictureDetailBean;
import com.pufei.gxdt.module.login.activity.LoginActivity;
import com.pufei.gxdt.module.maker.activity.EditImageActivity;
import com.pufei.gxdt.utils.AppManager;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 表情操作
 * Created by tb on 2018/5/23.
 */

public class DisPictureDetailActivity extends BaseMvpActivity<DisPicDetPresenter> implements DisPicDetView, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.iv_now_picture)
    ImageView iv_picture;
    @BindView(R.id.look_edit_image_iv)
    TextView iv_editimage;
    //    @BindView(R.id.activity_home1_cancel)
//    LinearLayout linearLayout_cancel;
    @BindView(R.id.rl_picture)
    RecyclerView rl_picture;
    @BindView(R.id.activity_home1_shoucang)
    ImageButton activity_home1_shoucang;

    @BindView(R.id.activity_home1_title)
    TextView textViewTitle;


    @BindView(R.id.tv_eyes)
    TextView tv_eyes;
    @BindView(R.id.tv_hot)
    TextView tv_hot;
    @BindView(R.id.tv_change_img)
    TextView tv_change_img;
    private String path;

    private int index;
    private String orginid, orgintable, id, uid, mcount, isSaveImg;
    private List<DiscoverListBean.ResultBean> pictureList = new ArrayList<>();
    //    private List<DiscoverListBean.ResultBean> sendpictureList = new ArrayList<>();
    private List<DiscoverListBean.ResultBean> mlist = new ArrayList<>();

    private List<DiscoverEditImageBean.ResultBean.DataBean> pictureList01 = new ArrayList<>();
    private List<DiscoverEditImageBean.ResultBean.DataBean> mlist01 = new ArrayList<>();
    private DisOtherPictureAdapter adapter;
    private DisPictureAdapter adapter01;
    private String URL;
    private int type = 0;

    @Override
    public void initView() {
        path = Environment.getExternalStorageDirectory().getPath() + "/" + getResources().getString(R.string.dtds);
        Intent intent = getIntent();
        isSaveImg = intent.getStringExtra("isSaveImg");
        id = intent.getStringExtra("id");
        orginid = intent.getStringExtra("orginid");
        orgintable = intent.getStringExtra("orgintable");
        index = intent.getIntExtra("picture_index", 0);
        if (TextUtils.isEmpty(intent.getStringExtra("type"))) {
            pictureList = (List<DiscoverListBean.ResultBean>) intent.getSerializableExtra("picture_list");
        } else {
            type = 1;
            pictureList01 = (List<DiscoverEditImageBean.ResultBean.DataBean>) intent.getSerializableExtra("picture_list");
        }


//        sendpictureList=(List<DiscoverListBean.ResultBean>) intent.getSerializableExtra("picture_list");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rl_picture.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void getData() {
        switch (type) {
            case 0:
                adapter = new DisOtherPictureAdapter(mlist);
                adapter.setOnItemClickListener(this);
                rl_picture.setAdapter(adapter);
                break;
            case 1:
                adapter01 = new DisPictureAdapter(mlist01);
                adapter01.setOnItemClickListener(this);
                rl_picture.setAdapter(adapter01);
                break;
        }

        setMyAdapter();
        getImageDetailList();
    }


    public void setMyAdapter() {
        JSONObject jsonObject = KeyUtil.getJson(this);
        try {
            jsonObject.put("id", id);
            jsonObject.put("orginid", orginid);
            jsonObject.put("orgintable", orgintable);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.imageDetail(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } else {
            ToastUtils.showShort(this, getResources().getString(R.string.check_the_network_please));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMyAdapter();
        getImageDetailList();
    }

    public void getImageDetailList() {
        switch (type) {
            case 0:
                if (pictureList.size() > 0) {
                    mlist.clear();
                    mlist.addAll(pictureList);
                    adapter.notifyDataSetChanged();
                }
                break;
            case 1:
                if (pictureList01.size() > 0) {
                    mlist01.clear();
                    mlist01.addAll(pictureList01);
                    adapter01.notifyDataSetChanged();
                }
                break;
        }


    }

    public void setImageDetail() {
        JSONObject jsonObject = KeyUtil.getJson(this);
        try {
            if (id == null || orginid == null || orgintable == null) return;
            jsonObject.put("id", id);
            jsonObject.put("orginid", orginid);
            jsonObject.put("orgintable", orgintable);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetWorkUtil.isNetworkConnected(this)) {
            presenter.getImageDetail(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } else {
            ToastUtils.showShort(this, getResources().getString(R.string.check_the_network_please));
        }
    }


    @Override
    public void getImageDetail(DisPicDetBean bean) {
        GlideApp.with(this).load(bean.getResult().getMake_url()).placeholder(R.mipmap.ic_default_picture)
                .into(iv_picture);
        if (bean.getResult().getView().length() > 0) {
            tv_eyes.setText(bean.getResult().getView());
        }
        if (bean.getResult().getHot().length() > 0) {
            tv_hot.setText(bean.getResult().getHot());
        }
        if (bean.getResult().getCount().length() > 0) {
            tv_change_img.setText(bean.getResult().getCount());
        }
        uid = bean.getResult().getUid();
        mcount = bean.getResult().getCount();
        URL = bean.getResult().getMake_url();

        switch (type) {
            case 0:
                if ("0".equals(pictureList.get(index).getIsSaveImg())) {
                    activity_home1_shoucang.setBackgroundResource(R.mipmap.com_bt_ttab_star_normal);
                } else {
                    activity_home1_shoucang.setBackgroundResource(R.mipmap.com_bt_ttab_star_select);
                }
                break;

            case 1:
                if ("0".equals(pictureList01.get(index).getIsSaveImg())) {
                    activity_home1_shoucang.setBackgroundResource(R.mipmap.com_bt_ttab_star_normal);
                } else {
                    activity_home1_shoucang.setBackgroundResource(R.mipmap.com_bt_ttab_star_select);
                }
                break;
        }


    }

    @Override
    public void resultImageDetail(PictureDetailBean bean) {
        if (bean.getResult() != null) {
            Intent intent = new Intent(this, EditImageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("picture_bean", bean.getResult());
            intent.putExtras(bundle);
            intent.putExtra(EditImageActivity.EDIT_TYPE, EditImageActivity.EDIT_TYPE_EDIT);
            startActivity(intent);
        } else {
            ToastUtils.showShort(this, "value is none");
        }

    }

    @Override
    public void resultAddFavorite(FavoriteBean bean) {
        if (bean != null) {
            switch (type) {
                case 0:
                    if ("0".equals(bean.getCode())) {
                        pictureList.get(index).setIsSaveImg("1");
                        activity_home1_shoucang.setBackgroundResource(R.mipmap.com_bt_ttab_star_select);
                        ToastUtils.showShort(this, getResources().getString(R.string.collection_success));
                    } else {
                        pictureList.get(index).setIsSaveImg("0");
                        ToastUtils.showShort(this, bean.getMsg());
                    }

                    break;
                case 1:
                    if ("0".equals(bean.getCode())) {
                        pictureList01.get(index).setIsSaveImg("1");
                        activity_home1_shoucang.setBackgroundResource(R.mipmap.com_bt_ttab_star_select);
                        ToastUtils.showShort(this, getResources().getString(R.string.collection_success));
                    } else {
                        pictureList01.get(index).setIsSaveImg("0");
                        ToastUtils.showShort(this, bean.getMsg());
                    }

                    break;
            }

        }
    }

    @Override
    public void resultCancleFavorite(FavoriteBean bean) {
        if (bean != null) {
            switch (type) {
                case 0:
                    if ("0".equals(bean.getCode())) {
                        pictureList.get(index).setIsSaveImg("0");
                        activity_home1_shoucang.setBackgroundResource(R.mipmap.com_bt_ttab_star_normal);
                        ToastUtils.showShort(this, getResources().getString(R.string.cancel_collection_success));
                    } else {
                        pictureList.get(index).setIsSaveImg("1");
                        ToastUtils.showShort(this, bean.getMsg());
                    }
                    break;
                case 1:
                    if ("0".equals(bean.getCode())) {
                        pictureList01.get(index).setIsSaveImg("0");
                        activity_home1_shoucang.setBackgroundResource(R.mipmap.com_bt_ttab_star_normal);
                        ToastUtils.showShort(this, getResources().getString(R.string.cancel_collection_success));
                    } else {
                        pictureList01.get(index).setIsSaveImg("1");
                        ToastUtils.showShort(this, bean.getMsg());
                    }
                    break;
            }

        }

    }

    @Override
    public void requestErrResult(String msg) {

    }


    @Override
    public int getLayout() {
        return R.layout.activity_look;
    }

    @Override
    public void setPresenter(DisPicDetPresenter presenter) {
        if (presenter == null) {
            this.presenter = new DisPicDetPresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        id = mlist.get(position).getId();
        orginid = mlist.get(position).getOrginid();
        orgintable = mlist.get(position).getOrgintable();
        index = position;
        setMyAdapter();
    }


    @OnClick({R.id.look_edit_image_iv, R.id.tv_change_img, R.id.activity_finish, R.id.activity_home1_shoucang, R.id.ib_dowm_load})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.look_edit_image_iv:
                setImageDetail();
                break;
            case R.id.tv_change_img:
//                if (Integer.parseInt(mcount) > 0) {
                Intent intent01 = new Intent(this, DiscoverDetailedActivity.class);
                Bundle bundle01 = new Bundle();
                bundle01.putString("id", id);
                bundle01.putString("orginid", orginid);
                bundle01.putString("orgintable", orgintable);
                bundle01.putString("uid", uid);
                intent01.putExtras(bundle01);
                startActivity(intent01);
//                } else {
//                    ToastUtils.showShort(this, getResources().getString(R.string.none_pic));
//                }
                break;
            case R.id.ib_dowm_load:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GetImageInputStream(URL);
                    }
                }).start();
                break;
            case R.id.activity_finish:
//                Intent intent = new Intent();
////                intent.putExtra("msg", three);
//                setResult(1, intent);
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.activity_home1_shoucang:
                if (App.userBean != null) {
                    switch (type) {
                        case 0:
                            setAddFavorite0();
                            break;
                        case 1:
                            setAddFavorite1();
                            break;
                    }
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
        }
    }

    public void setAddFavorite0() {
        if ("0".equals(pictureList.get(index).getIsSaveImg())) {//加收藏
            if (NetWorkUtil.isNetworkConnected(this)) {
                try {
                    JSONObject jsonObject = KeyUtil.getJson(this);
                    jsonObject.put("auth", SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH));
                    jsonObject.put("type", 1 + "");
                    jsonObject.put("url", URL);
                    presenter.addFavorite(RetrofitFactory.getRequestBody(jsonObject.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtils.showShort(this, getResources().getString(R.string.check_the_network_please));
            }

        } else {//取消收藏
            if (NetWorkUtil.isNetworkConnected(this)) {
                try {
                    JSONObject jsonObject = KeyUtil.getJson(this);
                    jsonObject.put("auth", SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH));
                    jsonObject.put("type", 1 + "");
                    jsonObject.put("id", URL);

//                    jsonObject.put("id", pictureList.get(index).getId());
                    presenter.cancleFavorite(RetrofitFactory.getRequestBody(jsonObject.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtils.showShort(this, getResources().getString(R.string.check_the_network_please));
            }
        }
    }

    public void setAddFavorite1() {
        if ("0".equals(pictureList01.get(index).getIsSaveImg())) {//加收藏
            if (NetWorkUtil.isNetworkConnected(this)) {
                try {
                    JSONObject jsonObject = KeyUtil.getJson(this);
                    jsonObject.put("auth", SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH));
                    jsonObject.put("type", 1 + "");
                    jsonObject.put("url", URL);
                    presenter.addFavorite(RetrofitFactory.getRequestBody(jsonObject.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtils.showShort(this, getResources().getString(R.string.check_the_network_please));
            }

        } else {//取消收藏
            if (NetWorkUtil.isNetworkConnected(this)) {
                try {
                    JSONObject jsonObject = KeyUtil.getJson(this);
                    jsonObject.put("auth", SharedPreferencesUtil.getInstance().getString(Contents.STRING_AUTH));
                    jsonObject.put("type", 1 + "");
                    jsonObject.put("id", URL);
//                    jsonObject.put("id", pictureList.get(index).getId());
                    presenter.cancleFavorite(RetrofitFactory.getRequestBody(jsonObject.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtils.showShort(this, getResources().getString(R.string.check_the_network_please));
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
        String[] a = URL.split("/");
        final String fileName = a[a.length - 1];
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
            MediaStore.Images.Media.insertImage(DisPictureDetailActivity.this.getContentResolver(),//将图片插入系统图库
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//保存成功，通知系统更新相册
        Uri uri = Uri.fromFile(filena);
        intent.setData(uri);
        DisPictureDetailActivity.this.sendBroadcast(intent);
        DisPictureDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(DisPictureDetailActivity.this, getResources().getString(R.string.pic_isSave) + path + "/" + fileName);
            }
        });
    }
}
