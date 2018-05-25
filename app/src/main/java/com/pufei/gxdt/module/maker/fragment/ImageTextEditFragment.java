package com.pufei.gxdt.module.maker.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseFragment;
import com.pufei.gxdt.module.maker.activity.EditImageActivity;
import com.pufei.gxdt.utils.SoftKeyboardStateHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文字
 */

public class ImageTextEditFragment extends BaseFragment implements EditTextBottomFragment.InputTextListener {

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
                    editTextBottomFragment.show(getActivity().getSupportFragmentManager(),"1");
                }else {
                    editTextBottomFragment.show(getActivity().getSupportFragmentManager(),"0");
                }
                break;
            case R.id.ll_select_color:
                break;
            case R.id.ll_select_font:
                break;
        }
    }

    @Override
    public void inputText(int type,String text, int color) {
        callback.getInputTextCallback(type,text,color);
    }

    public interface GetInputTextCallback {
        void getInputTextCallback(int type,String text, int color);
    }

}
