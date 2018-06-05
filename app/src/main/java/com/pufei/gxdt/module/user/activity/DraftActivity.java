package com.pufei.gxdt.module.user.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.db.DraftInfo;
import com.pufei.gxdt.db.DraftInfo_Table;
import com.pufei.gxdt.db.ImageDraft;
import com.pufei.gxdt.db.ImageDraft_Table;
import com.pufei.gxdt.db.TextDraft;
import com.pufei.gxdt.db.TextDraft_Table;
import com.pufei.gxdt.module.maker.activity.EditImageActivity;
import com.pufei.gxdt.module.maker.activity.MakerFinishActivity;
import com.pufei.gxdt.module.maker.bean.MaterialBean;
import com.pufei.gxdt.module.maker.bean.RecommendTextBean;
import com.pufei.gxdt.module.maker.common.MakerEventMsg;
import com.pufei.gxdt.module.maker.presenter.EditImagePresenter;
import com.pufei.gxdt.module.maker.view.EditImageView;
import com.pufei.gxdt.module.user.adapter.DraftAdapter;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.ImageUtils;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.utils.UploadImageUtil;
import com.pufei.gxdt.widgets.GlideApp;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.pufei.gxdt.utils.ImageUtils.getBytes;


public class DraftActivity extends BaseMvpActivity<EditImagePresenter> implements EditImageView, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_draft)
    RecyclerView rvDraft;
    private DraftAdapter draftAdapter;
    private List<DraftInfo> datas;
    private int position;
    private String imageBase64;
    private String bgImageBase64;
    private ProgressDialog mProgressDialog;

    @Override
    public void initView() {
        llTitleLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("我的草稿箱");
        rvDraft.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(MakerEventMsg msg) {
        if (msg.getType() == 5) {
            DraftInfo info = datas.get(position);
            info.imagePath = msg.getUrl();
            draftAdapter.notifyItemChanged(position);
        }else if (msg.getType() == 12) {
            presenter.upLoadImage(RetrofitFactory.getRequestBody(msg.getUrl()));
        }
    }

    @Override
    public void getData() {
        datas = new Select().from(DraftInfo.class).where(DraftInfo_Table.isDraft.is(true)).queryList();
        for (int i = 0; i < datas.size(); i++) {
            DraftInfo info = datas.get(i);
            if (!info.make_url.contains("http:") || !info.make_url.contains("https:")) {
                File file = new File(info.make_url);
                if (!file.exists()) {
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
                this.position = position;
                Intent intent = new Intent(this, EditImageActivity.class);
                intent.putExtra(EditImageActivity.IMAGE_ID, datas.get(position).imageId);
                intent.putExtra(EditImageActivity.IMAGE_PATH, datas.get(position).imagePath);
                intent.putExtra(EditImageActivity.EDIT_TYPE, EditImageActivity.EDIT_TYPE_DRAFT);
                startActivity(intent);
                break;
            case R.id.tv_publish:
//                if(imageBase64 != null && bgImageBase64 != null) {
                    showLoading("上传中...");
                    final List<ImageDraft> imageDrafts = new Select().from(ImageDraft.class).where(ImageDraft_Table.imageId.is(datas.get(position).imageId)).and(ImageDraft_Table.isDraft.is(true)).queryList();
                    final List<TextDraft> textDrafts = new Select().from(TextDraft.class).where(TextDraft_Table.imageId.is(datas.get(position).imageId)).and(TextDraft_Table.isDraft.is(true)).queryList();
                    getBase64(datas.get(position).imagePath,datas.get(position).make_url);
                    UploadImageUtil.uploadImage(this, datas.get(position), datas.get(position).imagePath, imageBase64, bgImageBase64, imageDrafts, textDrafts);
//                }

                break;
        }
    }

    private void getBase64(String path, String makeUrl) {
        if (makeUrl.contains("gif") || makeUrl.contains("GIF")) {
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(new File(makeUrl));
                byte[] inputData = getBytes(inputStream);
                imageBase64 = Base64.encodeToString(inputData, Base64.NO_WRAP);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Luban.with(this)
                    .load(new File(makeUrl))
                    .ignoreBy(100)
                    .setTargetDir(App.path1 + "/")
                    .filter(new CompressionPredicate() {
                        @Override
                        public boolean apply(String path) {
                            return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                        }
                    })
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(File file) {
                            SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>(datas.get(position).width, datas.get(position).height) {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                                    imageBase64 = ImageUtils.bitmapToBase64(resource);
                                }
                            };
                            GlideApp.with(DraftActivity.this).asBitmap().load(file).into(simpleTarget);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("exception", e.getMessage());
                        }
                    }).launch();


        }

        if (path.contains("http:") || path.contains("https:")) {//背景图
            if (path.contains(".gif") || path.contains("GIF")) {
                presenter.downloadImage(path, 1);
            } else {
                presenter.downloadGif(path, App.path1 + "/" + System.currentTimeMillis() + ".png");
            }
        } else {
            if (path.contains("gif") || path.contains("GIF")) {
                try {
                    InputStream inputStream = new FileInputStream(new File(path));
                    byte[] inputData = getBytes(inputStream);
                    bgImageBase64 = Base64.encodeToString(inputData, Base64.NO_WRAP);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Luban.with(this)
                        .load(new File(path))
                        .ignoreBy(100)
                        .setTargetDir(App.path1 + "/")
                        .filter(new CompressionPredicate() {
                            @Override
                            public boolean apply(String path) {
                                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                            }
                        })
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                                // TODO 压缩开始前调用，可以在方法内启动 loading UI
                            }

                            @Override
                            public void onSuccess(File file) {
                                SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>(datas.get(position).width, datas.get(position).height) {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                                        bgImageBase64 = ImageUtils.bitmapToBase64(resource);
                                    }
                                };
                                GlideApp.with(DraftActivity.this).asBitmap().load(file).into(simpleTarget);
                            }

                            @Override
                            public void onError(Throwable e) {
                            }
                        }).launch();
            }
        }

    }

    @Override
    public void upLoadImageResult(ModifyResultBean response) {
        hideLoading();
        ToastUtils.showShort(this,response.getMsg());

    }

    @Override
    public void downloadImageResult(String base64, int type) {
        if (type == 0) {
            imageBase64 = base64;
        } else {
            bgImageBase64 = base64;
        }
    }

    @Override
    public void downloadGifResult(String path) {

    }

    @Override
    public void recommentTextResult(RecommendTextBean response) {

    }

    @Override
    public void materialResult(MaterialBean response, int type) {

    }

    @Override
    public void requestErrResult(String msg) {
        ToastUtils.showShort(this,msg);
    }

    @Override
    public void setPresenter(EditImagePresenter presenter) {
        if(presenter == null) {
            this.presenter = new EditImagePresenter();
            this.presenter.attachView(this);
        }
    }

    void showLoading(@NonNull String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
