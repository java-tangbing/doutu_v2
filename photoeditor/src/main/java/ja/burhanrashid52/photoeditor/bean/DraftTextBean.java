package ja.burhanrashid52.photoeditor.bean;

import android.graphics.Typeface;

public class DraftTextBean {
    private String text;
    private int textColor;
    private float textSize;
    private Typeface textFont;
    private float translationX;
    private float translationY;
    private float scaleX;
    private float scaleY;
    private float rotation;
    private float width;
    private float height;
    private int bgWidth;
    private int bgHeight;

    public int getBgWidth() {
        return bgWidth;
    }

    public void setBgWidth(int bgWidth) {
        this.bgWidth = bgWidth;
    }

    public int getBgHeight() {
        return bgHeight;
    }

    public void setBgHeight(int bgHeight) {
        this.bgHeight = bgHeight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public Typeface getTextFont() {
        return textFont;
    }

    public void setTextFont(Typeface textFont) {
        this.textFont = textFont;
    }

    public float getTranslationX() {
        return translationX;
    }

    public void setTranslationX(float translationX) {
        this.translationX = translationX;
    }

    public float getTranslationY() {
        return translationY;
    }

    public void setTranslationY(float translationY) {
        this.translationY = translationY;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
