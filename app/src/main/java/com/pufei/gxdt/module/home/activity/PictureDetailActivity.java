package com.pufei.gxdt.module.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.base.BasePresenter;
import com.pufei.gxdt.module.home.adapter.OtherPictureAdapter;
import com.pufei.gxdt.module.home.model.PictureDetailBean;
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.module.home.presenter.ImageTypePresenter;
import com.pufei.gxdt.module.home.view.ImageTypeView;
import com.pufei.gxdt.module.maker.activity.EditImageActivity;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.widgets.GlideApp;

import org.json.JSONException;
import org.json.JSONObject;

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
                setUserData(index);
            }
        });

    }

    @Override
    public void getData() {
        joinPicture();
        URL = pictureList.get(index).getUrl();
        setUserData(index);
    }
    private void setUserData(int index){
        tv_eyes.setText(pictureList.get(index).getView());
        tv_hot.setText(pictureList.get(index).getHot());
       // tv_change_img.setText(pictureList.get(index).getOrgintable());
    }
    private void joinPicture(){
        GlideApp.with(this).load(pictureList.get(index).getUrl()).placeholder(R.mipmap.loading).into(iv_picture);
    }
    @OnClick({R.id.look_edit_image_iv,R.id.tv_share_qq,R.id.tv_share_wx})
    public void viewClick(View view){
        switch (view.getId()){
            case R.id.look_edit_image_iv:
                getImageDetail();
                break;
            case R.id.tv_share_qq:
                break;
            case R.id.tv_share_wx:
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
            Intent intent = new Intent(this, EditImageActivity.class);
            Bundle bundle =  new Bundle();
            bundle.putSerializable("picture_bean",bean.getResult());
            intent.putExtras(bundle);
            intent.putExtra(EditImageActivity.EDIT_TYPE,EditImageActivity.EDIT_TYPE_EDIT);
            startActivity(intent);
        }

    }
}
