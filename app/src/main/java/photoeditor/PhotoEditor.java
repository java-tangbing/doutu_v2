package photoeditor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.annotation.UiThread;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.pufei.gxdt.R;
import com.pufei.gxdt.app.App;
import com.pufei.gxdt.module.maker.common.MakerEventMsg;
import com.pufei.gxdt.utils.ToastUtils;
import com.pufei.gxdt.widgets.GlideApp;
import com.waynejo.androidndkgif.GifDecoder;
import com.waynejo.androidndkgif.GifEncoder;
import com.waynejo.androidndkgif.GifImage;
import com.waynejo.androidndkgif.GifImageIterator;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import photoeditor.bean.AddViewBean;
import photoeditor.bean.BitmapBean;
import photoeditor.bean.DraftImageBean;
import photoeditor.bean.DraftTextBean;
import photoeditor.bean.TextBean;
import photoeditor.gifmaker.AnimatedGifEncoder;

/**
 * <p>
 * This class in initialize by {@link PhotoEditor.Builder} using a builder pattern with multiple
 * editing attributes
 * </p>
 *
 * @author <a href="https://github.com/burhanrashid52">Burhanuddin Rashid</a>
 * @version 0.1.1
 * @since 18/01/2017
 */
public class PhotoEditor implements BrushViewChangeListener {

    private static final String TAG = PhotoEditor.class.getSimpleName();
    private final LayoutInflater mLayoutInflater;
    private Context context;
    private RelativeLayout parentView;
    private ImageView imageView;
    private View deleteView;
    private BrushDrawingView brushDrawingView;
    private List<AddViewBean> addedViews;
    private List<AddViewBean> redoViews;
    private OnPhotoEditorListener mOnPhotoEditorListener;
    private boolean isTextPinchZoomable;
    private Typeface mDefaultTextTypeface;
    private Typeface mDefaultEmojiTypeface;
    private List<TextBean> addTextViews;
    private boolean isAddBrushImageFinish = false;


    private PhotoEditor(Builder builder) {
        this.context = builder.context;
        this.parentView = builder.parentView;
        this.imageView = builder.imageView;
        this.deleteView = builder.deleteView;
        this.brushDrawingView = builder.brushDrawingView;
        this.isTextPinchZoomable = builder.isTextPinchZoomable;
        this.mDefaultTextTypeface = builder.textTypeface;
        this.mDefaultEmojiTypeface = builder.emojiTypeface;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        brushDrawingView.setBrushViewChangeListener(this);
        addedViews = new ArrayList<>();
        redoViews = new ArrayList<>();
        addTextViews = new ArrayList<>();
    }


