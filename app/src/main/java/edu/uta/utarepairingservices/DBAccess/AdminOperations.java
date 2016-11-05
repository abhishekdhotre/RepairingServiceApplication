package edu.uta.utarepairingservices.DBAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import edu.uta.utarepairingservices.TableInfo.UserTableInfo;

public class AdminOperations extends SQLiteOpenHelper {
    public static final int database_version = 1;
    public String CREATE_QUERY = "CREATE TABLE " + UserTableInfo.TableInfo.TABLE_NAME+" ("+ UserTableInfo.TableInfo.FIRST_NAME+" TEXT,"+ UserTableInfo.TableInfo.LAST_NAME+" TEXT)";

    public AdminOperations(Context context) {
        super(context, UserTableInfo.TableInfo.DATABASE_NAME, null, database_version);
        Log.d("AdminOperations", "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.d("AdminOperations", "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void putInformation(AdminOperations dop, String first_name, String last_name) {
        SQLiteDatabase sq = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(UserTableInfo.TableInfo.FIRST_NAME, first_name);
        cv.put(UserTableInfo.TableInfo.LAST_NAME, last_name);
        long k = sq.insert(UserTableInfo.TableInfo.TABLE_NAME, null, cv);
        Log.d("AdminOperations", "One row inserted!");
    }
}
