package com.pufei.gxdt.module.maker.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseFragment;
import com.pufei.gxdt.base.BaseMvpFragment;
import com.pufei.gxdt.module.maker.activity.EditImageActivity;
import com.pufei.gxdt.module.maker.bean.MaterialBean;
import com.pufei.gxdt.module.maker.bean.RecommendTextBean;
import com.pufei.gxdt.module.maker.presenter.EditImagePresenter;
import com.pufei.gxdt.module.maker.view.EditImageView;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SoftKeyboardStateHelper;
import com.pufei.gxdt.utils.SystemInfoUtils;
import com.pufei.gxdt.utils.ToastUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文字
 */

public class ImageTextEditFragment extends BaseMvpFragment<EditImagePresenter> implements EditImageView,EditTextBottomFragment.InputTextListener {

    @BindView(R.id.ll_input)
    LinearLayout llInput;
    @BindView(R.id.ll_select_color)
    LinearLayout llSelectColor;
    @BindView(R.id.ll_select_font)
    LinearLayout llSelectFont;
    @BindView(R.id.ll_parent)
    LinearLayout llParent;

    private EditTextBottomFragment editTextBottomFragment;
    private GetInputTextCallback callback;
    private EditImageActivity activity;
    private RecommendTextBean bean;


    public static ImageTextEditFragment newInstance() {
        return new ImageTextEditFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (EditImageActivity)context;
        if(activity  != null) {
            callback = (GetInputTextCallback)activity;
        }
    }

    @Override
    public void initView() {
        editTextBottomFragment = new EditTextBottomFragment();
        editTextBottomFragment.setInputTextListener(this);

    }

    @Override
    public void getData() {
        Map<String,String> map = new HashMap<>();
        map.put("deviceid", SystemInfoUtils.deviced(getActivity()));
        map.put("version", SystemInfoUtils.versionName(getActivity()));
        map.put("timestamp", (System.currentTimeMillis() / 1000) + "");
        map.put("os", "1");
        map.put("sign","sign");
        map.put("key","key");
        presenter.getRecommentText(RetrofitFactory.getRequestBody(new Gson().toJson(map)));
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_image_text_edit;
    }

    @OnClick({R.id.ll_input, R.id.ll_select_color, R.id.ll_select_font})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_input:
                int type = activity.getTypes();
                if(type == 1) {
                    editTextBottomFragment.show(getChildFragmentManager(),"1");
                }else {
                    editTextBottomFragment.show(getChildFragmentManager(),"0");
                }
                break;
            case R.id.ll_select_color:
                int type1 = activity.getTypes();
                if(type1 == 1) {
                    SelectTextColorBottomFragment selectTextColor = new SelectTextColorBottomFragment();
                    selectTextColor.show(getChildFragmentManager(),"");
                }
                break;
            case R.id.ll_select_font:
                int type2 = activity.getTypes();
                if(type2 == 1) {
                    TextFontBottomFragment fontBottomFragment = new TextFontBottomFragment();
                    fontBottomFragment.show(getChildFragmentManager(),"");
                }
                break;
        }
    }

    @Override
    public void inputText(int type,String text, int color) {
        callback.getInputTextCallback(type,text,color);
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
        this.bean = response;
    }

    @Override
    public void materialResult(MaterialBean response, int type) {

    }

    @Override
    public void requestErrResult(String msg) {

    }

    @Override
    public void requestSuccess(String msg) {

    }


    @Override
    public void setPresenter(EditImagePresenter presenter) {
        if(presenter == null) {
            this.presenter = new EditImagePresenter();
            this.presenter.attachView(this);
        }
    }

    public interface GetInputTextCallback {
        void getInputTextCallback(int type,String text, int color);
    }

    public RecommendTextBean getBean() {
        return bean;
    }
}
