package ja.burhanrashid52.photoeditor;

import android.graphics.Bitmap;

public class BitmapBean {
    private Bitmap bitmap;
    private int delay;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
