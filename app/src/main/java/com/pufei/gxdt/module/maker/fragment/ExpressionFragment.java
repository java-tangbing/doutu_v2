package com.pufei.gxdt.module.maker.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseFragment;
import com.pufei.gxdt.module.maker.adapter.StickerImageAdapter;
import com.pufei.gxdt.module.maker.common.MakerEventMsg;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * 表情
 */

public class ExpressionFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv_sticker)
    RecyclerView rvSticker;
    private StickerImageAdapter stickerImageAdapter;
    private List<String> imgList;

    public static ExpressionFragment newInstance() {
        return new ExpressionFragment();
    }


    @Override
    public void initView() {
        rvSticker.setLayoutManager(new GridLayoutManager(getActivity(), 4));
    }

    @Override
    public void getData() {
        String[] img = {"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3244299404,551733934&fm=27&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3801134091,755836747&fm=27&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1761354297,1466360338&fm=27&gp=0.jpg",
                "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3737670875,1541825744&fm=27&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3631692516,2485412998&fm=27&gp=0.jpg",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2907297107,4194518999&fm=27&gp=0.jpg",
                "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3551247198,4170942931&fm=27&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1553888207,726435672&fm=27&gp=0.jpg"};
        imgList = new ArrayList<>();
        Collections.addAll(imgList, img);
        Collections.addAll(imgList, img);
        Collections.addAll(imgList, img);
        Collections.addAll(imgList, img);
        stickerImageAdapter = new StickerImageAdapter(imgList);
        stickerImageAdapter.setOnItemClickListener(this);
        rvSticker.setAdapter(stickerImageAdapter);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_featured;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        EventBus.getDefault().post(new MakerEventMsg(1,imgList.get(position)));
    }
}
