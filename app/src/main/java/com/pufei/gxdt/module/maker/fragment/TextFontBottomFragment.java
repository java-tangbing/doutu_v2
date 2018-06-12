package com.pufei.gxdt.module.maker.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pufei.gxdt.R;
import com.pufei.gxdt.module.maker.adapter.SelectFontAdapter;
import com.pufei.gxdt.module.maker.common.MakerEventMsg;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择文字颜色
 */

public class TextFontBottomFragment extends BottomSheetDialogFragment implements View.OnClickListener,BaseQuickAdapter.OnItemClickListener{

    private LinearLayout llSelectColorBack;
    private RecyclerView rvFont;
    private SelectFontAdapter selectFontAdapter;
    private List<Integer> fontList;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setContentView(R.layout.fragment_add_font);
        llSelectColorBack = bottomSheetDialog.findViewById(R.id.ll_select_color_back);
        rvFont = bottomSheetDialog.findViewById(R.id.rv_font);
        rvFont.setLayoutManager(new GridLayoutManager(getActivity(),3));
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
        fontList = new ArrayList<>();
        fontList.add(R.mipmap.bt_made_operation_style_fangzhengjianzhi);
        fontList.add(R.mipmap.bt_made_operation_style_fangzhengkatong);
        fontList.add(R.mipmap.bt_made_operation_style_fangzhengyasong);
        fontList.add(R.mipmap.bt_made_operation_style_lantingdahei);
        fontList.add(R.mipmap.bt_made_operation_style_xindijianzhi);
        fontList.add(R.mipmap.bt_made_operation_style_xindixiaowanzixiaoxueban);
        selectFontAdapter = new SelectFontAdapter(fontList);
        selectFontAdapter.setOnItemClickListener(this);
        rvFont.setAdapter(selectFontAdapter);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        EventBus.getDefault().post(new MakerEventMsg(3,0,position));
    }
}