    /**
     * This will add image on {@link PhotoEditorView} which you drag,rotate and scale using pinch
     * if {@link PhotoEditor.Builder#setPinchTextScalable(boolean)} enabled
     */
    public void addImage(String path) {
        final View imageRootView = getLayout(ViewType.IMAGE, path);
        final ImageView imageView = imageRootView.findViewById(R.id.imgPhotoEditorImage);
        final FrameLayout frmBorder = imageRootView.findViewById(R.id.frmBorder);
        final ImageView imgClose = imageRootView.findViewById(R.id.imgPhotoEditorClose);
        frmBorder.setTag(true);
//        imageView.setImageBitmap(desiredImage);
        GlideApp.with(context).load(path).into(imageView);
        MultiTouchListener multiTouchListener = getMultiTouchListener();
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick() {
                boolean isBackgroundVisible = frmBorder.getTag() != null && (boolean) frmBorder.getTag();
                frmBorder.setBackgroundResource(isBackgroundVisible ? 0 : R.drawable.rounded_border_tv);
                imgClose.setVisibility(isBackgroundVisible ? View.GONE : View.VISIBLE);
                frmBorder.setTag(!isBackgroundVisible);
            }

            @Override
            public void onLongClick() {

            }
        });

        imageRootView.setOnTouchListener(multiTouchListener);

        addViewToParent(imageRootView, imageView, path, ViewType.IMAGE);

    }


    public boolean getIsContainsBrush() {
        for (int i = 0; i < addedViews.size(); i++) {
            if (addedViews.get(i).getType() == ViewType.BRUSH_DRAWING) {
                return true;
            }
        }
        return false;
    }

    public void addGifImage(String path) {
        final View imageRootView = getLayout(ViewType.IMAGE, path);
        final ImageView imageView = imageRootView.findViewById(R.id.imgPhotoEditorImage);
        final FrameLayout frmBorder = imageRootView.findViewById(R.id.frmBorder);
        final ImageView imgClose = imageRootView.findViewById(R.id.imgPhotoEditorClose);
        frmBorder.setTag(true);
        GlideApp.with(context).load(path).into(imageView);
        MultiTouchListener multiTouchListener = getMultiTouchListener();
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick() {
                boolean isBackgroundVisible = frmBorder.getTag() != null && (boolean) frmBorder.getTag();
                frmBorder.setBackgroundResource(isBackgroundVisible ? 0 : R.drawable.rounded_border_tv);
                imgClose.setVisibility(isBackgroundVisible ? View.GONE : View.VISIBLE);
                frmBorder.setTag(!isBackgroundVisible);
            }

            @Override
            public void onLongClick() {

            }
        });

        imageRootView.setOnTouchListener(multiTouchListener);

        addViewToParent(imageRootView, imageView, path, ViewType.IMAGE);
    }


    public void addBrushImage(final Bitmap brushBitmap, final float x, final float y, String path) {
        final View imageRootView = getLayout(ViewType.IMAGE, path);
        final ImageView imageView = imageRootView.findViewById(R.id.imgPhotoEditorImage);
        final FrameLayout frmBorder = imageRootView.findViewById(R.id.frmBorder);
        final ImageView imgClose = imageRootView.findViewById(R.id.imgPhotoEditorClose);
        frmBorder.setTag(true);
        imageRootView.post(new Runnable() {
            @Override
            public void run() {
                imageRootView.setX(x);
                imageRootView.setY(y);
            }
        });
        imageRootView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(brushBitmap);
//        Log.e("width",brushBitmap.getWidth() +" " + brushBitmap.getHeight());
//        imageView.requestLayout();
//        imageView.getLayoutParams().width = brushBitmap.getWidth();
//        imageView.getLayoutParams().height = brushBitmap.getHeight();
//        Log.e("test width",imageView.getWidth() + " " + imageView.getHeight());
//        MultiTouchListener multiTouchListener = getMultiTouchListener();
//        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
//            @Override
//            public void onClick() {
//                boolean isBackgroundVisible = frmBorder.getTag() != null && (boolean) frmBorder.getTag();
//                frmBorder.setBackgroundResource(isBackgroundVisible ? 0 : R.drawable.rounded_border_tv);
//                imgClose.setVisibility(isBackgroundVisible ? View.GONE : View.VISIBLE);
//                frmBorder.setTag(!isBackgroundVisible);
//            }
//
//            @Override
//            public void onLongClick() {
//
//            }
//        });
//
//        imageRootView.setOnTouchListener(multiTouchListener);

        addViewToParent(imageRootView, imageView, path, ViewType.IMAGE);
    }


    public void reAddImage(final DraftImageBean bean, final Bitmap desiredImage, final String path) {
        final View imageRootView = getLayout(ViewType.IMAGE, path);
        final ImageView imageView = imageRootView.findViewById(R.id.imgPhotoEditorImage);
        final FrameLayout frmBorder = imageRootView.findViewById(R.id.frmBorder);
        final ImageView imgClose = imageRootView.findViewById(R.id.imgPhotoEditorClose);
        frmBorder.setTag(true);
        imageRootView.post(new Runnable() {
            @Override
            public void run() {
                final float centerX = bean.getBgWidth() * bean.getTranslationX() - imageRootView.getLeft() - imageView.getWidth() * bean.getImageWidth() / 2 - 120;
                final float centerY = bean.getBgHeight() * bean.getTranslationY() - imageRootView.getTop() - imageView.getHeight() * bean.getImageHeight() / 2 - 110;
                imageRootView.setTranslationX(centerX);
                imageRootView.setTranslationY(centerY);
                imageRootView.setScaleX(bean.getScaleX());
                imageRootView.setScaleY(bean.getScaleY());
                imageRootView.setRotation(bean.getRotation());
                imageView.requestLayout();
                imageView.getLayoutParams().width = (int) (bean.getBgWidth() * bean.getImageWidth());
                imageView.getLayoutParams().height = (int) (bean.getBgHeight() * bean.getImageHeight());
                GlideApp.with(context).load(path).into(imageView);
            }
        });

        MultiTouchListener multiTouchListener = getMultiTouchListener();
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick() {
                boolean isBackgroundVisible = frmBorder.getTag() != null && (boolean) frmBorder.getTag();
                frmBorder.setBackgroundResource(isBackgroundVisible ? 0 : R.drawable.rounded_border_tv);
                imgClose.setVisibility(isBackgroundVisible ? View.GONE : View.VISIBLE);
                frmBorder.setTag(!isBackgroundVisible);
            }

            @Override
            public void onLongClick() {

            }
        });

        imageRootView.setOnTouchListener(multiTouchListener);

        addViewToParent(imageRootView, imageView, path, ViewType.IMAGE);
    }

    /**
     * This add the text on the {@link PhotoEditorView} with provided parameters
     * by default {@link TextView#setText(int)} will be 18sp
     *
     * @param text              text to display
     * @param colorCodeTextView text color to be displayed
     */
    @SuppressLint("ClickableViewAccessibility")
    public void addText(String text, final int colorCodeTextView) {
        addText(null, text, colorCodeTextView);
    }


    /**
     * This add the text on the {@link PhotoEditorView} with provided parameters
     * by default {@link TextView#setText(int)} will be 18sp
     *
     * @param textTypeface      typeface for custom font in the text
     * @param text              text to display
     * @param colorCodeTextView text color to be displayed
     */
    @SuppressLint("ClickableViewAccessibility")
    public void addText(@Nullable Typeface textTypeface, final String text, final int colorCodeTextView) {
        brushDrawingView.setBrushDrawingMode(false);
        final View textRootView = getLayout(ViewType.TEXT, "");
        final TextView textInputTv = textRootView.findViewById(R.id.tvPhotoEditorText);
        final ImageView imgClose = textRootView.findViewById(R.id.imgPhotoEditorClose);
        final FrameLayout frmBorder = textRootView.findViewById(R.id.frmBorder);
        frmBorder.setTag(true);
        textInputTv.setDrawingCacheEnabled(true);
        textInputTv.setText(text);
        textInputTv.setTextColor(colorCodeTextView);
        textRootView.setDrawingCacheEnabled(true);
        if (textTypeface != null) {
            textInputTv.setTypeface(textTypeface);
        }
        MultiTouchListener multiTouchListener = getMultiTouchListener();
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick() {
                for (int i = 0; i < addTextViews.size(); i++) {
                    if (addTextViews.get(i).getFrameLayout() != textRootView) {
                        addTextViews.get(i).getAddFrameLayout().setBackgroundResource(0);
                        addTextViews.get(i).getAddImageClose().setVisibility(View.GONE);
                    }
                }
                boolean isBackgroundVisible = frmBorder.getTag() != null && (boolean) frmBorder.getTag();
                frmBorder.setBackgroundResource(isBackgroundVisible ? 0 : R.drawable.rounded_border_tv);
                imgClose.setVisibility(isBackgroundVisible ? View.GONE : View.VISIBLE);
                frmBorder.setTag(!isBackgroundVisible);
                String textInput = textInputTv.getText().toString();
                int currentTextColor = textInputTv.getCurrentTextColor();
                if (mOnPhotoEditorListener != null) {
                    if (!isBackgroundVisible) {
                        mOnPhotoEditorListener.onEditTextChangeListener(textRootView, textInput, currentTextColor);
                    } else {
                        mOnPhotoEditorListener.onEditTextChangeListener(null, textInput, currentTextColor);
                    }
                }
            }

            @Override
            public void onLongClick() {
//                String textInput = textInputTv.getText().toString();
//                int currentTextColor = textInputTv.getCurrentTextColor();
//                if (mOnPhotoEditorListener != null) {
//                    mOnPhotoEditorListener.onEditTextChangeListener(textRootView, textInput, currentTextColor);
//                }
            }
        });

        textRootView.setOnTouchListener(multiTouchListener);
        addViewToParent(textRootView, textInputTv, "", ViewType.TEXT);

        TextBean bean = new TextBean();
        bean.setFrameLayout(textRootView);
        bean.setTextView(textInputTv);
        bean.setAddFrameLayout(frmBorder);
        bean.setAddImageClose(imgClose);
        bean.setBitmap(textRootView.getDrawingCache());
        addTextViews.add(bean);
        mOnPhotoEditorListener.onEditTextChangeListener(textRootView, textInputTv.getText().toString(), textInputTv.getCurrentTextColor());
        for (int i = 0; i < addTextViews.size(); i++) {
            if (addTextViews.get(i).getFrameLayout() != textRootView) {
                addTextViews.get(i).getAddFrameLayout().setBackgroundResource(0);
                addTextViews.get(i).getAddImageClose().setVisibility(View.GONE);
            }
        }
    }

    public void reAddText(final DraftTextBean reAdd) {
        brushDrawingView.setBrushDrawingMode(false);
        final View textRootView = getLayout(ViewType.TEXT, "");
        final TextView textInputTv = textRootView.findViewById(R.id.tvPhotoEditorText);
        final ImageView imgClose = textRootView.findViewById(R.id.imgPhotoEditorClose);
        final FrameLayout frmBorder = textRootView.findViewById(R.id.frmBorder);
        frmBorder.setTag(true);

        textRootView.post(new Runnable() {
            @Override
            public void run() {
                final float centerX = reAdd.getBgWidth() * reAdd.getTranslationX() - textRootView.getLeft() - textRootView.getWidth() * reAdd.getWidth() / 2;
                final float centerY = reAdd.getBgHeight() * reAdd.getTranslationY() - textRootView.getTop() - textRootView.getHeight() * reAdd.getHeight() / 2;
//                Log.e("center point",centerX +" " + centerY +" " + textInputTv.getLeft() +" " + textInputTv.getTop());
                if (reAdd.getHeight() == 0 && reAdd.getWidth() == 0) {
                    textRootView.setTranslationX(reAdd.getTranslationX());
                    textRootView.setTranslationY(reAdd.getTranslationY());
                } else {
                    textRootView.setTranslationX(centerX);
                    textRootView.setTranslationY(centerY);
                }
                textRootView.setScaleX(reAdd.getScaleX());
                textRootView.setScaleY(reAdd.getScaleY());
                textRootView.setRotation(reAdd.getRotation());
                textInputTv.setText(reAdd.getText());
                textInputTv.setTextColor(reAdd.getTextColor());
                textRootView.setDrawingCacheEnabled(true);
                textInputTv.setDrawingCacheEnabled(true);
                if (reAdd.getTextFont() != null) {
                    textInputTv.setTypeface(reAdd.getTextFont());
                }
            }
        });

        MultiTouchListener multiTouchListener = getMultiTouchListener();
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick() {
                for (int i = 0; i < addTextViews.size(); i++) {
                    if (addTextViews.get(i).getFrameLayout() != textRootView) {
                        addTextViews.get(i).getAddFrameLayout().setBackgroundResource(0);
                        addTextViews.get(i).getAddImageClose().setVisibility(View.GONE);
                    }
                }
                boolean isBackgroundVisible = frmBorder.getTag() != null && (boolean) frmBorder.getTag();
                frmBorder.setBackgroundResource(isBackgroundVisible ? 0 : R.drawable.rounded_border_tv);
                imgClose.setVisibility(isBackgroundVisible ? View.GONE : View.VISIBLE);
                frmBorder.setTag(!isBackgroundVisible);
                String textInput = textInputTv.getText().toString();
                int currentTextColor = textInputTv.getCurrentTextColor();
                if (mOnPhotoEditorListener != null) {
                    if (!isBackgroundVisible) {
                        mOnPhotoEditorListener.onEditTextChangeListener(textRootView, textInput, currentTextColor);
                    } else {
                        mOnPhotoEditorListener.onEditTextChangeListener(null, textInput, currentTextColor);
                    }
                }
            }

            @Override
            public void onLongClick() {

            }
        });

        textRootView.setOnTouchListener(multiTouchListener);
        addViewToParent(textRootView, textInputTv, "", ViewType.TEXT);

        TextBean bean = new TextBean();
        bean.setFrameLayout(textRootView);
        bean.setTextView(textInputTv);
        bean.setAddFrameLayout(frmBorder);
        bean.setAddImageClose(imgClose);
        bean.setBitmap(textRootView.getDrawingCache());
        addTextViews.add(bean);
        mOnPhotoEditorListener.onEditTextChangeListener(textRootView, textInputTv.getText().toString(), textInputTv.getCurrentTextColor());
        for (int i = 0; i < addTextViews.size(); i++) {
            if (addTextViews.get(i).getFrameLayout() != textRootView) {
                addTextViews.get(i).getAddFrameLayout().setBackgroundResource(0);
                addTextViews.get(i).getAddImageClose().setVisibility(View.GONE);
            }
        }
    }


    /**
     * This will update text and color on provided view
     *
     * @param view      view on which you want update
     * @param inputText text to update {@link TextView}
     * @param colorCode color to update on {@link TextView}
     */
    public void editText(View view, String inputText, int colorCode) {
        editText(view, null, inputText, colorCode);
    }

    /**
     * This will update the text and color on provided view
     *
     * @param view         root view where text view is a child
     * @param textTypeface update typeface for custom font in the text
     * @param inputText    text to update {@link TextView}
     * @param colorCode    color to update on {@link TextView}
     */
    public void editText(View view, Typeface textTypeface, String inputText, int colorCode) {
        if (view != null) {
            TextView inputTextView = view.findViewById(R.id.tvPhotoEditorText);
            for (int i = 0; i < addedViews.size(); i++) {
                if (inputTextView != null && addedViews.get(i).getView() == view && !TextUtils.isEmpty(inputText)) {
                    inputTextView.setText(inputText);
                    if (textTypeface != null) {
                        inputTextView.setTypeface(textTypeface);
                    }
                    inputTextView.setTextColor(colorCode);
                    parentView.updateViewLayout(view, view.getLayoutParams());
//                int i = addedViews.indexOf(view);
                    addedViews.set(i, new AddViewBean(view, inputTextView, "", ViewType.TEXT));
                }
            }
        } else {
            ToastUtils.showShort(context, "view is null");
        }


    }

    /**
     * Adds emoji to the {@link PhotoEditorView} which you drag,rotate and scale using pinch
     * if {@link PhotoEditor.Builder#setPinchTextScalable(boolean)} enabled
     *
     * @param emojiName
     */
    public void addEmoji(String emojiName) {
        addEmoji(null, emojiName);
    }

    /**
     * Adds emoji to the {@link PhotoEditorView} which you drag,rotate and scale using pinch
     * if {@link PhotoEditor.Builder#setPinchTextScalable(boolean)} enabled
     *
     * @param emojiTypeface typeface for custom font to show emoji unicode in specific font
     * @param emojiName     unicode in form of string to display emoji
     */
    public void addEmoji(Typeface emojiTypeface, String emojiName) {
        brushDrawingView.setBrushDrawingMode(false);
        final View emojiRootView = getLayout(ViewType.EMOJI, "");
        final TextView emojiTextView = emojiRootView.findViewById(R.id.tvPhotoEditorText);
        final FrameLayout frmBorder = emojiRootView.findViewById(R.id.frmBorder);
        final ImageView imgClose = emojiRootView.findViewById(R.id.imgPhotoEditorClose);

        if (emojiTypeface != null) {
            emojiTextView.setTypeface(emojiTypeface);
        }
        emojiTextView.setTextSize(56);
        emojiTextView.setText(emojiName);
        MultiTouchListener multiTouchListener = getMultiTouchListener();
        multiTouchListener.setOnGestureControl(new MultiTouchListener.OnGestureControl() {
            @Override
            public void onClick() {
                boolean isBackgroundVisible = frmBorder.getTag() != null && (boolean) frmBorder.getTag();
                frmBorder.setBackgroundResource(isBackgroundVisible ? 0 : R.drawable.rounded_border_tv);
                imgClose.setVisibility(isBackgroundVisible ? View.GONE : View.VISIBLE);
                frmBorder.setTag(!isBackgroundVisible);
            }

            @Override
            public void onLongClick() {
            }
        });
        emojiRootView.setOnTouchListener(multiTouchListener);
        addViewToParent(emojiRootView, emojiTextView, "", ViewType.EMOJI);
    }


    /**
     * Add to root view from image,emoji and text to our parent view
     *
     * @param rootView rootview of image,text and emoji
     */
    private void addViewToParent(View rootView, View addView, String path, ViewType viewType) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        parentView.addView(rootView, params);
        addedViews.add(new AddViewBean(rootView, addView, path, viewType));

        if (mOnPhotoEditorListener != null)
            mOnPhotoEditorListener.onAddViewListener(viewType, addedViews.size());
    }

    /**
     * Create a new instance and scalable touchview
     *
     * @return scalable multitouch listener
     */
    @NonNull
    private MultiTouchListener getMultiTouchListener() {
        MultiTouchListener multiTouchListener = new MultiTouchListener(
                deleteView,
                parentView,
                this.imageView,
                isTextPinchZoomable,
                mOnPhotoEditorListener);

        //multiTouchListener.setOnMultiTouchListener(this);

        return multiTouchListener;
    }

    /**
     * Get root view by its type i.e image,text and emoji
     *
     * @param viewType image,text or emoji
     * @return rootview
     */
    private View getLayout(final ViewType viewType, final String path) {
        View rootView = null;
        View addView = null;
        switch (viewType) {
            case TEXT:
                rootView = mLayoutInflater.inflate(R.layout.view_photo_editor_text, null);
                TextView txtText = rootView.findViewById(R.id.tvPhotoEditorText);
                addView = txtText;
                if (txtText != null && mDefaultTextTypeface != null) {
                    txtText.setGravity(Gravity.CENTER);
                    if (mDefaultEmojiTypeface != null) {
                        txtText.setTypeface(mDefaultTextTypeface);
                    }
                }
                break;
            case IMAGE:
                rootView = mLayoutInflater.inflate(R.layout.view_photo_editor_image, null);
                addView = rootView.findViewById(R.id.imgPhotoEditorImage);
                break;
            case EMOJI:
                rootView = mLayoutInflater.inflate(R.layout.view_photo_editor_text, null);
                TextView txtTextEmoji = rootView.findViewById(R.id.tvPhotoEditorText);
                if (txtTextEmoji != null) {
                    if (mDefaultEmojiTypeface != null) {
                        txtTextEmoji.setTypeface(mDefaultEmojiTypeface);
                    }
                    txtTextEmoji.setGravity(Gravity.CENTER);
                    txtTextEmoji.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                }
                break;
        }

        if (rootView != null) {
            final ImageView imgClose = rootView.findViewById(R.id.imgPhotoEditorClose);
            final View finalRootView = rootView;
            final View finalAddView = addView;
            if (imgClose != null) {
                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(path)) {
                            viewUndo(finalRootView, finalAddView, path, viewType);
                        }
                    }
                });
            }
        }
        return rootView;
    }

    /**
     * Enable/Disable drawing mode to draw on {@link PhotoEditorView}
     *
     * @param brushDrawingMode true if mode is enabled
     */
    public void setBrushDrawingMode(boolean brushDrawingMode) {
        if (brushDrawingView != null)
            brushDrawingView.setBrushDrawingMode(brushDrawingMode);
    }

    /**
     * @return true is brush mode is enabled
     */
    public Boolean getBrushDrawableMode() {
        return brushDrawingView != null && brushDrawingView.getBrushDrawingMode();
    }

    /**
     * set the size of bursh user want to paint on canvas i.e {@link BrushDrawingView}
     *
     * @param size size of brush
     */
    public void setBrushSize(float size) {
        if (brushDrawingView != null)
            brushDrawingView.setBrushSize(size);
    }

    /**
     * set opacity/transparency of brush while painting on {@link BrushDrawingView}
     *
     * @param opacity opacity is in form of percentage
     */
    public void setOpacity(@IntRange(from = 0, to = 100) int opacity) {
        if (brushDrawingView != null) {
            opacity = (int) ((opacity / 100.0) * 255.0);
            brushDrawingView.setOpacity(opacity);
        }
    }

    /**
     * set brush color which user want to paint
     *
     * @param color color value for paint
     */
    public void setBrushColor(@ColorInt int color) {
        if (brushDrawingView != null)
            brushDrawingView.setBrushColor(color);
    }

    /**
     * set the eraser size
     * <br></br>
     * <b>Note :</b> Eraser size is different from the normal brush size
     *
     * @param brushEraserSize size of eraser
     */
    public void setBrushEraserSize(float brushEraserSize) {
        if (brushDrawingView != null)
            brushDrawingView.setBrushEraserSize(brushEraserSize);
    }

    void setBrushEraserColor(@ColorInt int color) {
        if (brushDrawingView != null)
            brushDrawingView.setBrushEraserColor(color);
    }

    /**
     * @return provide the size of eraser
     * @see PhotoEditor#setBrushEraserSize(float)
     */
    public float getEraserSize() {
        return brushDrawingView != null ? brushDrawingView.getEraserSize() : 0;
    }

    /**
     * @return provide the size of eraser
     * @see PhotoEditor#setBrushSize(float)
     */
    public float getBrushSize() {
        if (brushDrawingView != null)
            return brushDrawingView.getBrushSize();
        return 0;
    }

    /**
     * @return provide the size of eraser
     * @see PhotoEditor#setBrushColor(int)
     */
    public int getBrushColor() {
        if (brushDrawingView != null)
            return brushDrawingView.getBrushColor();
        return 0;
    }

    /**
     * <p>
     * Its enables eraser mode after that whenever user drags on screen this will erase the existing
     * paint
     * <br>
     * <b>Note</b> : This eraser will work on paint views only
     * <p>
     */
    public void brushEraser() {
        if (brushDrawingView != null)
            brushDrawingView.brushEraser();
    }

    private void viewUndo() {
        if (addedViews.size() > 0) {
            AddViewBean bean = addedViews.get(addedViews.size() - 1);
            parentView.removeView(bean.getView());
            addedViews.remove(bean);
//            parentView.removeView(addedViews.remove(addedViews.size() - 1));
            if (mOnPhotoEditorListener != null)
                mOnPhotoEditorListener.onRemoveViewListener(addedViews.size());
        }
    }

    private void viewUndo(View removedView, View addView, String path, ViewType type) {
        if (addedViews.size() > 0) {
            for (int i = 0; i < addedViews.size(); i++) {
                if (addedViews.get(i).getView() == removedView) {
                    parentView.removeView(removedView);
                    addedViews.remove(addedViews.get(i));
                    redoViews.add(new AddViewBean(removedView, addView, "", type));
//                redoViews.add(removedView);
                    if (mOnPhotoEditorListener != null)
                        mOnPhotoEditorListener.onRemoveViewListener(addedViews.size());
                }
            }
//            if (addedViews.contains(removedView)) {
//                parentView.removeView(removedView);
//                addedViews.remove(removedView);
//                redoViews.add(new AddViewBean(removedView,type));
////                redoViews.add(removedView);
//                if (mOnPhotoEditorListener != null)
//                    mOnPhotoEditorListener.onRemoveViewListener(addedViews.size());
//            }
        }
    }

    /**
     * Undo the last operation perform on the {@link PhotoEditor}
     *
     * @return true if there nothing more to undo
     */
    public boolean undo() {
        if (addedViews.size() > 0) {
            AddViewBean bean = addedViews.get(addedViews.size() - 1);
            View removeView = bean.getView();
            ViewType type = bean.getType();
            View addView = bean.getAddView();
            String path = bean.getChildImagePath();
//            View removeView = addedViews.get(addedViews.size() - 1);
            if (removeView instanceof BrushDrawingView) {
                return brushDrawingView != null && brushDrawingView.undo();
            } else {
                addedViews.remove(addedViews.size() - 1);
                parentView.removeView(removeView);
                redoViews.add(new AddViewBean(removeView, addView, path, type));
            }
            if (mOnPhotoEditorListener != null) {
                mOnPhotoEditorListener.onRemoveViewListener(addedViews.size());
            }
        }
        return addedViews.size() != 0;
    }

    /**
     * Redo the last operation perform on the {@link PhotoEditor}
     *
     * @return true if there nothing more to redo
     */
    public boolean redo() {
        if (redoViews.size() > 0) {
            AddViewBean bean = redoViews.get(redoViews.size() - 1);
            View redoView = bean.getView();
            ViewType type = bean.getType();
            View addView = bean.getAddView();
            String path = bean.getChildImagePath();
//            View redoView = redoViews.get(redoViews.size() - 1);
            if (redoView instanceof BrushDrawingView) {
                return brushDrawingView != null && brushDrawingView.redo();
            } else {
                redoViews.remove(redoViews.size() - 1);
                parentView.addView(redoView);
                addedViews.add(new AddViewBean(redoView, addView, path, type));
            }
        }
        return redoViews.size() != 0;
    }

    public void clearBrushAllViews() {

        if (brushDrawingView != null) {
            for (int i = 0; i < addedViews.size(); i++) {
                if (addedViews.get(i).getType() == ViewType.BRUSH_DRAWING) {
                    addedViews.remove(addedViews.get(i));
                }
            }
            brushDrawingView.clearAll();
            brushDrawingView.clearTouchPoint();
        }
    }

    /**
     * Removes all the edited operations performed {@link PhotoEditorView}
     * This will also clear the undo and redo stack
     */
    public void clearAllViews() {
        for (int i = 0; i < addedViews.size(); i++) {
            AddViewBean bean = addedViews.get(i);
            parentView.removeView(bean.getView());
            if (bean.getView() == brushDrawingView) {
                parentView.addView(brushDrawingView);
            }
        }

        addedViews.clear();
        redoViews.clear();
        clearBrushAllViews();
    }


    /**
     * Remove all helper boxes from text
     */
    @UiThread
    private void clearTextHelperBox() {
        for (int i = 0; i < parentView.getChildCount(); i++) {
            View childAt = parentView.getChildAt(i);
            FrameLayout frmBorder = childAt.findViewById(R.id.frmBorder);
            if (frmBorder != null) {
                frmBorder.setBackgroundResource(0);
            }
            ImageView imgClose = childAt.findViewById(R.id.imgPhotoEditorClose);
            if (imgClose != null) {
                imgClose.setVisibility(View.GONE);
            }
        }
    }


    public interface OnDecodeImageListener {
        void onDecodeSuccess(List<String> path);

        void onDecodeFailed(Exception e);
    }

    public interface OnDecodeGifStickerListener {
        void onDecodeStickerSuccess(List<List<BitmapBean>> decodeList, List<Bitmap> bgBitmapList);

        void onDecodeStickerFailed(Exception e);
    }


    public interface AddBrushImageListener {
        void addBrushImageSuccess();
    }

    public static Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(),
                orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        resizedBitmap.setConfig(Bitmap.Config.ARGB_8888);
        return resizedBitmap;
    }


    @SuppressLint("StaticFieldLeak")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void generateImage(final int width, final int height, final String bgImagePath, final OnDecodeImageListener listener) {
        final List<String> pathList = new ArrayList<>();
        new AsyncTask<String, String, Exception>() {
            @Override
            protected void onPreExecute() {
                Log.v("photoEditor", "==================== pre to merge ===================================");
                clearTextHelperBox();
                parentView.setDrawingCacheEnabled(false);
            }

            @SuppressLint("MissingPermission")
            @Override
            protected Exception doInBackground(String... strings) {
                Log.v("photoEditor", "==================== start to merge ===================================");
                final List<Integer> frameList = new ArrayList<>();
                final List<Integer> delayList = new ArrayList<>();
                final List<AddViewBean> gifImageBeans = new ArrayList<>();//gif贴图集合
                final List<AddViewBean> staticImageBeans = new ArrayList<>();//静态贴图集合
                final List<AddViewBean> textBeans = new ArrayList<>();//文本集合
                final List<AddViewBean> brushBeans = new ArrayList<>();//画笔集合
                final List<BitmapBean> bgFrames = new ArrayList<>();//gif背景帧视图
                final List<List<BitmapBean>> stickerFrmaes = new ArrayList<>();//gif贴图帧视图
                for (int i = 0; i < addedViews.size(); i++) {
                    AddViewBean addViewBean = addedViews.get(i);
                    if (addViewBean.getType() == ViewType.IMAGE) {
                        if (addViewBean.getChildImagePath().contains(".gif") || addViewBean.getChildImagePath().contains(".GIF")) {//贴图是否包含gif
                            gifImageBeans.add(addViewBean);
                        } else {
                            staticImageBeans.add(addViewBean);
                        }
                    } else if (addViewBean.getType() == ViewType.TEXT) {
                        textBeans.add(addViewBean);
                    } else {
                        brushBeans.add(addViewBean);
                    }
                }

                if (bgImagePath.contains(".gif") || bgImagePath.contains(".GIF") || gifImageBeans.size() > 0) {//包含gif
                    if ((bgImagePath.contains(".gif") || bgImagePath.contains(".GIF")) && gifImageBeans.size() > 0) {
                        //bg 解码
                        bgFrames.addAll(decodeGifImage(bgImagePath, null, null, delayList));
                        frameList.add(bgFrames.size());
                        //sticker 解码
                        for (int i = 0; i < gifImageBeans.size(); i++) {
                            List<BitmapBean> tempBean = new ArrayList<>();
                            tempBean.addAll(decodeGifImage(gifImageBeans.get(i).getChildImagePath(), gifImageBeans.get(i).getView(), (ImageView) gifImageBeans.get(i).getAddView(), delayList));
                            stickerFrmaes.add(tempBean);
                            frameList.add(tempBean.size());
                        }

                    } else if ((bgImagePath.contains(".gif") || bgImagePath.contains(".GIF")) && gifImageBeans.size() == 0) {
                        //bg 解码
                        bgFrames.addAll(decodeGifImage(bgImagePath, null, null, delayList));
                        frameList.add(bgFrames.size());
                    } else if (!(bgImagePath.contains(".gif") || bgImagePath.contains(".GIF")) && gifImageBeans.size() > 0) {
                        //sticker解码
                        bgFrames.addAll(decodeStaticImage(bgImagePath, null, null));
                        for (int i = 0; i < gifImageBeans.size(); i++) {
                            List<BitmapBean> tempBean = new ArrayList<>();
                            tempBean.addAll(decodeGifImage(gifImageBeans.get(i).getChildImagePath(), gifImageBeans.get(i).getView(), (ImageView) gifImageBeans.get(i).getAddView(), delayList));
                            stickerFrmaes.add(tempBean);
                            frameList.add(tempBean.size());
                        }
                    }
                } else {//无gif图
                    bgFrames.addAll(decodeStaticImage(bgImagePath, null, null));
                }
                for (int i = 0; i < staticImageBeans.size(); i++) {//静态贴图
                    stickerFrmaes.add(decodeStaticImage(staticImageBeans.get(i).getChildImagePath(), staticImageBeans.get(i).getView(), (ImageView) staticImageBeans.get(i).getAddView()));
                }

                for (int i = 0; i < textBeans.size(); i++) {//文本
                    stickerFrmaes.add(decodeTextImage(textBeans.get(i).getAddView().getDrawingCache(), textBeans.get(i).getView(), (TextView) textBeans.get(i).getAddView()));
                }

                for (int i = 0; i < brushBeans.size(); i++) {
                    BrushDrawingView view = (BrushDrawingView) brushBeans.get(i).getView();
                    Bitmap bitmap = view.generateBimap();
                    if (bitmap != null) {
                        EventBus.getDefault().post(new MakerEventMsg(4, view.getMinTouchX(), view.getMinTouchY(), bitmap));
                        stickerFrmaes.add(decodeBrushImage(bitmap, view));
                    } else {
                        return new Exception();
                    }
                }
                if(delayList.size()> 0) {
                    String gifPath = App.path1 + "/"
                            + System.currentTimeMillis() + ".gif";
                    pathList.add(gifPath);
                    generateGif(mergeGifStickerBitmap(bgFrames, stickerFrmaes, width, height, frameList), gifPath, Collections.min(delayList));
                }else {
                    String staticPath = App.path1 + "/" + System.currentTimeMillis() + ".png";
                    pathList.add(staticPath);
                    generateGif(mergeGifStickerBitmap(bgFrames, stickerFrmaes, width, height, frameList), staticPath, 0);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Exception e) {
                if (e == null) {
                    listener.onDecodeSuccess(pathList);
                } else {
                    listener.onDecodeFailed(e);
                }
            }
        }.execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String generateGif(List<BitmapBean> bitmap, String path, int delay) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
        localAnimatedGifEncoder.start(baos);//start
        localAnimatedGifEncoder.setRepeat(0);//设置生成gif的开始播放时间。0为立即开始播放
        for (int i = 0; i < bitmap.size(); i++) {
            localAnimatedGifEncoder.setDelay(delay);
            Bitmap resizeBm = drawBg4Bitmap(Color.WHITE, resizeImage(bitmap.get(i).getBitmap(), 220, 220));
//                        localAnimatedGifEncoder.setTransparent(Color.BLACK);
            localAnimatedGifEncoder.addFrame(resizeBm);
        }
        localAnimatedGifEncoder.finish();

        try {
            FileOutputStream fos = new FileOutputStream(path);
            baos.writeTo(fos);
            baos.flush();
            fos.flush();
            baos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("photoEditor", "==================== merge error =================================== \n" + e.getMessage());
        }
        return path;
    }

    private List<BitmapBean> mergeGifStickerBitmap(List<BitmapBean> bgFrame, List<List<BitmapBean>> stickerFrame, int width, int height, List<Integer> frame) {
        List<BitmapBean> bitmapList = new ArrayList<>();
        int maxFrame = 1;
        if (frame.size() > 0) {
            maxFrame = Collections.max(frame);
            if (maxFrame == 1 || maxFrame == 0) {
                maxFrame = 1;
            }
        }

        for (int i = 0; i < maxFrame; i++) {
            BitmapBean tb = new BitmapBean();
            BitmapBean bgBitmap = null;
            if (bgFrame.size() == 1) {
                bgBitmap = bgFrame.get(0);
            } else {
                if (bgFrame.size() > 0) {
                    bgBitmap = bgFrame.get(i % bgFrame.size());
                }
            }
            if (bgBitmap != null) {
                float scaleWidth = ((float) width) / bgBitmap.getBitmap().getWidth();
                float scaleHeight = ((float) height) / bgBitmap.getBitmap().getHeight();
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                Bitmap bmOverlay = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bmOverlay);
                canvas.drawBitmap(bgBitmap.getBitmap(), matrix, null);
                if (stickerFrame.size() > 0) {
                    for (int j = 0; j < stickerFrame.size(); j++) {
                        List<BitmapBean> beans = stickerFrame.get(j);
                        BitmapBean bean = null;
                        if (beans.size() == 1) {
                            bean = beans.get(0);
                        } else {
                            bean = beans.get(i % beans.size());
                        }
                        float ftscaleWidth = ((float) bean.getWidth()) / bean.getBitmap().getWidth();
                        float ftscaleHeight = ((float) bean.getHeight()) / bean.getBitmap().getHeight();
                        Matrix frontMx = new Matrix();
                        frontMx.postRotate(bean.getRotation());
                        frontMx.postScale(ftscaleWidth, ftscaleHeight);
                        Bitmap ftBitmap = Bitmap.createBitmap(bean.getBitmap(), 0, 0, bean.getBitmap().getWidth(), bean.getBitmap().getHeight(), frontMx, true);
                        if (bean.getType() == ViewType.BRUSH_DRAWING) {
                            canvas.drawBitmap(ftBitmap, bean.getCenterX(), bean.getCenterY(), null);
                        } else {
                            canvas.drawBitmap(ftBitmap, bean.getCenterX() - ftBitmap.getWidth() / 2, bean.getCenterY() - ftBitmap.getHeight() / 2, null);
                        }
                        tb.setBitmap(bmOverlay);
                        tb.setDelay(bean.getDelay());
                        bitmapList.add(tb);
                    }
                } else {
                    tb.setBitmap(bmOverlay);
                    tb.setDelay(bgBitmap.getDelay());
                    bitmapList.add(tb);
                }
            }

        }
        return bitmapList;
    }

    private List<BitmapBean> decodeBrushImage(Bitmap bitmap, BrushDrawingView parent) {
        List<BitmapBean> bitmaps = new ArrayList<>();
        BitmapBean bean = new BitmapBean();
        bean.setBitmap(bitmap);
        bean.setWidth(bitmap.getWidth());
        bean.setHeight(bitmap.getHeight());
        bean.setRotation(parent.getRotation());
        bean.setCenterX(parent.getMinTouchX());
        bean.setCenterY(parent.getMinTouchY());
        bean.setType(ViewType.BRUSH_DRAWING);
        bitmaps.add(bean);
        return bitmaps;
    }

    private List<BitmapBean> decodeTextImage(Bitmap bitmap, View parent, TextView child) {
        List<BitmapBean> bitmaps = new ArrayList<>();
        BitmapBean bean = new BitmapBean();
        bean.setBitmap(bitmap);
        float centerX = ((float) parent.getWidth()) / 2 + parent.getLeft() + parent.getTranslationX();
        float centerY = ((float) parent.getHeight()) / 2 + parent.getTop() + parent.getTranslationY();
        bean.setWidth((int) (child.getWidth() * parent.getScaleX()));
        bean.setHeight((int) (child.getHeight() * parent.getScaleY()));
        bean.setRotation(parent.getRotation());
        bean.setCenterX(centerX);
        bean.setCenterY(centerY);
        bean.setType(ViewType.TEXT);
        bitmaps.add(bean);
        return bitmaps;
    }


    private List<BitmapBean> decodeStaticImage(String path, View parent, ImageView child) {
        RequestManager rc = GlideApp.with(context);
        FutureTarget<File> cacheFile = null;
        File tf = null;
        List<BitmapBean> bitmaps = new ArrayList<>();
        if (path.contains("http:") || path.contains("https:")) {
            cacheFile = rc.downloadOnly().load(path).submit();
        } else {
            cacheFile = rc.downloadOnly().load(new File(path)).submit();
        }

        try {
            tf = cacheFile.get();
            Bitmap bitmap = BitmapFactory.decodeFile(tf.getPath());
            BitmapBean bean = new BitmapBean();
            bean.setBitmap(bitmap);
            bean.setDelay(0);
            if (parent != null && child != null) {
                float centerX = ((float) parent.getWidth()) / 2 + parent.getLeft() + parent.getTranslationX();
                float centerY = ((float) parent.getHeight()) / 2 + parent.getTop() + parent.getTranslationY();
                bean.setWidth((int) (child.getWidth() * parent.getScaleX()));
                bean.setHeight((int) (child.getHeight() * parent.getScaleY()));
                bean.setRotation(parent.getRotation());
                bean.setCenterX(centerX);
                bean.setCenterY(centerY);
                bean.setType(ViewType.IMAGE);
            }
            bitmaps.add(bean);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmaps;
    }

    private List<BitmapBean> decodeGifImage(String path, View parent, ImageView child, List<Integer> integers) {
        RequestManager rc = GlideApp.with(context);
        FutureTarget<File> cacheFile = null;
        File tf = null;
        List<BitmapBean> bitmaps = new ArrayList<>();
        if (path.contains("http") || path.contains("https")) {
            cacheFile = rc.downloadOnly().load(path).submit();
        } else {
            cacheFile = rc.downloadOnly().load(new File(path)).submit();
        }

        try {
            tf = cacheFile.get();
            GifDecoder gifDecoder = new GifDecoder();
            final GifImageIterator iterator = gifDecoder.loadUsingIterator(tf.getPath());
            float centerX = 0;
            float centerY = 0;
            if (parent != null && child != null) {
                centerX = ((float) parent.getWidth()) / 2 + parent.getLeft() + parent.getTranslationX();
                centerY = ((float) parent.getHeight()) / 2 + parent.getTop() + parent.getTranslationY();
            }

            if (iterator != null) {
                while (iterator.hasNext()) {
                    GifImage next = iterator.next();
                    if (null != next) {
                        BitmapBean bean = new BitmapBean();
                        bean.setBitmap(next.bitmap);
                        bean.setDelay(next.delayMs);
                        if (parent != null && child != null) {
                            bean.setWidth((int) (child.getWidth() * parent.getScaleX()));
                            bean.setHeight((int) (child.getHeight() * parent.getScaleY()));
                            bean.setRotation(parent.getRotation());
                            bean.setCenterX(centerX);
                            bean.setCenterY(centerY);
                            bean.setType(ViewType.IMAGE);
                        }
                        bitmaps.add(bean);
                        integers.add(next.delayMs);
                    }
                }
                iterator.close();
            }

            if (bitmaps.size() == 0) {
                BitmapBean bean = new BitmapBean();
                Bitmap bitmap = BitmapFactory.decodeFile(tf.getPath());
                bean.setBitmap(bitmap);
                bean.setDelay(0);
                if (parent != null && child != null) {
                    bean.setWidth((int) (child.getWidth() * parent.getScaleX()));
                    bean.setHeight((int) (child.getHeight() * parent.getScaleY()));
                    bean.setRotation(parent.getRotation());
                    bean.setCenterX(centerX);
                    bean.setCenterY(centerY);
                    bean.setType(ViewType.IMAGE);
                }
                bitmaps.add(bean);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("PhotoEditor", "Gif decode error " + e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.e("PhotoEditor", "Gif decode error " + e.getMessage());
        }
        return bitmaps;
    }

    private static String convertEmoji(String emoji) {
        String returnedEmoji;
        try {
            int convertEmojiToInt = Integer.parseInt(emoji.substring(2), 16);
            returnedEmoji = getEmojiByUnicode(convertEmojiToInt);
        } catch (NumberFormatException e) {
            returnedEmoji = "";
        }
        return returnedEmoji;
    }

    private static String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    /**
     * Callback on editing operation perform on {@link PhotoEditorView}
     *
     * @param onPhotoEditorListener {@link OnPhotoEditorListener}
     */
    public void setOnPhotoEditorListener(@NonNull OnPhotoEditorListener onPhotoEditorListener) {
        this.mOnPhotoEditorListener = onPhotoEditorListener;
    }

    /**
     * Check if any changes made need to save
     *
     * @return true if nothing is there to change
     */
    public boolean isCacheEmpty() {
        return addedViews.size() == 0 && redoViews.size() == 0;
    }

    public List<AddViewBean> getAddedViews() {
        return addedViews;
    }

    @Override
    public void onViewAdd(BrushDrawingView brushDrawingView, ViewType type) {
        if (redoViews.size() > 0) {
            redoViews.remove(redoViews.size() - 1);
        }
        addedViews.add(new AddViewBean(brushDrawingView, null, "", type));
        if (mOnPhotoEditorListener != null) {
            mOnPhotoEditorListener.onAddViewListener(ViewType.BRUSH_DRAWING, addedViews.size());
        }

    }

    @Override
    public void onViewRemoved(BrushDrawingView brushDrawingView) {
        if (addedViews.size() > 0) {
            AddViewBean bean = addedViews.get(addedViews.size() - 1);
            View removeView = bean.getView();
            ViewType type = bean.getType();
            View addView = bean.getAddView();
            String path = bean.getChildImagePath();
            addedViews.remove(bean);
//            View removeView = addedViews.remove(addedViews.size() - 1);
            if (!(removeView instanceof BrushDrawingView)) {
                parentView.removeView(removeView);
            }
            redoViews.add(new AddViewBean(removeView, addView, path, type));
        }
        if (mOnPhotoEditorListener != null) {
            mOnPhotoEditorListener.onRemoveViewListener(addedViews.size());
        }
    }

    @Override
    public void onStartDrawing() {
        if (mOnPhotoEditorListener != null) {
            mOnPhotoEditorListener.onStartViewChangeListener(ViewType.BRUSH_DRAWING);
        }
    }

    @Override
    public void onStopDrawing() {
        if (mOnPhotoEditorListener != null) {
            mOnPhotoEditorListener.onStopViewChangeListener(ViewType.BRUSH_DRAWING);
        }
    }

    /**
     * Builder pattern to define {@link PhotoEditor} Instance
     */
    public static class Builder {

        private Context context;
        private RelativeLayout parentView;
        private ImageView imageView;
        private View deleteView;
        private BrushDrawingView brushDrawingView;
        private Typeface textTypeface;
        private Typeface emojiTypeface;
        //By Default pinch zoom on text is enabled
        private boolean isTextPinchZoomable = true;

        /**
         * Building a PhotoEditor which requires a Context and PhotoEditorView
         * which we have setup in our xml layout
         *
         * @param context         context
         * @param photoEditorView {@link PhotoEditorView}
         */
        public Builder(Context context, PhotoEditorView photoEditorView) {
            this.context = context;
            parentView = photoEditorView;
            imageView = photoEditorView.getSource();
            brushDrawingView = photoEditorView.getBrushDrawingView();
        }

        Builder setDeleteView(View deleteView) {
            this.deleteView = deleteView;
            return this;
        }

        /**
         * set default text font to be added on image
         *
         * @param textTypeface typeface for custom font
         * @return {@link Builder} instant to build {@link PhotoEditor}
         */
        public Builder setDefaultTextTypeface(Typeface textTypeface) {
            this.textTypeface = textTypeface;
            return this;
        }

        /**
         * set default font specific to add emojis
         *
         * @param emojiTypeface typeface for custom font
         * @return {@link Builder} instant to build {@link PhotoEditor}
         */
        public Builder setDefaultEmojiTypeface(Typeface emojiTypeface) {
            this.emojiTypeface = emojiTypeface;
            return this;
        }

        /**
         * set false to disable pinch to zoom on text insertion.By deafult its true
         *
         * @param isTextPinchZoomable flag to make pinch to zoom
         * @return {@link Builder} instant to build {@link PhotoEditor}
         */
        public Builder setPinchTextScalable(boolean isTextPinchZoomable) {
            this.isTextPinchZoomable = isTextPinchZoomable;
            return this;
        }

        Builder setBrushDrawingView(BrushDrawingView brushDrawingView) {
            this.brushDrawingView = brushDrawingView;
            return this;
        }

        /**
         * @return build PhotoEditor instance
         */
        public PhotoEditor build() {
            return new PhotoEditor(this);
        }
    }

    /**
     * Provide the list of emoji in form of unicode string
     *
     * @param context context
     * @return list of emoji unicode
     */
    public static ArrayList<String> getEmojis(Context context) {
        ArrayList<String> convertedEmojiList = new ArrayList<>();
        String[] emojiList = context.getResources().getStringArray(R.array.photo_editor_emoji);
        for (String emojiUnicode : emojiList) {
            convertedEmojiList.add(convertEmoji(emojiUnicode));
        }
        return convertedEmojiList;
    }
}
