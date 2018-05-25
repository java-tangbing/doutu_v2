package com.pufei.gxdt.module.home.fragment;

import android.content.Intent;
import android.media.FaceDetector;
import android.view.View;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseFragment;
import com.pufei.gxdt.module.home.activity.FaceTypeActivity;
import com.pufei.gxdt.module.home.activity.HotImageActivity;
import com.pufei.gxdt.module.home.activity.JokeActivity;
import com.pufei.gxdt.module.home.activity.ThemeImageActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment {
    @Override
    public void initView() {

    }

    @Override
    public void getData() {

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_home;
    }
    @OnClick({R.id.tv_hot_face,R.id.tv_joke,R.id.tv_theme_face,R.id.iv_user_shoucang})
    public  void onViewClicked(View view){
        switch (view.getId()){
            case R.id.tv_hot_face:
                startActivity(new Intent(getActivity(), HotImageActivity.class));
                break;
            case R.id.tv_joke:
                startActivity(new Intent(getActivity(), JokeActivity.class));
                break;
            case R.id.tv_theme_face:
                startActivity(new Intent(getActivity(), ThemeImageActivity.class));
                     break;
//            case R.id.iv_user_shoucang:
//                startActivity(new Intent(getActivity(), ThemeImageActivity.class));
//                break;

        }
    }
    @OnClick({R.id.ll_doutu,R.id.ll_chongwu,R.id.ll_gaoxiao,R.id.ll_meishi,R.id.ll_mengwa,R.id.ll_mingxing,R.id.ll_qita,R.id.ll_yingshi})
    public  void typeFaceActivity(){
        startActivity(new Intent(getActivity(), FaceTypeActivity.class));
    }
}
