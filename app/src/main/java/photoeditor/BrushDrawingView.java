package photoeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.pufei.gxdt.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * This is custom drawing view used to do painting on user touch events it it will paint on canvas
 * as per attributes provided to the paint
 * </p>
 *
 * @author <a href="https://github.com/burhanrashid52">Burhanuddin Rashid</a>
 * @version 0.1.1
 * @since 12/1/18
 */
public class BrushDrawingView extends View {

    private float mBrushSize = 25;
    private float mBrushEraserSize = 50;
    private int mOpacity = 255;

    private List<LinePath> mLinePaths = new ArrayList<>();
    private List<LinePath> mRedoLinePaths = new ArrayList<>();
    private Paint mDrawPaint;

    private Canvas mDrawCanvas;
    private boolean mBrushDrawMode = false;

    private Path mPath;
    private float mTouchX, mTouchY;
    private static final float TOUCH_TOLERANCE = 4;
    private List<Float> mTouchXList = new ArrayList<>();
    private List<Float> mTouchYList = new ArrayList<>();
    private List<Bitmap> bitmapList = new ArrayList<>();
    private BrushViewChangeListener mBrushViewChangeListener;

    public BrushDrawingView(Context context) {
        this(context, null);
    }

    public BrushDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupBrushDrawing();
    }

    public BrushDrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupBrushDrawing();
    }

    private void setupBrushDrawing() {
        //Caution: This line is to disable hardware acceleration to make eraser feature work properly
        setLayerType(LAYER_TYPE_HARDWARE, null);
        mDrawPaint = new Paint();
        mPath = new Path();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setDither(true);
        mDrawPaint.setColor(Color.BLACK);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawPaint.setStrokeWidth(mBrushSize);
        mDrawPaint.setAlpha(mOpacity);
        mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
        this.setVisibility(View.GONE);
    }

    private void refreshBrushDrawing() {
        mBrushDrawMode = true;
        mPath = new Path();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setDither(true);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawPaint.setStrokeWidth(mBrushSize);
        mDrawPaint.setAlpha(mOpacity);
        mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
    }

    void brushEraser() {
        mBrushDrawMode = true;
        mDrawPaint.setStrokeWidth(mBrushEraserSize);
        mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    void setBrushDrawingMode(boolean brushDrawMode) {
        this.mBrushDrawMode = brushDrawMode;
        if (brushDrawMode) {
            this.setVisibility(View.VISIBLE);
            refreshBrushDrawing();
        }
    }

    void setOpacity(@IntRange(from = 0, to = 255) int opacity) {
        this.mOpacity = opacity;
        setBrushDrawingMode(true);
    }

    boolean getBrushDrawingMode() {
        return mBrushDrawMode;
    }

    void setBrushSize(float size) {
        mBrushSize = size;
        setBrushDrawingMode(true);
    }

    void setBrushColor(@ColorInt int color) {
        mDrawPaint.setColor(color);
        setBrushDrawingMode(true);
    }

    void setBrushEraserSize(float brushEraserSize) {
        this.mBrushEraserSize = brushEraserSize;
        setBrushDrawingMode(true);
    }

    void setBrushEraserColor(@ColorInt int color) {
        mDrawPaint.setColor(color);
        setBrushDrawingMode(true);
    }

    float getEraserSize() {
        return mBrushEraserSize;
    }

    float getBrushSize() {
        return mBrushSize;
    }

    int getBrushColor() {
        return mDrawPaint.getColor();
    }

    void clearAll() {
        mLinePaths.clear();
        mRedoLinePaths.clear();
        if (mDrawCanvas != null) {
            mDrawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }
        invalidate();
    }

    void setBrushViewChangeListener(BrushViewChangeListener brushViewChangeListener) {
        mBrushViewChangeListener = brushViewChangeListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(w != 0 && h != 0) {
            Bitmap canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mDrawCanvas = new Canvas(canvasBitmap);
            bitmapList.add(canvasBitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (LinePath linePath : mLinePaths) {
            canvas.drawPath(linePath.getDrawPath(), linePath.getDrawPaint());
        }
        canvas.drawPath(mPath, mDrawPaint);
    }

    /**
     * Handle touch event to draw paint on canvas i.e brush drawing
     *
     * @param event points having touch info
     * @return true if handling touch events
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (mBrushDrawMode) {
            float touchX = event.getX();
            float touchY = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchXList.add(touchX);
                    mTouchYList.add(touchY);
                    touchStart(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mTouchXList.add(touchX);
                    mTouchYList.add(touchY);
                    touchMove(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    mTouchXList.add(touchX);
                    mTouchYList.add(touchY);
                    touchUp();
                    break;
            }
            invalidate();
            return true;
        } else {
            return false;
        }
    }

    private class LinePath {
        private Paint mDrawPaint;
        private Path mDrawPath;
        private float touchX;
        private float touchY;

        LinePath(Path drawPath, Paint drawPaints,float touchX,float touchY) {
            mDrawPaint = new Paint(drawPaints);
            mDrawPath = new Path(drawPath);
            this.touchX = touchX;
            this.touchY = touchY;
        }

        public float getTouchX() {
            return touchX;
        }

        public float getTouchY() {
            return touchY;
        }

        Paint getDrawPaint() {
            return mDrawPaint;
        }

        Path getDrawPath() {
            return mDrawPath;
        }
    }

    boolean undo() {
        if (mLinePaths.size() > 0) {
            mRedoLinePaths.add(mLinePaths.remove(mLinePaths.size() - 1));
            invalidate();
        }
        if (mBrushViewChangeListener != null) {
            mBrushViewChangeListener.onViewRemoved(this);
        }
        return mLinePaths.size() != 0;
    }

    boolean redo() {
        if (mRedoLinePaths.size() > 0) {
            mLinePaths.add(mRedoLinePaths.remove(mRedoLinePaths.size() - 1));
            invalidate();
        }
        if (mBrushViewChangeListener != null) {
            mBrushViewChangeListener.onViewAdd(this, ViewType.BRUSH_DRAWING);
        }
        return mRedoLinePaths.size() != 0;
    }

    public void clearTouchPoint() {
        mTouchXList.clear();
        mTouchYList.clear();
    }


    private void touchStart(float x, float y) {
        mRedoLinePaths.clear();
        mPath.reset();
        mPath.moveTo(x, y);
        mTouchX = x;
        mTouchY = y;
        if (mBrushViewChangeListener != null) {
            mBrushViewChangeListener.onStartDrawing();
        }
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mTouchX);
        float dy = Math.abs(y - mTouchY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mTouchX, mTouchY, (x + mTouchX) / 2, (y + mTouchY) / 2);
            mTouchX = x;
            mTouchY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mTouchX, mTouchY);
        // Commit the path to our offscreen
        mDrawCanvas.drawPath(mPath, mDrawPaint);
        // kill this so we don't double draw
        mLinePaths.add(new LinePath(mPath, mDrawPaint,mTouchX,mTouchY));
        mPath = new Path();
        if (mBrushViewChangeListener != null) {
            mBrushViewChangeListener.onStopDrawing();
            mBrushViewChangeListener.onViewAdd(this, ViewType.BRUSH_DRAWING);
        }
    }

    public Bitmap generateBimap() {
        float maxTouchx = Collections.max(mTouchXList);
        float minTouchx = Collections.min(mTouchXList);
        float maxTouchY = Collections.max(mTouchYList);
        float minTouchY = Collections.min(mTouchYList);
//        Bitmap bitmap = Bitmap.createBitmap((int)(maxTouchx - minTouchx),(int)(maxTouchY - minTouchY), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        Log.e("linePath","line path size is " + mLinePaths.size());
//        for (LinePath path : mLinePaths) {
//            canvas.drawPath(path.getDrawPath(),path.getDrawPaint());
//        }
//        canvas.save(Canvas.ALL_SAVE_FLAG);
//        canvas.restore();
//        Log.e("fdsf",(int)(maxTouchx - minTouchx) + " " + (int)(maxTouchY - minTouchY));
        int width = (int) (maxTouchx - minTouchx);
        int height = (int) (maxTouchY - minTouchY);
        // 计算缩放比例.

        Bitmap map = null;
        Bitmap newMap = null;
        if(bitmapList.size() > 0) {
            map = bitmapList.get(0);
//            if((int)minTouchx + (int)(maxTouchx - minTouchx) < map.getWidth() && (int)minTouchY + (int)(maxTouchY - minTouchY) < map.getHeight() && minTouchx > 0 && minTouchY > 0 ) {
//            }
            try {
                newMap = Bitmap.createBitmap(map,(int)minTouchx,(int)minTouchY,(int)(maxTouchx - minTouchx),(int)(maxTouchY - minTouchY));
//                map.recycle();
            }catch (Exception e) {
                ToastUtils.showShort(getContext(),"画笔不能超过屏幕边界");
            }
//            float scaleWidth = ((float) width) / bitmapList.get(0).getWidth();
//            float scaleHeight = ((float) height) / bitmapList.get(0).getHeight();
//            Matrix matrix = new Matrix();
//            matrix.postScale(scaleWidth, scaleHeight);
//            map = Bitmap.createBitmap(bitmapList.get(0), 0, 0, bitmapList.get(0).getWidth(), bitmapList.get(0).getHeight(), matrix, true);
        }
        return newMap;
    }

    public float getMinTouchX() {
        return Collections.min(mTouchXList);
    }


    public float getMinTouchY() {
        return Collections.min(mTouchYList);
    }

}