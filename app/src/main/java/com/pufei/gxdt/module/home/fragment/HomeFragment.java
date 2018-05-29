package com.pufei.gxdt.module.home.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseMvpFragment;
import com.pufei.gxdt.module.home.activity.FaceTypeActivity;
import com.pufei.gxdt.module.home.activity.HomeImageActivity;
import com.pufei.gxdt.module.home.activity.HotImageActivity;
import com.pufei.gxdt.module.home.activity.JokeActivity;
import com.pufei.gxdt.module.home.activity.ThemeImageActivity;
import com.pufei.gxdt.module.home.adapter.HomeListAdapter;
import com.pufei.gxdt.module.home.model.HomeDetailBean;
import com.pufei.gxdt.module.home.model.HomeResultBean;
import com.pufei.gxdt.module.home.presenter.HomeListPresenter;
import com.pufei.gxdt.module.home.view.HomeListView;
import com.pufei.gxdt.utils.KeyUtil;
import com.pufei.gxdt.utils.NetWorkUtil;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseMvpFragment<HomeListPresenter> implements HomeListView {
    @BindView(R.id.request_failed)
    LinearLayout request_failed;
    @BindView(R.id.srf_home_list)
    SmartRefreshLayout srf_home_lisyt;
    @BindView(R.id.rl_home_list)
    XRecyclerView rl_home_list;
    @BindView(R.id.iv_adver)
    ImageView iv_adver;
    private HomeListAdapter adapter;
    private int page = 1;
    private List<HomeResultBean.ResultBean> homeList = new ArrayList<>();
    @Override
    public void initView() {
        iv_adver.setVisibility(View.GONE);
        adapter = new HomeListAdapter(getActivity(),homeList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());//布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rl_home_list.setLayoutManager(layoutManager);
        rl_home_list.setAdapter(adapter);
        adapter.setOnItemClickListener(new HomeListAdapter.MyItemClickListener() {
            @Override
            public void setOnItemClickListener(View itemview, View view, int postion) {
                if(homeList.get(postion).getImages()==null){
                    Intent intent = new Intent(getActivity(), HomeImageActivity.class);
                    intent.putExtra("category_id",homeList.get(postion).getImgs().get(0).getCategory_id());
                    startActivity(intent);
                }

            }

            @Override
            public void OnLike(int position) {

            }

            @Override
            public void OnBtDelete(int position) {

            }
        });
    }

    @Override
    public void getData() {
        if (NetWorkUtil.isNetworkConnected(getActivity())) {
            requestHomeList();
        }else{
            request_failed.setVisibility(View.VISIBLE);
        }

    }
    private void requestHomeList(){
        String  net = null;
        if(NetWorkUtil.isWifiConnected(getActivity())){
            net = "1";
        }else if(NetWorkUtil.isMobileConnected(getActivity())){
            net = "4";
        }else{
            net = "0";
        }
        try {
            JSONObject jsonObject = KeyUtil.getJson(getActivity());
            jsonObject.put("net", net );
            jsonObject.put("page", page );
            presenter.getHomeList(RetrofitFactory.getRequestBody(jsonObject.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }


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

    @Override
    public void setPresenter(HomeListPresenter presenter) {
        if (presenter == null) {
            this.presenter = new HomeListPresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void resultHomeList(HomeResultBean bean) {
        if(bean!=null){
           homeList.addAll(bean.getResult());
           adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void resultHomeDetailList(HomeDetailBean bean) {

    }
}
