package com.pufei.gxdt.module.maker.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.maker.adapter.SelectColorAdapter;
import com.pufei.gxdt.module.maker.common.MakerEventMsg;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择文字颜色
 */

public class SelectTextColorBottomFragment extends BottomSheetDialogFragment implements View.OnClickListener,BaseQuickAdapter.OnItemClickListener{

    private LinearLayout llSelectColorBack;
    private RecyclerView rvColorList;
    private SelectColorAdapter selectColorAdapter;
    private List<Integer> colorList;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setContentView(R.layout.fragment_add_text_select_color);
        llSelectColorBack = bottomSheetDialog.findViewById(R.id.ll_select_color_back);
        rvColorList = bottomSheetDialog.findViewById(R.id.rv_color);
        rvColorList.setLayoutManager(new GridLayoutManager(getActivity(),8));
        llSelectColorBack.setOnClickListener(this);
        initData();
        return bottomSheetDialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_select_color_back:
                dismiss();
                break;
        }
    }

    private void initData() {
        colorList = new ArrayList<>();
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color1));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color2));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color3));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color4));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color5));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color6));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color7));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color8));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color9));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color10));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color11));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color12));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color13));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color14));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color15));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color16));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color17));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color18));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color19));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color20));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color21));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color22));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color23));
        colorList.add(ContextCompat.getColor(getActivity(),R.color.select_color24));
        selectColorAdapter = new SelectColorAdapter(colorList);
        selectColorAdapter.setOnItemClickListener(this);
        rvColorList.setAdapter(selectColorAdapter);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        EventBus.getDefault().post(new MakerEventMsg(2,colorList.get(position)));
    }
}
