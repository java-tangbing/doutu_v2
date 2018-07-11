package com.pufei.gxdt.db.migration;

import com.pufei.gxdt.db.AppDatabase;
import com.pufei.gxdt.db.ImageDraft;
import com.pufei.gxdt.db.ImageDraft_Table;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

@Migration(version = AppDatabase.VERSION, database = AppDatabase.class)
public class ImageDraftMigration extends AlterTableMigration<ImageDraft> {

    public ImageDraftMigration(Class<ImageDraft> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();
        addColumn(SQLiteType.INTEGER, ImageDraft_Table.type.getNameAlias().name());
    }
}
