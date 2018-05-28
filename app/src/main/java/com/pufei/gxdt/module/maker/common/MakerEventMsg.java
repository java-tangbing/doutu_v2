package com.pufei.gxdt.module.maker.common;

public class MakerEventMsg {
    private int type;
    private String url;
    private int color;

    public MakerEventMsg(int type, String url) {
        this.type = type;
        this.url = url;
    }

    public MakerEventMsg(int type, int color) {
        this.type = type;
        this.color = color;
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
