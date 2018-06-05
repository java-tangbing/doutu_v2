package com.pufei.gxdt.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.UUID;

@Table(database = AppDatabase.class)
public class DraftInfo extends BaseModel{
    @PrimaryKey(autoincrement = true)
    long id;
    @Column
    public String imagePath;
    @Column
    public String imageId;
    @Column
    public int width;
    @Column
    public int height;
    @Column
    public boolean isDraft;
    @Column
    public String uid;
    @Column
    public String originTable;
    @Column
    public String originId;
    @Column
    public String originImageId;
    @Column
    public String make_url;


}
