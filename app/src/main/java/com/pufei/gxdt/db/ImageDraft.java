package com.pufei.gxdt.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.UUID;

@Table(database = AppDatabase.class)
public class ImageDraft extends BaseModel {
    @PrimaryKey(autoincrement = true)
    long id;
    @Column
    public String imageId;
    @Column
    public String stickerImagePath;
    @Column
    public float imageWidth;
    @Column
    public float imageHeight;

    @Column
    public float translationX;
    @Column
    public float translationY;
    @Column
    public float scaleX;
    @Column
    public float scaleY;
    @Column
    public float rotation;
    @Column
    public boolean isDraft;
    @Column
    public int type;//0--图片,1---画笔
}
