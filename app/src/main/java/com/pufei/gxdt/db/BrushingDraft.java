package com.pufei.gxdt.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDatabase.class)
public class BrushingDraft extends BaseModel {
    @PrimaryKey(autoincrement = true)
    long id;
    @Column
    public String imageId;
    @Column
    public int brushColor;
    @Column
    public int brushSize;
}
