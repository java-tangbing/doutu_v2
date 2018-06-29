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
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.base.BaseMvpActivity;
import com.pufei.gxdt.contents.EventMsg;
import com.pufei.gxdt.contents.MsgType;
import com.pufei.gxdt.db.DraftInfo;
import com.pufei.gxdt.db.DraftInfo_Table;
import com.pufei.gxdt.db.ImageDraft;
import com.pufei.gxdt.db.ImageDraft_Table;
import com.pufei.gxdt.db.TextDraft;
import com.pufei.gxdt.db.TextDraft_Table;
import com.pufei.gxdt.module.home.model.PictureDetailBean;
import com.pufei.gxdt.module.maker.bean.MaterialBean;
import com.pufei.gxdt.module.maker.bean.RecommendTextBean;
import com.pufei.gxdt.module.maker.common.MakerEventMsg;
import com.pufei.gxdt.module.maker.fragment.ImageBlushFragment;
import com.pufei.gxdt.module.maker.fragment.ImageStickerFragment;
import com.pufei.gxdt.module.maker.fragment.ImageTextEditFragment;
import com.pufei.gxdt.module.maker.presenter.EditImagePresenter;
import com.pufei.gxdt.module.maker.view.EditImageView;
import com.pufei.gxdt.module.user.bean.ModifyResultBean;
import com.pufei.gxdt.utils.AppManager;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.widgets.GlideApp;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import photoeditor.BrushDrawingView;
import photoeditor.OnPhotoEditorListener;
import photoeditor.PhotoEditor;
import photoeditor.PhotoEditorView;
import photoeditor.ViewType;
import photoeditor.bean.AddViewBean;
import photoeditor.bean.BitmapBean;
import photoeditor.bean.DraftImageBean;
import photoeditor.bean.DraftTextBean;


