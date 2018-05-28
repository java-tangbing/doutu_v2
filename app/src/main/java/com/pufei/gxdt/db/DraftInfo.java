package com.pufei.gxdt.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.UUID;

@Table(database = AppDatabase.class)
public class DraftInfo extends BaseModel{
    @PrimaryKey
    UUID id;
    @Column
    public float rotation;
    @Column
    public long viewId;
    @Column
    public int width;
    @Column
    public int height;
    @Column
    public float xPos;
    @Column
    public float yPos;
    @Column
    public String text;
    @Column
    public int color;
    @Column
    public int type;
}
