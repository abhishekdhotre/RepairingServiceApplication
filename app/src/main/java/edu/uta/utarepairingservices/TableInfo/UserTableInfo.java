package edu.uta.utarepairingservices.TableInfo;

import android.provider.BaseColumns;

public class UserTableInfo {

    public UserTableInfo() {

    }

    public static abstract class TableInfo implements BaseColumns {
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String DATABASE_NAME = "utadb";
        public static final String TABLE_NAME = "customer";

    }
}
