package com.pufei.gxdt.module.maker.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.pufei.gxdt.R;
import com.pufei.gxdt.base.BaseActivity;
import com.pufei.gxdt.db.DraftInfo;
import com.pufei.gxdt.db.DraftInfo_Table;
import com.pufei.gxdt.module.maker.common.MakerEventMsg;
import com.pufei.gxdt.module.maker.fragment.ImageBlushFragment;
import com.pufei.gxdt.module.maker.fragment.ImageStickerFragment;
import com.pufei.gxdt.module.maker.fragment.ImageTextEditFragment;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.widgets.GlideApp;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ja.burhanrashid52.photoeditor.AddViewBean;
import ja.burhanrashid52.photoeditor.DraftBean;
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.TextBean;
import ja.burhanrashid52.photoeditor.ViewType;

public class EditImageActivity extends BaseActivity implements OnPhotoEditorListener, ImageStickerFragment.ShowBitmapCallback, ImageTextEditFragment.GetInputTextCallback, ImageBlushFragment.SetBlushPropertiesListener {
    @BindView(R.id.btn_next)
    TextView btnNext;
    @BindView(R.id.photoEditorView)
    PhotoEditorView photoEditorView;
    @BindView(R.id.tv_save_draft)
    TextView tvSaveDraft;
    @BindView(R.id.tv_redo)
    TextView tvUndeo;
    @BindView(R.id.tv_undo)
    TextView tvUndo;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_guide)
    LinearLayout llGuide;
    @BindView(R.id.tv_pic_mode)
    TextView tvPicMode;
    @BindView(R.id.tv_text_mode)
    TextView tvTextMode;
    @BindView(R.id.tv_blush_mode)
    TextView tvBlushMode;
    @BindView(R.id.mode_content)
    FrameLayout modeContent;

    private PhotoEditor mPhotoEditor;
    private List<Fragment> fragmentList;
    private int previousIndex;
    private View rootView;
    private String text;
    private int type = 0;
    private ProgressDialog mProgressDialog;


    @Override
    public void initView() {
        addFragment();
        defaultSelect();
        mPhotoEditor = new PhotoEditor.Builder(this, photoEditorView)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                //.setDefaultTextTypeface(mTextRobotoTf)
                //.setDefaultEmojiTypeface(mEmojiTypeFace)
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);

    }

    @Override
    public void getData() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_image_edit;
    }

    @OnClick({R.id.btn_next, R.id.tv_save_draft, R.id.tv_redo, R.id.tv_undo, R.id.tv_delete, R.id.tv_pic_mode, R.id.tv_text_mode, R.id.tv_blush_mode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                saveImage();
                break;
            case R.id.tv_save_draft:
                for (int i = 0; i < mPhotoEditor.getAddViews().size(); i++) {
                    TextBean bean = mPhotoEditor.getAddViews().get(i);
                    TextView view1 = (TextView) bean.getTextView();
                    DraftInfo draft = new DraftInfo();
                    draft.viewId = 2;
                    draft.color = view1.getCurrentTextColor();
                    draft.type = view1.getGravity();
                    draft.height = view1.getMeasuredHeight();
                    draft.width = view1.getMeasuredWidth();
                    draft.rotation = view1.getRotation();
                    draft.xPos = view1.getX();
                    draft.yPos = view1.getY();
                    draft.text = view1.getText().toString();
                    draft.insert();
                }

                break;
            case R.id.tv_redo:
                mPhotoEditor.redo();
                break;
            case R.id.tv_undo:
                mPhotoEditor.undo();
                break;
            case R.id.tv_delete:
                mPhotoEditor.clearAllViews();
                break;
            case R.id.tv_pic_mode:
                setSelectedItemState(tvPicMode);
                setUnSelectedItemState(tvTextMode, tvBlushMode);
                showFragment(0, previousIndex);
                previousIndex = 0;
                break;
            case R.id.tv_text_mode:
                setSelectedItemState(tvTextMode);
                setUnSelectedItemState(tvPicMode, tvBlushMode);
                showFragment(1, previousIndex);
                previousIndex = 1;
                break;
            case R.id.tv_blush_mode:
                setSelectedItemState(tvBlushMode);
                setUnSelectedItemState(tvPicMode, tvTextMode);
                showFragment(2, previousIndex);
                previousIndex = 2;
                break;
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MakerEventMsg msg) {
        if (msg.getType() == 0) {//替换背景图
            mPhotoEditor.clearAllViews();
            GlideApp.with(this).load(msg.getUrl()).into(photoEditorView.getSource());
        } else if (msg.getType() == 1) {//贴图
            SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>(240, 240) {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                    mPhotoEditor.addImage(resource);
                }
            };
            GlideApp.with(this).asBitmap().load(msg.getUrl()).into(simpleTarget);
        } else if (msg.getType() == 2) {//更改文本颜色
            mPhotoEditor.editText(rootView, text, msg.getColor());
        }
    }

    private void saveImage() {
        Acp.getInstance(this)
                .request(new AcpOptions.Builder().setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                showLoading("Saving...");
                                File file = new File(Environment.getExternalStorageDirectory()
                                        + File.separator + ""
                                        + System.currentTimeMillis() + ".png");
                                try {
                                    file.createNewFile();
                                    if (ActivityCompat.checkSelfPermission(EditImageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return;
                                    }
                                    mPhotoEditor.saveImage(file.getAbsolutePath(), new PhotoEditor.OnSaveListener() {
                                        @Override
                                        public void onSuccess(@NonNull String imagePath) {
                                            hideLoading();
                                            photoEditorView.getSource().setImageURI(Uri.fromFile(new File(imagePath)));
                                        }

                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            hideLoading();
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                ToastUtils.showShort(EditImageActivity.this, "请求权限失败,请手动开启！");
                            }
                        });
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

    private void addFragment() {
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(ImageStickerFragment.newInstance());
        fragmentList.add(ImageTextEditFragment.newInstance());
        fragmentList.add(ImageBlushFragment.newInstance());
    }

    private void showFragment(int showIndex, int hideIndex) {//fragment管理
        Fragment showFragment = fragmentList.get(showIndex);
        Fragment hideFragment = fragmentList.get(hideIndex);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (!showFragment.isAdded()) {
            ft.add(R.id.mode_content, showFragment);
        }
        if (showIndex == hideIndex) {
            ft.show(showFragment).commit();
        } else {
            ft.show(showFragment).hide(hideFragment).commit();
        }
    }

    private void defaultSelect() {
        setSelectedItemState(tvPicMode);
        setUnSelectedItemState(tvTextMode, tvBlushMode);
        showFragment(0, previousIndex);
        previousIndex = 0;
    }

    private void setSelectedItemState(TextView tv) {
        tv.setTextColor(ContextCompat.getColor(this, R.color.black));
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    private void setUnSelectedItemState(TextView tv1, TextView tv2) {
        tv1.setTextColor(ContextCompat.getColor(this, R.color.text_default_color));
        tv1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tv2.setTextColor(ContextCompat.getColor(this, R.color.text_default_color));
        tv2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    @Override
    public void showBitmap(Bitmap bitmap) {
        mPhotoEditor.clearAllViews();
        photoEditorView.getSource().setImageBitmap(bitmap);
    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode) {
        this.rootView = rootView;
        if (rootView == null) {
            type = 0;
        } else {
            this.text = text;
            type = 1;
        }
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onRemoveViewListener(int numberOfAddedViews) {

    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {

    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {

    }

    @Override
    public void getInputTextCallback(int type, String text, int color) {
        if (type == 0) {
            mPhotoEditor.addText(text, color);
        } else {
            mPhotoEditor.editText(rootView, text, color);
        }
    }

    public int getTypes() {
        return type;
    }

    @Override
    public void setBlushMode(boolean isOpenBlush) {
        mPhotoEditor.setBrushDrawingMode(isOpenBlush);
    }

    @Override
    public void setBlushColor(int color) {
        mPhotoEditor.setBrushColor(color);
    }

    @Override
    public void setBlushSize(int size) {
        mPhotoEditor.setBrushSize(size);
    }

    @Override
    public void setBlushEraser() {
        mPhotoEditor.brushEraser();
    }


}
