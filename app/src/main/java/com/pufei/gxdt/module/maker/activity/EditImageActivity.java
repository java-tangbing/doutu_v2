package com.pufei.gxdt.module.maker.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import com.pufei.gxdt.utils.BitmapMergerTask;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.widgets.GlideApp;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ja.burhanrashid52.photoeditor.AddViewBean;
import ja.burhanrashid52.photoeditor.BitmapBean;
import ja.burhanrashid52.photoeditor.DraftBean;
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.TextBean;
import ja.burhanrashid52.photoeditor.ViewType;

public class EditImageActivity extends BaseActivity implements OnPhotoEditorListener, ImageStickerFragment.ShowBitmapCallback, ImageTextEditFragment.GetInputTextCallback,
        ImageStickerFragment.ShowGifCallback, ImageBlushFragment.SetBlushPropertiesListener {
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
    private File gifFile;
    private boolean isGifBg = false; //背景图片是否为gif图

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
                if (isGifBg) {
                    saveGif();
                } else {
                    saveImage();
                }
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
            if (msg.getUrl().contains(".gif") || msg.getUrl().contains("GIF")) {
                isGifBg = true;
            } else {
                isGifBg = false;
            }
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

    private void saveGif() {
        Acp.getInstance(this)
                .request(new AcpOptions.Builder().setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).build(),
                        new AcpListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onGranted() {
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
                                showLoading("Saving...");
                                final String gifPath = Environment.getExternalStorageDirectory()
                                        + File.separator + ""
                                        + System.currentTimeMillis() + ".gif";

                                photoEditorView.setBackgroundColor(Color.TRANSPARENT);
                                photoEditorView.getSource().setBackgroundColor(Color.TRANSPARENT);
                                photoEditorView.getSource().setImageAlpha(0);
                                final List<BitmapBean> gifEncodeBitmap = new ArrayList<>();
                                mPhotoEditor.saveGif(gifFile, new PhotoEditor.OnDecodeGifListener() {
                                    @Override
                                    public void onDecodeSuccess(List<BitmapBean> frameBitmaps, List<Bitmap> bitmaps) {
                                        photoEditorView.getSource().setImageAlpha(255);
                                        photoEditorView.setBackgroundColor(Color.WHITE);
                                        photoEditorView.getSource().setBackgroundColor(Color.WHITE);
                                        for (int i = 0; i < frameBitmaps.size(); i++) {
                                            Bitmap bitmap = mergeBitmap(frameBitmaps.get(i).getBitmap(), bitmaps.get(0));
                                            BitmapBean bean = new BitmapBean();
                                            bean.setBitmap(bitmap);
                                            bean.setDelay(frameBitmaps.get(i).getDelay());
                                            gifEncodeBitmap.add(bean);
                                        }
                                        mPhotoEditor.encodeGif(photoEditorView.getWidth(), photoEditorView.getHeight(), gifPath, gifEncodeBitmap, new PhotoEditor.OnEncodeGifListener() {
                                            @Override
                                            public void onEncodeSuccess(String path) {
                                                hideLoading();
//                                                mPhotoEditor.clearAllViews();
                                                Intent intent = new Intent(EditImageActivity.this,MakerFinishActivity.class);
                                                intent.putExtra(MakerFinishActivity.IMAGE_PATH,path);
                                                startActivity(intent);
//                                                GlideApp.with(EditImageActivity.this).load(new File(path)).into(photoEditorView.getSource());
                                            }

                                            @Override
                                            public void onEncodeFailed(Exception e) {

                                            }
                                        });

                                    }

                                    @Override
                                    public void onDecodeFailed(Exception e) {
                                        hideLoading();
                                    }
                                });

                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                ToastUtils.showShort(EditImageActivity.this, "请求权限失败,请手动开启！");
                            }
                        });
    }

    private void saveImage() {
        Acp.getInstance(this)
                .request(new AcpOptions.Builder().setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).build(),
                        new AcpListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onGranted() {
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
                                showLoading("Saving...");
                                final String path = Environment.getExternalStorageDirectory()
                                        + File.separator + ""
                                        + System.currentTimeMillis() + ".png";

                                mPhotoEditor.saveImage(path, new PhotoEditor.OnSaveListener() {
                                    @Override
                                    public void onSuccess(@NonNull String imagePath) {
                                        hideLoading();
                                        Intent intent = new Intent(EditImageActivity.this,MakerFinishActivity.class);
                                        intent.putExtra(MakerFinishActivity.IMAGE_PATH,imagePath);
                                        startActivity(intent);
//                                        photoEditorView.getSource().setImageURI(Uri.fromFile(new File(imagePath)));
                                    }

                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        hideLoading();
                                    }
                                });


                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                ToastUtils.showShort(EditImageActivity.this, "请求权限失败,请手动开启！");
                            }
                        });
    }

    public Bitmap setImgSize(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public Bitmap mergeBitmap(Bitmap backBitmap, Bitmap frontBitmap) {
        int bgWidth = photoEditorView.getWidth();
        int bgHeight = photoEditorView.getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        cv.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG));
        backBitmap = setImgSize(backBitmap, bgWidth, bgHeight);
        cv.drawBitmap(backBitmap, 0, 0, paint);
        cv.drawBitmap(frontBitmap, 0, 0, paint);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
        return newbmp;
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
        isGifBg = false;
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


    @Override
    public void showGif(File gif) {
        isGifBg = true;
        GlideApp.with(this).load(gif).into(photoEditorView.getSource());
        gifFile = gif;
    }
}
