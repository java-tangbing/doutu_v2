package com.pufei.gxdt.module.maker.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseFragment;
import com.pufei.gxdt.base.BaseMvpFragment;
import com.pufei.gxdt.module.maker.adapter.StickerImageAdapter;
import com.pufei.gxdt.module.maker.bean.MaterialBean;
import com.pufei.gxdt.module.maker.bean.RecommendTextBean;
import com.pufei.gxdt.module.maker.common.MakerEventMsg;
import com.pufei.gxdt.module.maker.presenter.EditImagePresenter;
import com.pufei.gxdt.module.maker.view.EditImageView;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SystemInfoUtils;
import com.pufei.gxdt.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 形象
 */

public class FigureFragment extends BaseMvpFragment<EditImagePresenter> implements EditImageView, BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rv_sticker)
    RecyclerView rvSticker;
    private StickerImageAdapter stickerImageAdapter;
    private List<MaterialBean.ResultBean> imgList;
    private int page = 1;

    public static FigureFragment newInstance() {
        return new FigureFragment();
    }


    @Override
    public void initView() {
        rvSticker.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        imgList = new ArrayList<>();
        stickerImageAdapter = new StickerImageAdapter(imgList);
        stickerImageAdapter.setOnItemClickListener(this);
        stickerImageAdapter.setOnLoadMoreListener(this,rvSticker);
        rvSticker.setAdapter(stickerImageAdapter);
    }

    @Override
    public void getData() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("deviceid", SystemInfoUtils.deviced(getActivity()));
        map1.put("version", SystemInfoUtils.versionName(getActivity()));
        map1.put("timestamp", (System.currentTimeMillis() / 1000) + "");
        map1.put("os", "1");
        map1.put("sign", "sign");
        map1.put("key", "key");
        map1.put("page", page+"");
        map1.put("type", "2");
        //presenter.getMaterial(RetrofitFactory.getRequestBody(new Gson().toJson(map1)), 2);

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_featured;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        EventBus.getDefault().post(new MakerEventMsg(1, imgList.get(position).getImage()));
    }

    @Override
    public void upLoadImageResult(ModifyResultBean response) {

    }

    @Override
    public void downloadImageResult(String base64, int type) {

    }

    @Override
    public void downloadGifResult(String path) {

    }

    @Override
    public void recommentTextResult(RecommendTextBean response) {

    }

    @Override
    public void materialResult(MaterialBean response, int type) {
        if (response.getCode() == 0) {
            if(response.getResult().size() > 0) {
                page++;
                stickerImageAdapter.addData(response.getResult());
                stickerImageAdapter.loadMoreComplete();
            }else {
                stickerImageAdapter.loadMoreEnd();
            }
        } else {
            ToastUtils.showShort(getActivity(), response.getMsg());
        }
    }

    @Override
    public void requestErrResult(String msg) {

    }

    @Override
    public void requestSuccess(String msg) {

    }

    @Override
    public void setPresenter(EditImagePresenter presenter) {
        if (presenter == null) {
            this.presenter = new EditImagePresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("deviceid", SystemInfoUtils.deviced(getActivity()));
        map1.put("version", SystemInfoUtils.versionName(getActivity()));
        map1.put("timestamp", (System.currentTimeMillis() / 1000) + "");
        map1.put("os", "1");
        map1.put("sign", "sign");
        map1.put("key", "key");
        map1.put("page", page+"");
        map1.put("type", "2");
        presenter.getMaterial(RetrofitFactory.getRequestBody(new Gson().toJson(map1)), 2);
    }
}
