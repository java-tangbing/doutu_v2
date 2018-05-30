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
    public int imageWidth;
    @Column
    public int imageHeight;

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
}