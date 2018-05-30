package com.pufei.gxdt.module.user.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.db.DraftInfo;
import com.pufei.gxdt.module.maker.activity.EditImageActivity;
import com.pufei.gxdt.module.user.adapter.DraftAdapter;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.ToastUtils;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class DraftActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_draft)
    RecyclerView rvDraft;
    private DraftAdapter draftAdapter;
    private List<DraftInfo> datas;

    @Override
    public void initView() {
        llTitleLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("我的草稿箱子");
        rvDraft.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void getData() {
        datas = new Select().from(DraftInfo.class).queryList();
        for (int i = 0; i < datas.size(); i++) {
            DraftInfo info = datas.get(i);
            if(!info.imagePath.contains("http:") || !info.imagePath.contains("https:")) {
                File file = new File(info.imagePath);
                if(!file.exists()) {
                    datas.remove(info);
                }
            }
        }
        draftAdapter = new DraftAdapter(datas);
        draftAdapter.setOnItemChildClickListener(this);
        rvDraft.setAdapter(draftAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_draft;
    }

    @OnClick(R.id.ll_title_left)
    public void onViewClicked() {
        AppManager.getAppManager().finishActivity();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_edit:

                Intent intent = new Intent(this, EditImageActivity.class);
                intent.putExtra(EditImageActivity.IMAGE_ID,datas.get(position).imageId);
                intent.putExtra(EditImageActivity.IMAGE_PATH,datas.get(position).imagePath);
                startActivity(intent);
                break;
            case R.id.tv_publish:
                ToastUtils.showShort(this,"发布");
                break;
        }
    }
}
