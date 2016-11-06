package edu.uta.utarepairingservices.DBAccess;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import edu.uta.utarepairingservices.TableInfo.UserDetails;

/**
 * Created by Sunil on 11/5/2016.
 */
public class LoginOperations {
    public static final String LOGTAG = "LOGIN_MNGMNT_SYS";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    public LoginOperations(Context context){
        dbhandler = new LoginDBHandler(context);
    }

    public void open(){
        Log.i(LOGTAG,"Database Opened");
        database = dbhandler.getWritableDatabase();
    }

    public void close(){
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();

    }

    public void addUser(UserDetails UserDetails){
        ContentValues values  = new ContentValues();
        values.put(LoginDBHandler.COLUMN_NETID,UserDetails.getNetID());
        values.put(LoginDBHandler.COLUMN_PASSWORD, UserDetails.getPwd());
        values.put(LoginDBHandler.COLUMN_ROLE, UserDetails.getRole());
        long loginID = database.insert(LoginDBHandler.TABLE_LOGIN,null,values);
        UserDetails.setLoginID(loginID);
    }

    public String getRole(String netID)
    {
        Cursor cursor = database.query(LoginDBHandler.TABLE_LOGIN, new String[]{ LoginDBHandler.COLUMN_ROLE },
        LoginDBHandler.COLUMN_NETID + "=?",
                new String[]{String.valueOf(netID)},null,null,null);
        if (cursor != null)
            cursor.moveToFirst();

        // return Employee
        return cursor.getString(0);
    }

    public void removeUser(UserDetails userDetails) {
        database.delete(LoginDBHandler.TABLE_LOGIN, LoginDBHandler.COLUMN_ID + "=" + userDetails.getLoginID(), null);
    }
}
