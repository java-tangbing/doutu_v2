package com.pufei.gxdt.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDatabase.class)
public class TextDraft extends BaseModel {
    @PrimaryKey(autoincrement = true)
    long id;
    @Column
    public String imageId;
    @Column
    public String text;
    @Column
    public int textColor;
    @Column
    public float textSize;
    @Column
    public String textFont;
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
