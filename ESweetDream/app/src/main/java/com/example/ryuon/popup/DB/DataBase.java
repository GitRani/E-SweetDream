package com.example.ryuon.popup.DB;

import android.provider.BaseColumns;

public class DataBase {
    public static final class CreateDB implements BaseColumns {
        public static final String _TABLENAME = "GroupList";
        public static final String NAME = "GroupName";
        public static final String MODULE = "Module";
        public static final String _ID = "id";

        public static final String _CREATE =
                "CREATE TABLE IF NOT EXISTS "+_TABLENAME+"("
                        +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                        +NAME+" TEXT NOT NULL , "
                        +MODULE+" TEXT );";
    }
}
