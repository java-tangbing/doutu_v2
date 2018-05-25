package ja.burhanrashid52.photoeditor;

import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class TextBean {
    private View addEditText;
    private ImageView addImageClose;
    private FrameLayout addFrameLayout;
    private boolean isVisibleBorder;


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

    public View getAddEditText() {
        return addEditText;
    }

    public void setAddEditText(View addEditText) {
        this.addEditText = addEditText;
    }

    public boolean isVisibleBorder() {
        return isVisibleBorder;
    }

    public void setVisibleBorder(boolean visibleBorder) {
        isVisibleBorder = visibleBorder;
    }
}
