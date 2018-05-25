package com.pufei.gxdt.module.maker.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.maker.adapter.HotTextAdapter;
import com.pufei.gxdt.module.maker.common.EventMsg;
import com.pufei.gxdt.utils.SoftKeyboardStateHelper;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class EditTextBottomFragment extends BottomSheetDialogFragment implements View.OnClickListener,BaseQuickAdapter.OnItemClickListener {

    private EditText etInput;
    private TextView tvConfirm;
    private TextView tvRecommend;
    private LinearLayout llBottom;
    private RecyclerView rvHotText;
    private InputTextListener inputTextListener;
    private int type = 0;
    private List<String> hotTextList;
    private HotTextAdapter hotTextAdapter;
    private InputMethodManager inputManager;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setContentView(R.layout.fragment_addtext_pop);
        llBottom = bottomSheetDialog.findViewById(R.id.ll_bottom);
        etInput = bottomSheetDialog.findViewById(R.id.et_input);
        tvConfirm = bottomSheetDialog.findViewById(R.id.tv_confirm);
        tvRecommend = bottomSheetDialog.findViewById(R.id.tv_recommend);
        rvHotText = bottomSheetDialog.findViewById(R.id.rv_hot_text);
        tvConfirm.setOnClickListener(this);
        tvRecommend.setOnClickListener(this);
        rvHotText.setLayoutManager(new LinearLayoutManager(getActivity()));
        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                inputManager.showSoftInput(etInput, 0);
            }
        },300);

        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(!isOpen) {
                            if(rvHotText.getVisibility() != View.VISIBLE) {
                                dismiss();
                            }
                        }
                    }
                });

        initData();
        try {
            Field mBehaviorField = bottomSheetDialog.getClass().getDeclaredField("mBehavior");
            mBehaviorField.setAccessible(true);
            final BottomSheetBehavior behavior = (BottomSheetBehavior) mBehaviorField.get(bottomSheetDialog);
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }else if(newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return bottomSheetDialog;
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
                if(!etInput.getText().toString().isEmpty()){
                    inputTextListener.inputText(type,etInput.getText().toString(), ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    etInput.setText("");
                }
                dismiss();
                break;
            case R.id.tv_recommend:
                if(rvHotText.getVisibility() == View.VISIBLE) {
                    tvRecommend.setTextColor(ContextCompat.getColor(getActivity(),R.color.hint_color));
                    tvRecommend.setBackgroundResource(R.drawable.circle_stroke_grey);
                    rvHotText.setVisibility(View.INVISIBLE);
                    inputManager.showSoftInput(etInput, 0);
                }else {
                    tvRecommend.setTextColor(ContextCompat.getColor(getActivity(),R.color.recommed_color));
                    tvRecommend.setBackgroundResource(R.drawable.circle_stroke_yellow);
                    rvHotText.setVisibility(View.VISIBLE);
                    inputManager.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
                }
                break;
        }
    }

    private void initData() {
        hotTextList = new ArrayList<String>();
        for (int i = 0; i < 15; i++) {
            hotTextList.add("这是第" + i + "个热词");
        }
        hotTextAdapter = new HotTextAdapter(hotTextList);
        rvHotText.setAdapter(hotTextAdapter);
        hotTextAdapter.setOnItemClickListener(this);
    }

    public void setInputTextListener(InputTextListener listener) {
        this.inputTextListener = listener;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        etInput.setText(hotTextList.get(position));
    }

    public interface InputTextListener {
        void inputText(int type,String text, int color);
    }
}
