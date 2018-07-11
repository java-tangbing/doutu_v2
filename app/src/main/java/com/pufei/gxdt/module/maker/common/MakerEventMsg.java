package com.pufei.gxdt.module.maker.common;

import android.graphics.Bitmap;

public class MakerEventMsg {
    private int type;
    private String url;
    private int color;
    private int textFontStyle;
    private float x;
    private float y;
    private Bitmap bitmap;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public MakerEventMsg(int type, float x, float y, Bitmap bitmap) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
    }

    public MakerEventMsg(int type, String url) {
        this.type = type;
        this.url = url;
    }

    public MakerEventMsg(int type, int color) {
        this.type = type;
        this.color = color;
    }

    public MakerEventMsg(int type, int color, int textFontStyle) {
        this.type = type;
        this.color = color;
        this.textFontStyle = textFontStyle;
    }

    public int getTextFontStyle() {
        return textFontStyle;
    }

    public void setTextFontStyle(int textFontStyle) {
        this.textFontStyle = textFontStyle;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
