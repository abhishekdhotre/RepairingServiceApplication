package edu.uta.utarepairingservices.DBAccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Shwetha on 11/5/2016.
 */
public class LoginDBHandler extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "login.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_LOGIN = "login";
    public static final String COLUMN_ID = "loginId";
    public static final String COLUMN_NETID = "netID";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_LOGIN + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NETID + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_ROLE + " TEXT " +
                    ")";

    public LoginDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_LOGIN);
        db.execSQL(TABLE_CREATE);
    }
}
