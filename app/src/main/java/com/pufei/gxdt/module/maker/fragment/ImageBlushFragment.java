package com.pufei.gxdt.module.maker.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseFragment;
import com.pufei.gxdt.module.maker.adapter.SelectColorAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 画笔
 */

public class ImageBlushFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, SeekBar.OnSeekBarChangeListener {


    @BindView(R.id.seekbar_brush_size)
    AppCompatSeekBar seekbarBrushSize;
    @BindView(R.id.rv_color)
    RecyclerView rvColor;
    private SelectColorAdapter selectColorAdapter;
    private List<Integer> colorList;
    private SetBlushPropertiesListener blushListener;

    public static ImageBlushFragment newInstance() {
        return new ImageBlushFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        if (activity != null) {
            blushListener = (SetBlushPropertiesListener) activity;
        }
    }

    @Override
    public void initView() {
        rvColor.setLayoutManager(new GridLayoutManager(getActivity(), 8));
        seekbarBrushSize.setOnSeekBarChangeListener(this);
        //获取seerbar层次drawable对象
        LayerDrawable layerDrawable = (LayerDrawable) seekbarBrushSize.getProgressDrawable();
        int layers = layerDrawable.getNumberOfLayers();
        for (int i = 0; i < layers; i++) {
            if(layerDrawable.getId(i) == android.R.id.background) {
                Drawable drawable = layerDrawable.getDrawable(i);
                drawable.setColorFilter(Color.parseColor("#DEDEDE"), PorterDuff.Mode.SRC);
            }else {
                Drawable drawable = layerDrawable.getDrawable(i);
                drawable.setColorFilter(Color.parseColor("#6A6A6A"), PorterDuff.Mode.SRC);
            }

        }
        seekbarBrushSize.getThumb().setColorFilter(Color.parseColor("#6A6A6A"), PorterDuff.Mode.SRC_ATOP);

    }

    @Override
    public void getData() {
        colorList = new ArrayList<>();
        colorList.add(ContextCompat.getColor(getActivity(), R.color.select_color1));
        colorList.add(ContextCompat.getColor(getActivity(), R.color.select_color3));
        colorList.add(ContextCompat.getColor(getActivity(), R.color.select_color6));
        colorList.add(ContextCompat.getColor(getActivity(), R.color.select_color7));
        colorList.add(ContextCompat.getColor(getActivity(), R.color.select_color8));
        colorList.add(ContextCompat.getColor(getActivity(), R.color.select_color9));
        colorList.add(ContextCompat.getColor(getActivity(), R.color.select_color10));
        colorList.add(ContextCompat.getColor(getActivity(), R.color.select_color11));
        colorList.add(ContextCompat.getColor(getActivity(), R.color.select_color12));
        colorList.add(ContextCompat.getColor(getActivity(), R.color.select_color13));
        colorList.add(ContextCompat.getColor(getActivity(), R.color.select_color15));
        colorList.add(ContextCompat.getColor(getActivity(), R.color.select_color16));
        colorList.add(ContextCompat.getColor(getActivity(), R.color.select_color18));
        colorList.add(ContextCompat.getColor(getActivity(), R.color.select_color19));
        colorList.add(ContextCompat.getColor(getActivity(), R.color.select_color24));
        colorList.add(ContextCompat.getColor(getActivity(), R.color.select_earse));

        selectColorAdapter = new SelectColorAdapter(colorList);
        selectColorAdapter.setOnItemClickListener(this);
        rvColor.setAdapter(selectColorAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_brush;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if(position != colorList.size() - 1) {
            blushListener.setBlushColor(colorList.get(position));
        }else {
            blushListener.setBlushEraser();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        blushListener.setBlushSize(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @OnClick(R.id.ll_blush_mode)
    public void onViewClicked() {
        blushListener.setBlushMode(true);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if(hidden) {
            blushListener.setBlushMode(false);
        }
    }

    public interface SetBlushPropertiesListener {
        void setBlushMode(boolean isOpenBlush);

        void setBlushColor(int color);

        void setBlushSize(int size);
        void setBlushEraser();
    }
}