public class EditImageActivity extends BaseMvpActivity<EditImagePresenter> implements EditImageView, OnPhotoEditorListener, ImageStickerFragment.ShowBitmapCallback, ImageTextEditFragment.GetInputTextCallback,
        ImageStickerFragment.ShowGifCallback, ImageBlushFragment.SetBlushPropertiesListener {
    @BindView(R.id.btn_next)
    TextView btnNext;
    @BindView(R.id.photoEditorView)
    PhotoEditorView photoEditorView;
    @BindView(R.id.tv_save_draft)
    TextView tvSaveDraft;
    @BindView(R.id.tv_redo)
    ImageView tvUndeo;
    @BindView(R.id.tv_undo)
    ImageView tvUndo;
    @BindView(R.id.tv_delete)
    ImageView tvDelete;
    @BindView(R.id.ll_guide)
    LinearLayout llGuide;
    @BindView(R.id.ll_pic_mode)
    LinearLayout llPicMode;
    @BindView(R.id.ll_text_mode)
    LinearLayout llTextMode;
    @BindView(R.id.ll_blush_mode)
    LinearLayout llBlushMode;
    @BindView(R.id.iv_pic_mode)
    ImageView ivPicMode;
    @BindView(R.id.iv_text_mode)
    ImageView ivTextMode;
    @BindView(R.id.iv_blush_mode)
    ImageView ivBlushMode;
    @BindView(R.id.mode_content)
    FrameLayout modeContent;

    public static String IMAGE_ID = "IMAGE_ID";
    public static String IMAGE_PATH = "IMAGE_PATH";
    public static String EDIT_TYPE = "TYPE";
    public static String EDIT_TYPE_EDIT = "EDIT";//改图
    public static String EDIT_TYPE_DRAFT = "DRAFT";//草稿箱
    private PhotoEditor mPhotoEditor;
    private List<Fragment> fragmentList;
    private int previousIndex;
    private View rootView;
    private String text;
    private int type = 0;
    private ProgressDialog mProgressDialog;
    private File gifFile;
    private String imageId;
    private String id;
    private String uid;
    private String originTable;
    private String imagePath;
    private List<ImageDraft> imageDrafts;
    private List<TextDraft> textDrafts;
    private List<PictureDetailBean.ResultBean.DataBean> attachMentList;
    private int editType = 0;
    private String draftImgPath = "";//制成图的路径
    private Typeface fangzhengjianzhi;
    private Typeface fangzhengkatong;
    private Typeface fangzhengyasong;
    private Typeface lantingdahei;
    private Typeface xindijianzhi;
    private Typeface xindixiaowanzixiaoxueban;
    private int colorMode;
    private boolean isAddBrushImage = false;


    @Override
    public void initView() {
        addFragment();

        fangzhengjianzhi = Typeface.createFromAsset(getAssets(), "fangzhengjianzhi.ttf");
        fangzhengkatong = Typeface.createFromAsset(getAssets(), "fangzhengkatong.ttf");
        fangzhengyasong = Typeface.createFromAsset(getAssets(), "fangzhengyasong.TTF");
        lantingdahei = Typeface.createFromAsset(getAssets(), "lantingdahei.ttf");
        xindijianzhi = Typeface.createFromAsset(getAssets(), "xindijianzhi.ttf");
        xindixiaowanzixiaoxueban = Typeface.createFromAsset(getAssets(), "xindischool.ttf");

        mPhotoEditor = new PhotoEditor.Builder(this, photoEditorView)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                .setDefaultTextTypeface(lantingdahei)
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);
        mPhotoEditor.setBrushColor(ContextCompat.getColor(this, R.color.select_color1));
        mPhotoEditor.setBrushDrawingMode(false);
        imageDrafts = new ArrayList<>();
        textDrafts = new ArrayList<>();
        defaultSelect();
    }

    @Override
    public void getData() {
        Intent intent = getIntent();
        if (intent.getStringExtra(EDIT_TYPE) == null) {
            editType = 0;
            imageId = System.currentTimeMillis() + "";
        } else if (intent.getStringExtra(EDIT_TYPE).equals(EDIT_TYPE_EDIT)) {
            editType = 1;
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                PictureDetailBean.ResultBean bean = (PictureDetailBean.ResultBean) bundle.getSerializable("picture_bean");
                if (bean != null) {
                    imagePath = bean.getUrl();
                    if (TextUtils.isEmpty(bean.getOrginid())) {
                        imageId = System.currentTimeMillis() + "";
                    } else {
                        imageId = bean.getOrginid();
                    }
                    id = bean.getId();
                    uid = bean.getUid();
                    originTable = bean.getOrgintable();
                    GlideApp.with(this).load(imagePath).placeholder(R.mipmap.newloding).into(photoEditorView.getSource());
                    attachMentList = new ArrayList<>();
                    attachMentList.addAll(bean.getData());
                    initEditStatus(attachMentList);
                    if (imagePath.contains("gif") || imagePath.contains("GIF")) {
                        String filePath = App.path1 + "/" + System.currentTimeMillis() + ".gif";
                        presenter.downloadGif(imagePath, filePath);
                    }
                }

            }
        } else if (intent.getStringExtra(EDIT_TYPE).equals(EDIT_TYPE_DRAFT)) {
            editType = 2;
            imageId = intent.getStringExtra(IMAGE_ID);
            imagePath = intent.getStringExtra(IMAGE_PATH);
            if (imagePath.contains("http:") || imagePath.contains("https:")) {
                GlideApp.with(this).load(imagePath).into(photoEditorView.getSource());
                String filePath = App.path1 + "/" + System.currentTimeMillis() + ".gif";
                presenter.downloadGif(imagePath, filePath);
            } else {
                GlideApp.with(this).load(new File(imagePath)).into(photoEditorView.getSource());
            }
            imageDrafts = new Select().from(ImageDraft.class).where(ImageDraft_Table.imageId.is(imageId)).and(ImageDraft_Table.isDraft.is(true)).queryList();
            textDrafts = new Select().from(TextDraft.class).where(TextDraft_Table.imageId.is(imageId)).and(ImageDraft_Table.isDraft.is(true)).queryList();
            initImageStatus(imageDrafts);
            initTextStatus(textDrafts);
        }

    }


    /**
     * 初始化改图状态
     */

    private void initEditStatus(List<PictureDetailBean.ResultBean.DataBean> beans) {
        for (int i = 0; i < beans.size(); i++) {
            final PictureDetailBean.ResultBean.DataBean dataBean = beans.get(i);
            if (dataBean.getUrl().isEmpty()) {
                final DraftTextBean bean = new DraftTextBean();
                bean.setTranslationX(Float.parseFloat(dataBean.getCenterX()));
                bean.setTranslationY(Float.parseFloat(dataBean.getCenterY()));
                bean.setScaleX(Float.parseFloat(dataBean.getZoom()));
                bean.setScaleY(Float.parseFloat(dataBean.getZoom()));
                bean.setRotation(Float.parseFloat(dataBean.getRolling()));
                bean.setWidth(Float.parseFloat(dataBean.getWidth()));
                bean.setHeight(Float.parseFloat(dataBean.getHeight()));
                String text1 = "";
                try {
                    text1 = URLDecoder.decode(dataBean.getTextName(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e("encodeErr", e.getMessage());
                }
                bean.setText(text1);
                bean.setTextColor(Color.parseColor("#" + dataBean.getTextFontColor()));
                String fontStyle = dataBean.getTextFont();
                if (fontStyle.equals("FZLTDHK--GBK1-0")) {
                    bean.setTextFont(fangzhengjianzhi);
                } else if (fontStyle.equals("FZZYSK1--GBK1-0")) {
                    bean.setTextFont(fangzhengkatong);
                } else if (fontStyle.equals("FZKATJW--GB1-0")) {
                    bean.setTextFont(fangzhengyasong);
                } else if (fontStyle.equals("SentyMARUKO-Elementary")) {
                    bean.setTextFont(lantingdahei);
                } else if (fontStyle.equals("SentyPaperCut")) {
                    bean.setTextFont(xindijianzhi);
                } else if (fontStyle.equals("FZJZJW--GB1-0")) {
                    bean.setTextFont(xindixiaowanzixiaoxueban);
                }
//                bean.setTextSize(Integer.parseInt(dataBean.getTextFontSize()));
                photoEditorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        photoEditorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        bean.setBgHeight(photoEditorView.getHeight());
                        bean.setBgWidth(photoEditorView.getWidth());
                        mPhotoEditor.reAddText(bean);
//                        Log.e("fsdfsd", photoEditorView.getWidth() + " " + photoEditorView.getHeight());
                    }
                });
            } else {
                final DraftImageBean bean = new DraftImageBean();
                bean.setTranslationX(Float.parseFloat(dataBean.getCenterX()));
                bean.setTranslationY(Float.parseFloat(dataBean.getCenterY()));
                bean.setScaleX(Float.parseFloat(dataBean.getZoom()));
                bean.setScaleY(Float.parseFloat(dataBean.getZoom()));
                bean.setRotation(Float.parseFloat(dataBean.getRolling()));
                bean.setImageHeight(Float.parseFloat(dataBean.getHeight()));
                bean.setImageWidth(Float.parseFloat(dataBean.getWidth()));
                photoEditorView.post(new Runnable() {
                    @Override
                    public void run() {
                        bean.setBgHeight(photoEditorView.getHeight());
                        bean.setBgWidth(photoEditorView.getWidth());
                        SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                                mPhotoEditor.reAddImage(bean, resource, dataBean.getUrl());
                            }
                        };
                        GlideApp.with(EditImageActivity.this).asBitmap().load(dataBean.getUrl()).into(simpleTarget);
                    }
                });

            }
        }
    }

    /**
     * 初始化草稿箱改图状态
     *
     * @param textDrafts
     */
    private void initTextStatus(List<TextDraft> textDrafts) {
        for (int i = 0; i < textDrafts.size(); i++) {
            final TextDraft draft = textDrafts.get(i);
            final DraftTextBean bean = new DraftTextBean();
            bean.setTranslationX(draft.translationX);
            bean.setTranslationY(draft.translationY);
            bean.setScaleX(draft.scaleX);
            bean.setScaleY(draft.scaleY);
            bean.setRotation(draft.rotation);
            bean.setHeight(draft.height);
            bean.setWidth(draft.width);
            photoEditorView.post(new Runnable() {
                @Override
                public void run() {
                    bean.setBgHeight(photoEditorView.getHeight());
                    bean.setBgWidth(photoEditorView.getWidth());
                    mPhotoEditor.reAddText(bean);
                }
            });
            bean.setText(draft.text);
            bean.setTextColor(Color.parseColor("#" + draft.textColor));
            bean.setTextSize(draft.textSize);
            String fontStyle = draft.textFont;
            if (fontStyle.equals("FZLTDHK--GBK1-0")) {
                bean.setTextFont(fangzhengjianzhi);
            } else if (fontStyle.equals("FZZYSK1--GBK1-0")) {
                bean.setTextFont(fangzhengkatong);
            } else if (fontStyle.equals("FZKATJW--GB1-0")) {
                bean.setTextFont(fangzhengyasong);
            } else if (fontStyle.equals("SentyMARUKO-Elementary")) {
                bean.setTextFont(lantingdahei);
            } else if (fontStyle.equals("SentyPaperCut")) {
                bean.setTextFont(xindijianzhi);
            } else if (fontStyle.equals("FZJZJW--GB1-0")) {
                bean.setTextFont(xindixiaowanzixiaoxueban);
            }
        }
    }

    private void initImageStatus(List<ImageDraft> imageDrafts) {//重新编辑图片
        for (int i = 0; i < imageDrafts.size(); i++) {
            final ImageDraft draft = imageDrafts.get(i);
            final DraftImageBean bean = new DraftImageBean();
            bean.setTranslationX(draft.translationX);
            bean.setTranslationY(draft.translationY);
            bean.setScaleX(draft.scaleX);
            bean.setScaleY(draft.scaleY);
            bean.setRotation(draft.rotation);
            bean.setImageHeight(draft.imageHeight);
            bean.setImageWidth(draft.imageWidth);
            photoEditorView.post(new Runnable() {
                @Override
                public void run() {
                    bean.setBgHeight(photoEditorView.getHeight());
                    bean.setBgWidth(photoEditorView.getWidth());
                    SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                            mPhotoEditor.reAddImage(bean, resource, draft.stickerImagePath);
                        }
                    };
                    GlideApp.with(EditImageActivity.this).asBitmap().load(draft.stickerImagePath).into(simpleTarget);
                }
            });

        }
    }


    @Override
    public int getLayout() {
        return R.layout.activity_image_edit;
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
    public void onEvent(final MakerEventMsg msg) {
        if (msg.getType() == 0) {//替换背景图
            mPhotoEditor.clearAllViews();
            imagePath = msg.getUrl();
            resetOriginId();
            if (imagePath.contains("gif") || imagePath.contains("GIF")) {
                String filePath = App.path1 + "/" + System.currentTimeMillis() + ".gif";
                presenter.downloadGif(imagePath, filePath);
            }
            GlideApp.with(this).load(imagePath).into(photoEditorView.getSource());
        } else if (msg.getType() == 1) {//贴图
            SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>(240, 240) {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                    mPhotoEditor.addImage(resource, msg.getUrl());
                }
            };
            GlideApp.with(this).asBitmap().load(msg.getUrl()).into(simpleTarget);
        } else if (msg.getType() == 2) {//更改文本颜色
            this.colorMode = msg.getColor();
            mPhotoEditor.editText(rootView, text, msg.getColor());
        } else if (msg.getType() == 3) {//更改字体
            if (msg.getTextFontStyle() == 0) {
                mPhotoEditor.editText(rootView, fangzhengjianzhi, text, colorMode);
            } else if (msg.getTextFontStyle() == 1) {
                mPhotoEditor.editText(rootView, fangzhengkatong, text, colorMode);
            } else if (msg.getTextFontStyle() == 2) {
                mPhotoEditor.editText(rootView, fangzhengyasong, text, colorMode);
            } else if (msg.getTextFontStyle() == 3) {
                mPhotoEditor.editText(rootView, lantingdahei, text, colorMode);
            } else if (msg.getTextFontStyle() == 4) {
                mPhotoEditor.editText(rootView, xindijianzhi, text, colorMode);
            } else if (msg.getTextFontStyle() == 5) {
                mPhotoEditor.editText(rootView, xindixiaowanzixiaoxueban, text, colorMode);
            }
        }
    }

    private void resetOriginId() {
        id = "";
        uid = "";
        originTable = "";
    }


    @OnClick({R.id.btn_next, R.id.tv_save_draft, R.id.tv_redo, R.id.tv_undo, R.id.tv_delete, R.id.ll_pic_mode, R.id.ll_text_mode, R.id.ll_blush_mode, R.id.ll_title_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.btn_next:
                if (imagePath != null) {
                    if(mPhotoEditor.getIsContainsBrush()) {//判断是否包含画笔，如果包含画笔则要等view确定位置之后再进行其他操作
                        isAddBrushImage = false;
                        addBrushImg(false);
                    }else {
                        saveToDraft(false);
                        if (imagePath.contains("gif") || imagePath.contains("GIF")) {
                            if (gifFile == null) {
                                gifFile = new File(imagePath);
                            }
                            saveGif(false);
                        } else {
                            saveImage(false);
                        }
                    }

                } else {
                    ToastUtils.showShort(this, "请选择背景图");
                }

                break;
            case R.id.tv_save_draft:
                if (!TextUtils.isEmpty(imagePath)) {
                    if(mPhotoEditor.getIsContainsBrush()) {//判断是否包含画笔，如果包含画笔则要等view确定位置之后再进行其他操作
                        isAddBrushImage = false;
                        addBrushImg(true);
                    }else {
                        if (imagePath.contains("gif") || imagePath.contains("GIF")) {
                            if (gifFile == null) {
                                gifFile = new File(imagePath);
                            }
                            saveGif(true);
                        } else {
                            saveImage(true);
                        }
                    }


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
            case R.id.ll_pic_mode:
                setSelectedItemState(0);
                showFragment(0, previousIndex);
                previousIndex = 0;
                break;
            case R.id.ll_text_mode:
                setSelectedItemState(1);
                showFragment(1, previousIndex);
                previousIndex = 1;
                break;
            case R.id.ll_blush_mode:
                setSelectedItemState(2);
                showFragment(2, previousIndex);
                previousIndex = 2;
                break;
        }
    }

    private void addBrushImg(final boolean isDraft) {
        List<AddViewBean> beans = mPhotoEditor.getAddedViews();
        if(beans.size()> 0) {
            for (int i = 0; i < beans.size(); i++) {
                AddViewBean bean = beans.get(i);
                if(bean.getType() == ViewType.BRUSH_DRAWING) {
                    if (!isAddBrushImage) {
                        BrushDrawingView brush = (BrushDrawingView) bean.getView();
                        Bitmap map = brush.generateBimap();
                        if (map != null) {
                            try {
                                File file = File.createTempFile(System.currentTimeMillis() +".png",null,getCacheDir());
                                FileOutputStream outputStream = new FileOutputStream(file);
                                map.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                                outputStream.close();
                                mPhotoEditor.addBrushImage(map, brush.getMinTouchX(), brush.getMinTouchY(), file.getPath(), new PhotoEditor.AddBrushImageListener() {
                                    @Override
                                    public void addBrushImageSuccess() {
                                        if(!isDraft) {
                                            saveToDraft(false);
                                            if (imagePath.contains("gif") || imagePath.contains("GIF")) {
                                                if (gifFile == null) {
                                                    gifFile = new File(imagePath);
                                                }
                                                saveGif(false);
                                            } else {
                                                saveImage(false);
                                            }
                                        }else {
                                            if (imagePath.contains("gif") || imagePath.contains("GIF")) {
                                                if (gifFile == null) {
                                                    gifFile = new File(imagePath);
                                                    gifFile = new File(imagePath);
                                                }
                                                saveGif(true);
                                            } else {
                                                saveImage(true);
                                            }
                                        }

                                    }
                                });
                                mPhotoEditor.clearBrushAllViews();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("addBrushImage",e.getMessage());
                            }
                        }
                        isAddBrushImage = true;
                    }
                }
            }
        }
    }

    private void saveToDraft(final boolean isDraft) {
        if (!isDraft) {
            showLoading("保存中...");
        }
        SQLite.delete().from(DraftInfo.class).where(DraftInfo_Table.imageId.is(imageId)).and(DraftInfo_Table.isDraft.is(isDraft)).execute();
        SQLite.delete().from(ImageDraft.class).where(ImageDraft_Table.imageId.is(imageId)).and(ImageDraft_Table.isDraft.is(isDraft)).execute();
        SQLite.delete().from(TextDraft.class).where(TextDraft_Table.imageId.is(imageId)).and(TextDraft_Table.isDraft.is(isDraft)).execute();
        List<AddViewBean> beans = mPhotoEditor.getAddedViews();
        DraftInfo info = new DraftInfo();
        info.imageId = imageId;
        info.imagePath = imagePath;
        info.width = photoEditorView.getWidth();
        info.height = photoEditorView.getHeight();
        if (editType == 1) {
            info.originId = id;
            info.uid = uid;
            info.originImageId = imageId;
            info.originTable = originTable;
        } else {
            info.originId = "";
            info.uid = "";
            info.originImageId = "";
            info.originTable = "design_images";
        }
//        info.originImageId = imageId;
        info.make_url = draftImgPath;
        info.isDraft = isDraft;
        info.insert();
        if (beans.size() != 0) {
            for (int i = 0; i < beans.size(); i++) {
                final AddViewBean bean = beans.get(i);
                switch (bean.getType()) {
                    case TEXT:
                        TextDraft textDraft = new TextDraft();
                        TextView tv = (TextView) bean.getAddView();
                        FrameLayout textRoot = (FrameLayout) bean.getView();
                        textDraft.text = tv.getText().toString();
                        textDraft.textColor = Integer.toHexString(tv.getCurrentTextColor()).substring(2);
                        textDraft.textSize = tv.getTextSize();
                        Typeface type = tv.getTypeface();
                        if (type == fangzhengjianzhi) {
                            textDraft.textFont = "FZLTDHK--GBK1-0";
                        } else if (type == fangzhengkatong) {
                            textDraft.textFont = "FZZYSK1--GBK1-0";
                        } else if (type == fangzhengyasong) {
                            textDraft.textFont = "FZKATJW--GB1-0";
                        } else if (type == lantingdahei) {
                            textDraft.textFont = "SentyMARUKO-Elementary";
                        } else if (type == xindijianzhi) {
                            textDraft.textFont = "SentyPaperCut";
                        } else if (type == xindixiaowanzixiaoxueban) {
                            textDraft.textFont = "FZJZJW--GB1-0";
                        }
                        float centerX = ((float) textRoot.getWidth() / 2 + ((float) textRoot.getLeft() + (float) textRoot.getTranslationX())) / (float) photoEditorView.getWidth();
                        float centerY = ((float) textRoot.getHeight() / 2 + (((float) textRoot.getTop()) + (float) textRoot.getTranslationY())) / (float) photoEditorView.getHeight();
                        float width = (float) tv.getWidth() / (float) photoEditorView.getWidth();
                        float height = (float) tv.getHeight() / (float) photoEditorView.getHeight();
                        textDraft.width = width;
                        textDraft.height = height;
                        textDraft.translationX = centerX;
                        textDraft.translationY = centerY;
                        textDraft.scaleX = textRoot.getScaleX();
                        textDraft.scaleY = textRoot.getScaleY();
                        textDraft.rotation = textRoot.getRotation();
                        textDraft.imageId = imageId;
                        textDraft.isDraft = isDraft;
                        textDraft.insert();
                        break;
                    case IMAGE:
                        final ImageDraft imageDraft = new ImageDraft();
                        final ImageView iv = (ImageView) bean.getAddView();
                        final FrameLayout rootview = (FrameLayout) bean.getView();
                        rootview.post(new Runnable() {
                            @Override
                            public void run() {
                                imageDraft.translationX = ((float) rootview.getWidth() / 2 + (float) rootview.getLeft() + rootview.getTranslationX()) / (float) photoEditorView.getWidth();
                                imageDraft.translationY = ((float) rootview.getHeight() / 2 + (float) rootview.getTop() + (float) rootview.getTranslationY()) / (float) photoEditorView.getHeight();
                                imageDraft.scaleX = rootview.getScaleX();
                                imageDraft.scaleY = rootview.getScaleY();
                                imageDraft.rotation = rootview.getRotation();
                                imageDraft.stickerImagePath = bean.getChildImagePath();
                                imageDraft.imageId = imageId;
                                imageDraft.isDraft = isDraft;
                                imageDraft.imageHeight = (float) iv.getHeight() / (float) photoEditorView.getHeight();
                                imageDraft.imageWidth = (float) iv.getWidth() / (float) photoEditorView.getWidth();
                                imageDraft.insert();
                                Log.e("widht", iv.getWidth()  + " " +  iv.getHeight() );
                            }
                        });
                        break;
                    case BRUSH_DRAWING:


//                        SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                Log.e("brush",resource.getWidth() +" " + resource.getHeight());
//
//                            }
//                        };
//                        GlideApp.with(this).asBitmap().load(brush.generateBimap()).into(simpleTarget);

                        break;
                }
            }
        }
        if (!isDraft) {
            hideLoading();
        }
        if (isDraft) {
            ToastUtils.showShort(this, "保存成功");
        }
    }

    private void startToMakerFinish(String path) {
        Intent intent = new Intent(EditImageActivity.this, MakerFinishActivity.class);
        intent.putExtra(MakerFinishActivity.IMAGE_PATH, path);
        intent.putExtra(MakerFinishActivity.IMAGE_ID, imageId);
        intent.putExtra(MakerFinishActivity.TYPE, editType);
        intent.putExtra("Id", id);
        intent.putExtra("uid", uid);
        intent.putExtra("originTable", originTable);
        startActivity(intent);
    }

    private void saveGif(final boolean isDraft) {
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
                                showLoading("保存中...");
                                final String gifPath = App.path1 + "/"
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
                                            bean.setPath(App.path1 + "/" + System.currentTimeMillis() + ".png");
                                            bean.setBitmap(bitmap);
                                            bean.setDelay(frameBitmaps.get(i).getDelay());
                                            gifEncodeBitmap.add(bean);
                                        }

                                        mPhotoEditor.saveFrame(gifEncodeBitmap, new PhotoEditor.OnSaveFrameListener() {
                                            @Override
                                            public void onSaveFrameSuccess() {
                                                for (int i = 0; i < gifEncodeBitmap.size(); i++) {
                                                    BitmapBean bean = gifEncodeBitmap.get(i);
                                                    bean.setBitmap(gifEncodeBitmap.get(i).getBitmap());
                                                    gifEncodeBitmap.set(i, bean);
                                                }
                                            }
                                        });

                                        mPhotoEditor.encodeGif(photoEditorView.getWidth(), photoEditorView.getHeight(), gifPath, gifEncodeBitmap, new PhotoEditor.OnEncodeGifListener() {
                                            @Override
                                            public void onEncodeSuccess(String path) {
                                                hideLoading();
                                                if (!isDraft) {
                                                    startToMakerFinish(path);
                                                } else {
                                                    draftImgPath = path;
                                                    saveToDraft(true);
                                                }
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

    private void saveImage(final boolean isDraft) {
        Acp.getInstance(this)
                .request(new AcpOptions.Builder().setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).build(),
                        new AcpListener() {
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
                                showLoading("保存中...");
                                final String path1 = App.path1 + "/"
                                        + System.currentTimeMillis() + ".png";

                                mPhotoEditor.saveImage(path1, new PhotoEditor.OnSaveListener() {
                                    @Override
                                    public void onSuccess(@NonNull String imagePath) {
                                        hideLoading();
                                        if (!isDraft) {
                                            startToMakerFinish(imagePath);
                                        } else {
                                            draftImgPath = imagePath;
                                            saveToDraft(true);
                                            Log.e("draftImagepath", imagePath);
                                        }
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
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
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

        if (showIndex == 2) {
            mPhotoEditor.setBrushDrawingMode(true);
        } else {
            mPhotoEditor.setBrushDrawingMode(false);
        }
    }

    private void defaultSelect() {
        setSelectedItemState(0);
        showFragment(0, previousIndex);
        previousIndex = 0;
    }

//    private void setSelectedItemState(ImageView iv) {
//        GlideApp.with(this).load(R.mipmap.made_ic_picture_pressed).into(iv);
//    }

    private void setSelectedItemState(int mode) {
        switch (mode) {
            case 0:
                GlideApp.with(this).load(R.mipmap.made_ic_picture_pressed).into(ivPicMode);
                GlideApp.with(this).load(R.mipmap.made_ic_character_normal).into(ivTextMode);
                GlideApp.with(this).load(R.mipmap.made_ic_pen_normal).into(ivBlushMode);
                break;
            case 1:
                GlideApp.with(this).load(R.mipmap.made_ic_picture_normal).into(ivPicMode);
                GlideApp.with(this).load(R.mipmap.made_ic_character_pressed).into(ivTextMode);
                GlideApp.with(this).load(R.mipmap.made_ic_pen_normal).into(ivBlushMode);
                break;
            case 2:
                GlideApp.with(this).load(R.mipmap.made_ic_picture_normal).into(ivPicMode);
                GlideApp.with(this).load(R.mipmap.made_ic_character_normal).into(ivTextMode);
                GlideApp.with(this).load(R.mipmap.made_ic_pen_pressed).into(ivBlushMode);
                break;
        }
    }

    @Override
    public void showBitmap(Bitmap bitmap, String path) {
        imagePath = path;
        mPhotoEditor.clearAllViews();
        resetOriginId();
//        GlideApp.with(this).load(path).into(photoEditorView.getSource());
        photoEditorView.getSource().setImageBitmap(bitmap);
    }

    @Override
    public void showGif(File gif, String path) {
        imagePath = path;
        resetOriginId();
        GlideApp.with(this).load(gif).into(photoEditorView.getSource());
    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode) {
        this.rootView = rootView;
        if (rootView == null) {
            type = 0;
        } else {
            this.text = text;
            this.colorMode = colorCode;
            type = 1;
        }
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onRemoveViewListener(int numberOfAddedViews) {
        type = 0;
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
            mPhotoEditor.addText(fangzhengjianzhi, text, color);
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
    public void upLoadImageResult(ModifyResultBean response) {

    }

    @Override
    public void downloadImageResult(String base64, int type) {

    }

    @Override
    public void downloadGifResult(String path) {
        gifFile = new File(path);
    }

    @Override
    public void recommentTextResult(RecommendTextBean response) {

    }

    @Override
    public void materialResult(MaterialBean response, int type) {

    }

    @Override
    public void requestErrResult(String msg) {
        ToastUtils.showShort(this, msg);
    }

    @Override
    public void requestSuccess(String msg) {

    }


    @Override
    public void setPresenter(EditImagePresenter presenter) {
        if (presenter == null) {
            this.presenter = new EditImagePresenter();
            this.presenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (editType == 2) {
//            EventBus.getDefault().postSticky(new MakerEventMsg(5, imagePath));

        } else {
            EventBus.getDefault().postSticky(new EventMsg(MsgType.MAKER_IMAGE));
        }
    }
}
