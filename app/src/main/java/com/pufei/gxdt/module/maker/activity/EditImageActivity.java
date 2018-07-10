package com.pufei.gxdt.module.maker.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.pufei.gxdt.MainActivity;
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
import com.pufei.gxdt.module.home.activity.PictureDetailActivity;
import com.pufei.gxdt.module.home.model.PictureDetailBean;
import com.pufei.gxdt.module.login.activity.LoginActivity;
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
import com.pufei.gxdt.utils.ImageUtils;
import com.pufei.gxdt.utils.RetrofitFactory;
import com.pufei.gxdt.utils.SharedPreferencesUtil;
import com.pufei.gxdt.utils.SystemInfoUtils;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.utils.UmengStatisticsUtil;
import com.pufei.gxdt.utils.UploadImageUtil;
import com.pufei.gxdt.widgets.GlideApp;
import com.pufei.gxdt.widgets.GuideView;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.umeng.qq.handler.UmengQQHandler;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
    @BindView(R.id.ll_redo)
    LinearLayout llUndeo;
    @BindView(R.id.ll_undo)
    LinearLayout llUndo;
    @BindView(R.id.ll_delete)
    LinearLayout llDelete;
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
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.cx_publish)
    CheckBox cxPublish;
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
    private Bitmap brushBitmap;
    private float brushX;
    private float brushY;


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
        initGuideView();
        initPhotoViewBg();
    }

    private void initGuideView() {
        int isShowGuideView = SharedPreferencesUtil.getInstance().getInt("GuideView",0);//0---show,1--hide
        if (isShowGuideView == 0) {
            SharedPreferencesUtil.getInstance().putInt("GuideView",1);
            ImageView iv = new ImageView(this);
            iv.setImageResource(R.mipmap.ic_picture_hint);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            iv.setLayoutParams(params);
            GuideView.Builder
                    .newInstance(this)
                    .setTargetView(cxPublish)
                    .setCustomGuideView(iv)
                    .setDirction(GuideView.Direction.LEFT_TOP)
                    .setShape(GuideView.MyShape.RECTANGULAR)
                    .setBgColor(getResources().getColor(R.color.shadow))
                    .setOnclickExit(true)
                    .setRadius(32)
                    .build()
                    .show();
        }

    }

    private void initPhotoViewBg() {
        GlideApp.with(this).load(R.drawable.com_made_ic_bg).into(photoEditorView.getSource());
        FileOutputStream outputStream = null;
        File file = null;
        try {
            file = File.createTempFile(System.currentTimeMillis()+".png","",getCacheDir());
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res,R.drawable.com_made_ic_bg);
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(file != null) {
            imagePath = file.getAbsolutePath();
        }else {
            imagePath = "";
        }
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
                    if (TextUtils.isEmpty(bean.getOrginid())) {
                        imageId = System.currentTimeMillis() + "";
                    } else {
                        imageId = bean.getOrginid();
                    }
                    id = bean.getId();
                    uid = bean.getUid();
                    originTable = bean.getOrgintable();
                    imagePath = bean.getUrl();
                    GlideApp.with(this).load(imagePath).placeholder(R.mipmap.newloding).into(photoEditorView.getSource());
                    initEditStatus(bean.getData());
                }

            }
        } else if (intent.getStringExtra(EDIT_TYPE).equals(EDIT_TYPE_DRAFT)) {
            editType = 2;
            imageId = intent.getStringExtra(IMAGE_ID);
            imagePath = intent.getStringExtra(IMAGE_PATH);
            if (imagePath.contains("http:") || imagePath.contains("https:")) {
                GlideApp.with(this).load(imagePath).placeholder(R.mipmap.newloding).into(photoEditorView.getSource());
            } else {
                GlideApp.with(this).load(new File(imagePath)).placeholder(R.mipmap.newloding).into(photoEditorView.getSource());
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
                if (!TextUtils.isEmpty(dataBean.getWidth()) && !TextUtils.isEmpty(dataBean.getHeight())) {
                    bean.setWidth(Float.parseFloat(dataBean.getWidth()));
                    bean.setHeight(Float.parseFloat(dataBean.getHeight()));
                } else {
                    bean.setWidth(0);
                    bean.setHeight(0);
                }

                String text1 = "";
                try {
                    text1 = URLDecoder.decode(dataBean.getTextName(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e(getClass().getSimpleName(),"encodeErr "+e.getMessage());
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
                photoEditorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        photoEditorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        bean.setBgHeight(photoEditorView.getHeight());
                        bean.setBgWidth(photoEditorView.getWidth());
                        mPhotoEditor.reAddText(bean);
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
            type = 0;
            mPhotoEditor.clearAllViews();
            imagePath = msg.getUrl();
            resetOriginId();
            GlideApp.with(this).load(imagePath).placeholder(R.mipmap.newloding).into(photoEditorView.getSource());
        } else if (msg.getType() == 1) {//贴图
//            mPhotoEditor.addGifImage("http://img3.duitang.com/uploads/item/201411/12/20141112224456_xyc4P.gif");
            mPhotoEditor.addImage(msg.getUrl());
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
        } else if (msg.getType() == 4) {//添加画笔
            brushBitmap = msg.getBitmap();
            brushX = msg.getX();
            brushY = msg.getY();
        }else if (msg.getType() == 12) {
            presenter.upLoadImage(RetrofitFactory.getRequestBody(msg.getUrl()));
        }
    }

    private void resetOriginId() {
        id = "";
        uid = "";
        originTable = "";
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick({R.id.btn_next, R.id.tv_save_draft, R.id.ll_redo, R.id.ll_undo, R.id.ll_delete, R.id.ll_pic_mode, R.id.ll_text_mode, R.id.ll_blush_mode, R.id.ll_title_back,R.id.ll_down_load,R.id.tv_share_qq,R.id.tv_share_wx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_back:
                AppManager.getAppManager().finishActivity();
                break;
            case R.id.btn_next:
//                doneImage(false);
                break;
            case R.id.tv_save_draft:
//                doneImage(true);
                break;
            case R.id.ll_redo:
                mPhotoEditor.redo();
                break;
            case R.id.ll_undo:
                mPhotoEditor.undo();
                break;
            case R.id.ll_delete:
                mPhotoEditor.clearAllViews();
                GlideApp.with(this).load(R.drawable.com_made_ic_bg).into(photoEditorView.getSource());
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
            case R.id.ll_down_load:
                doneImage(true,null);
                break;
            case R.id.tv_share_qq:
                doneImage(false,SHARE_MEDIA.QQ);
                break;
            case R.id.tv_share_wx:
                doneImage(false,SHARE_MEDIA.WEIXIN);
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("MissingPermission")
    private void doneImage(final boolean isdraft, final SHARE_MEDIA media) {
        if (!TextUtils.isEmpty(imagePath)) {
            showLoading("保存中...");
            mPhotoEditor.generateImage(photoEditorView.getWidth(), photoEditorView.getHeight(), imagePath, new PhotoEditor.OnDecodeImageListener() {
                @Override
                public void onDecodeSuccess(List<String> path) {
                    if(path.size() > 0) {
                        draftImgPath = path.get(0);
                        saveToDraft(isdraft,media);
//                        Log.e("path",draftImgPath);
                    }

                }

                @Override
                public void onDecodeFailed(Exception e) {
                    if (e.getMessage() == null) {
                        ToastUtils.showShort(EditImageActivity.this, "画笔不能超出画布!");
                    } else {
                        ToastUtils.showShort(EditImageActivity.this, "生成表情失败!!");

                    }
                }
            });
        } else {
            ToastUtils.showShort(this, "背景图不能为空!");
        }
    }

    private void saveToDraft(final boolean isDraft,SHARE_MEDIA media) {
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
                        isAddBrushImage = true;
                        final ImageDraft imageDraft = new ImageDraft();
                        final ImageView iv = (ImageView) bean.getAddView();
                        final FrameLayout rootview = (FrameLayout) bean.getView();
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
                        imageDraft.type = 0;
                        imageDraft.insert();
                        isAddBrushImage = false;
                        break;
                    case BRUSH_DRAWING:
                        ImageDraft draft = new ImageDraft();
                        float brushCenterX = (brushBitmap.getWidth() / 2 + brushX) / photoEditorView.getWidth();
                        float brushCenterY = (brushBitmap.getHeight() / 2 + brushY) / photoEditorView.getHeight();
                        draft.translationX = brushCenterX;
                        draft.translationY = brushCenterY;
                        draft.scaleX = 1;
                        draft.scaleY = 1;
                        draft.rotation = 0;
                        draft.stickerImagePath = ImageUtils.bitmapToBase64(brushBitmap);
                        draft.imageId = imageId;
                        draft.isDraft = isDraft;
                        draft.imageWidth = (float)brushBitmap.getWidth() / photoEditorView.getWidth();
                        draft.imageHeight = (float) brushBitmap.getHeight() / photoEditorView.getHeight();
                        draft.type = 1;
                        draft.insert();
                        break;
                }
            }
        }
        hideLoading();
        if (isDraft) {
            if(!TextUtils.isEmpty(draftImgPath)) {
                try {
                    File file = new File(draftImgPath);
                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), "斗图大师", "");
                    Uri uri = Uri.fromFile(file);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    ToastUtils.showShort(this, "图片已保存到:" + draftImgPath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if(cxPublish.isChecked()) {
                if (App.userBean != null) {
                    setRequestData(info,draftImgPath,imagePath);
                }
            }
            share(draftImgPath,media);
        }
    }

    private void share(String path,SHARE_MEDIA media) {
        if(path.contains(".gif") && media.equals(SHARE_MEDIA.WEIXIN)) {
            UMEmoji emoji = new UMEmoji(this,new File(path));
            emoji.setThumb(new UMImage(this, new File(path)));
            new ShareAction(EditImageActivity.this).setCallback(umShareListener)
                    .withMedia(emoji).setPlatform(media).share();
        }else {
            UMImage image = new UMImage(EditImageActivity.this, new File(path));//本地文件
            image.compressStyle = UMImage.CompressStyle.SCALE;
            image.compressStyle = UMImage.CompressStyle.QUALITY;
            UMImage thumb =  new UMImage(this, new File(path));
            image.setThumb(thumb);
            new ShareAction(EditImageActivity.this).withMedia(image).setPlatform(media).setCallback(umShareListener).share();
        }

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            //ToastUtils.showShort(EditImageActivity.this, "开始分享");
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform.equals(SHARE_MEDIA.WEIXIN)) {
                UmengStatisticsUtil.statisticsEvent(EditImageActivity.this, "17");
            } else if (platform.equals(SHARE_MEDIA.QQ)) {
                UmengStatisticsUtil.statisticsEvent(EditImageActivity.this, "15");
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            for (int i = 0; i < t.getStackTrace().length; i++) {
                Log.e("share error",t.getStackTrace()[i]+"");
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };

    private void setRequestData(final DraftInfo info, final String makeUrl, String bgPath) {
        UploadImageUtil.getImageBase64(this,bgPath, makeUrl, new UploadImageUtil.OnGetBase64Listener() {
            @Override
            public void getBase64Success(List<String> base64List) {
                if (!TextUtils.isEmpty(base64List.get(0)) && !TextUtils.isEmpty(base64List.get(1))) {
//                    showLoading("上传中...");
                    final List<ImageDraft> imageDrafts = new Select().from(ImageDraft.class).where(ImageDraft_Table.imageId.is(imageId)).and(ImageDraft_Table.isDraft.is(false)).queryList();
                    final List<TextDraft> textDrafts = new Select().from(TextDraft.class).where(TextDraft_Table.imageId.is(imageId)).and(TextDraft_Table.isDraft.is(false)).queryList();
                    UploadImageUtil.uploadImage(EditImageActivity.this, info, makeUrl, base64List.get(1), base64List.get(0), imageDrafts, textDrafts);
                }else {
                    ToastUtils.showShort(EditImageActivity.this,"图片解析错误,请稍后重试!");
                }
            }

            @Override
            public void getBase64Failed(Exception e) {
                ToastUtils.showShort(EditImageActivity.this,"图片解析错误,请稍后重试!");
            }
        });


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
        GlideApp.with(this).load(gif).placeholder(R.mipmap.newloding).into(photoEditorView.getSource());
    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode) {
        if (rootView == null) {
            type = 0;
        } else {
            this.rootView = rootView;
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
        if(response.getCode() == 0 ) {
            if(!TextUtils.isEmpty(id)) {
                Map<String,String> map = new HashMap<>();
                map.put("deviceid", SystemInfoUtils.deviced(this));
                map.put("version", SystemInfoUtils.versionName(this));
                map.put("sign","sign");
                map.put("key","key");
                map.put("timestamp", (System.currentTimeMillis() / 1000) + "");
                map.put("os", "1");
                map.put("id",id);
                map.put("type","3");
                map.put("orgintable",originTable);
                map.put("option","edit");
                presenter.favoriteCounter(RetrofitFactory.getRequestBody(new Gson().toJson(map)));
            }
            //ToastUtils.showShort(this, "发布成功");
        }else {
            ToastUtils.showShort(this, response.getMsg());
        }
    }

    @Override
    public void downloadImageResult(String base64, int type) {

    }

    @Override
    public void downloadGifResult(String path) {
        try {
            gifFile = File.createTempFile(path, null, getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(getClass().getSimpleName(), e.getMessage() + "");
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
