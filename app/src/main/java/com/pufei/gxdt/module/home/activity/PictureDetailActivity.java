package com.pufei.gxdt.module.home.activity;

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
import com.pufei.gxdt.module.home.model.PictureResultBean;
import com.pufei.gxdt.widgets.GlideApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**表情操作
 * Created by tb on 2018/5/23.
 */

public class PictureDetailActivity extends BaseMvpActivity{
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
    private List<PictureResultBean.ResultBean> picture_update_list = new ArrayList<>();
    private OtherPictureAdapter adapter;
    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rl_picture.setLayoutManager(linearLayoutManager);
        adapter = new OtherPictureAdapter(PictureDetailActivity.this,picture_update_list);
        rl_picture.setAdapter(adapter);
        adapter.setOnItemClickListener(new OtherPictureAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                GlideApp.with(PictureDetailActivity.this).load(picture_update_list.get(postion).getUrl()).placeholder(R.mipmap.loading).into(iv_picture);
                picture_update_list.clear();
                picture_update_list.addAll(pictureList);
                adapter.notifyDataSetChanged();
                setUserData(postion);
            }
        });

    }

    @Override
    public void getData() {
        Bundle bundle = this.getIntent().getExtras();
        index = bundle.getInt("picture_index");
        pictureList = (List<PictureResultBean.ResultBean>) bundle.getSerializable("picture_list");
        if(pictureList!=null){
            picture_update_list.addAll(pictureList);
            picture_update_list.remove(index);
            adapter.notifyDataSetChanged();
            joinPicture();
        }
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

    @Override
    public int getLayout() {
        return R.layout.activity_look;
    }
}
