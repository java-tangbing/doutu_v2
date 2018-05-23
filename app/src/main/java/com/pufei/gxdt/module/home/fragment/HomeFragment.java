package com.pufei.gxdt.module.home.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseFragment;
import com.pufei.gxdt.module.home.activity.FaceTypeActivity;
import com.pufei.gxdt.module.home.activity.JokeActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.tv_type_face)
    TextView tv_type_face;
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
    @OnClick({R.id.tv_type_face,R.id.tv_joke})
    public  void onViewClicked(View view){
        switch (view.getId()){
            case R.id.tv_type_face:
                startActivity(new Intent(getActivity(), FaceTypeActivity.class));
                break;
            case R.id.tv_joke:
                startActivity(new Intent(getActivity(), JokeActivity.class));
                break;
        }

    }
}
