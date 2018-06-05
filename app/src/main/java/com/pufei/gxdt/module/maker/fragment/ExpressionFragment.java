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
 * 表情
 */

public class ExpressionFragment extends BaseMvpFragment<EditImagePresenter> implements EditImageView, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv_sticker)
    RecyclerView rvSticker;
    private StickerImageAdapter stickerImageAdapter;
    private List<MaterialBean.ResultBean> imgList;

    public static ExpressionFragment newInstance() {
        return new ExpressionFragment();
    }


    @Override
    public void initView() {
        rvSticker.setLayoutManager(new GridLayoutManager(getActivity(), 4));
    }

    @Override
    public void getData() {
        Map<String,String> map2 = new HashMap<>();
        map2.put("deviceid", SystemInfoUtils.deviced(getActivity()));
        map2.put("version", SystemInfoUtils.versionName(getActivity()));
        map2.put("timestamp", (System.currentTimeMillis() / 1000) + "");
        map2.put("os", "1");
        map2.put("sign","sign");
        map2.put("key","key");
        map2.put("page","1");
        map2.put("type","3");
        presenter.getMaterial(RetrofitFactory.getRequestBody(new Gson().toJson(map2)),3);
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
            imgList = new ArrayList<>();
            imgList.addAll(response.getResult());
            stickerImageAdapter = new StickerImageAdapter(imgList);
            stickerImageAdapter.setOnItemClickListener(this);
            rvSticker.setAdapter(stickerImageAdapter);

        } else {
            ToastUtils.showShort(getActivity(), response.getMsg());
        }
    }

    @Override
    public void requestErrResult(String msg) {

    }

    @Override
    public void setPresenter(EditImagePresenter presenter) {
        if (presenter == null) {
            this.presenter = new EditImagePresenter();
            this.presenter.attachView(this);
        }
    }
}
