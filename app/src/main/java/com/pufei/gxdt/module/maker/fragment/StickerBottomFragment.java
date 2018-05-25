package com.pufei.gxdt.module.maker.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pufei.gxdt.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class StickerBottomFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private TextView tvCancle;
    private TextView tvFeatured;
    private TextView tvImage;
    private TextView tvExpression;
    private TextView tvFinished;
    private FrameLayout stickerContent;

    private List<Fragment> fragmentList;
    private int previousIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_template, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCancle = view.findViewById(R.id.tv_cancel);
        tvFeatured = view.findViewById(R.id.tv_featured);
        tvImage = view.findViewById(R.id.tv_image);
        tvExpression = view.findViewById(R.id.tv_expression);
        tvFinished = view.findViewById(R.id.tv_finish);
        stickerContent = view.findViewById(R.id.content_sticker);
        tvCancle.setOnClickListener(this);
        tvExpression.setOnClickListener(this);
        tvImage.setOnClickListener(this);
        tvFeatured.setOnClickListener(this);
        tvFinished.setOnClickListener(this);
        addFragment();
        defaultSelect();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_finish:
                dismiss();
                break;
            case R.id.tv_featured:
                defaultSelect();
                break;
            case R.id.tv_image:
                setSelectedItemState(tvImage);
                setUnSelectedItemState(tvFeatured,tvExpression);
                showFragment(1,previousIndex);
                previousIndex = 1;
                break;
            case R.id.tv_expression:
                setSelectedItemState(tvExpression);
                setUnSelectedItemState(tvFeatured,tvImage);
                showFragment(2,previousIndex);
                previousIndex = 2;
                break;

        }
    }

    private void showFragment(int showIndex, int hideIndex) {//fragment管理
        Fragment showFragment = fragmentList.get(showIndex);
        Fragment hideFragment = fragmentList.get(hideIndex);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (!showFragment.isAdded()) {
            ft.add(R.id.content_sticker, showFragment);
        }
        if (showIndex == hideIndex) {
            ft.show(showFragment).commit();
        } else {
            ft.show(showFragment).hide(hideFragment).commit();
        }
    }

    private void setSelectedItemState(TextView tv) {
        tv.setTextColor(ContextCompat.getColor(getActivity(),R.color.black));
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    private void setUnSelectedItemState(TextView tv1, TextView tv2) {
        tv1.setTextColor(ContextCompat.getColor(getActivity(),R.color.text_default_color));
        tv1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tv2.setTextColor(ContextCompat.getColor(getActivity(),R.color.text_default_color));
        tv2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    private void defaultSelect() {
        setSelectedItemState(tvFeatured);
        setUnSelectedItemState(tvImage,tvExpression);
        showFragment(0,previousIndex);
        previousIndex = 0;
    }

    private void addFragment() {
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(FeaturedFragment.newInstance());
        fragmentList.add(FigureFragment.newInstance());
        fragmentList.add(ExpressionFragment.newInstance());
    }
}
