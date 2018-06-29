package com.pufei.gxdt.module.maker.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.maker.adapter.HotTextAdapter;
import com.pufei.gxdt.module.maker.bean.RecommendTextBean;
import com.pufei.gxdt.module.maker.presenter.EditImagePresenter;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 添加文字
 */
public class EditTextBottomFragment extends BottomSheetDialogFragment implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    private EditText etInput;
    private TextView tvConfirm;
    private TextView tvRecommend;
    private LinearLayout llRecommend;
    private LinearLayout llBottom;
    private RecyclerView rvHotText;
    private InputTextListener inputTextListener;
    private int type = 0;
    private List<String> hotTextList;
    private HotTextAdapter hotTextAdapter;
    private InputMethodManager inputManager;
    private EditImagePresenter imagePresenter;
    private BottomSheetBehavior mBottomSheetBehavior;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        final View bottomSheetDialog = View.inflate(getContext(), R.layout.fragment_addtext_pop, null);
        dialog.setContentView(bottomSheetDialog);
        llBottom = bottomSheetDialog.findViewById(R.id.ll_bottom);
        etInput = bottomSheetDialog.findViewById(R.id.et_input);
        tvConfirm = bottomSheetDialog.findViewById(R.id.tv_confirm);
        tvRecommend = bottomSheetDialog.findViewById(R.id.tv_recommend);
        llRecommend = bottomSheetDialog.findViewById(R.id.ll_recommend);
        rvHotText = bottomSheetDialog.findViewById(R.id.rv_hot_text);
        etInput.setSelection(etInput.getText().length());
        tvConfirm.setOnClickListener(this);
        llRecommend.setOnClickListener(this);
        rvHotText.setLayoutManager(new LinearLayoutManager(getActivity()));
        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) bottomSheetDialog.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            mBottomSheetBehavior = (BottomSheetBehavior) behavior;
            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });

            bottomSheetDialog.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    bottomSheetDialog.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int height = bottomSheetDialog.getMeasuredHeight();
                    if(height < 1200) {
                        mBottomSheetBehavior.setPeekHeight(height);
                    }else {
                        mBottomSheetBehavior.setPeekHeight(height - 200);
                    }
//                    Log.e("height",height +"  ");
                }
            });
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                inputManager.showSoftInput(etInput, 0);
            }
        }, 300);

        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (!isOpen) {
                            if (rvHotText.getVisibility() != View.VISIBLE) {
                                if(!isHidden()) {
                                    dismiss();

                                }
                            }
                        }
                    }
                });

        initData();

        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (!etInput.getText().toString().isEmpty()) {
                    inputTextListener.inputText(type, etInput.getText().toString(), ContextCompat.getColor(getActivity(), R.color.select_color1));
                    etInput.setText("");
                }
                dismiss();
                return false;
            }
        });
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        type = Integer.parseInt(tag);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                if (!etInput.getText().toString().isEmpty()) {
                    inputTextListener.inputText(type, etInput.getText().toString(), ContextCompat.getColor(getActivity(), R.color.select_color17));
                    etInput.setText("");
                }
                dismiss();
                break;
            case R.id.ll_recommend:
                if (rvHotText.getVisibility() == View.VISIBLE) {
                    tvRecommend.setTextColor(ContextCompat.getColor(getActivity(), R.color.hint_color));
                    tvRecommend.setBackgroundResource(R.drawable.circle_stroke_grey);
                    rvHotText.setVisibility(View.INVISIBLE);
                    inputManager.showSoftInput(etInput, 0);
                } else {
                    tvRecommend.setTextColor(ContextCompat.getColor(getActivity(), R.color.recommed_color));
                    tvRecommend.setBackgroundResource(R.drawable.circle_stroke_yellow);
                    rvHotText.setVisibility(View.VISIBLE);
                    inputManager.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
                }
                break;
        }
    }

    private void initData() {
        ImageTextEditFragment fragment = (ImageTextEditFragment) getParentFragment();
        if (fragment != null) {
            if (fragment.getBean() != null) {
                hotTextList = fragment.getBean().getResult();
                hotTextAdapter = new HotTextAdapter(hotTextList);
                rvHotText.setAdapter(hotTextAdapter);
                hotTextAdapter.setOnItemClickListener(this);
            }

        }
    }

    public void setInputTextListener(InputTextListener listener) {
        this.inputTextListener = listener;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        etInput.setText(hotTextList.get(position));
    }


    public interface InputTextListener {
        void inputText(int type, String text, int color);
    }


}
