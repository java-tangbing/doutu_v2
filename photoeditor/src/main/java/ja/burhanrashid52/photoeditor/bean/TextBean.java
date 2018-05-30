package ja.burhanrashid52.photoeditor.bean;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class TextBean {
    private View frameLayout;
    private TextView textView;
    private ImageView addImageClose;
    private FrameLayout addFrameLayout;
    private boolean isVisibleBorder;
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public ImageView getAddImageClose() {
        return addImageClose;
    }

    public void setAddImageClose(ImageView addImageClose) {
        this.addImageClose = addImageClose;
    }

    public FrameLayout getAddFrameLayout() {
        return addFrameLayout;
    }

    public void setAddFrameLayout(FrameLayout addFrameLayout) {
        this.addFrameLayout = addFrameLayout;
    }

    public View getFrameLayout() {
        return frameLayout;
    }

    public void setFrameLayout(View frameLayout) {
        this.frameLayout = frameLayout;
    }

    public boolean isVisibleBorder() {
        return isVisibleBorder;
    }

    public void setVisibleBorder(boolean visibleBorder) {
        isVisibleBorder = visibleBorder;
    }
}
