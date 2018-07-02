package com.pufei.gxdt.db.migration;

import com.pufei.gxdt.db.AppDatabase;
import com.pufei.gxdt.db.TextDraft;
import com.pufei.gxdt.db.TextDraft_Table;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

@Migration(version = AppDatabase.VERSION,database = AppDatabase.class)
public class TextDraftMigration extends AlterTableMigration<TextDraft>{

    public TextDraftMigration(Class<TextDraft> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();
        addColumn(SQLiteType.REAL, TextDraft_Table.width.getNameAlias().name());
        addColumn(SQLiteType.REAL, TextDraft_Table.height.getNameAlias().name());
    }
}
