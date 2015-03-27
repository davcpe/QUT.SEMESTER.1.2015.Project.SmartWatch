package com.example.davcpe.healthprototypeapp;

/**
 * Created by davcpe on 3/27/2015.
 *
 *
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class UserTABLE {

    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSQLite, readSQLite;
    public static final String TABLE_USER = "userTABLE";
    public static final String COLUMN_NO_USER  ="_no";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static  final String COLUMN_USER_PASSWORD = "user_password";
    public static  final String COLUMN_OFFICER = "user_officer";

    public UserTABLE(Context context) {
        objMyOpenHelper = new MyOpenHelper(context);
        writeSQLite = objMyOpenHelper.getWritableDatabase();
        readSQLite = objMyOpenHelper.getReadableDatabase();
    }// Constructor

    public String[]searchUser(String strUSerName){
        try {

            String strData[]= null;
            Cursor objCursor = readSQLite.query(TABLE_USER,
                    new String[] {COLUMN_NO_USER, COLUMN_USER_ID,COLUMN_USER_NAME,COLUMN_USER_PASSWORD,COLUMN_OFFICER},COLUMN_USER_NAME+"=?",
                    new String[] {String.valueOf(strUSerName)}, null,null,null,null);
            if (objCursor != null) {
                if(objCursor.moveToFirst()){
                    strData = new String[objCursor.getColumnCount()];
                    strData[0] = objCursor.getString(0);
                    strData[1] = objCursor.getString(1);
                    strData[2] = objCursor.getString(2);
                    strData[3] = objCursor.getString(3);
                    strData[4] = objCursor.getString(4);
                }// if2
            }//if1

            objCursor.close();
            return strData;
        }catch (Exception e){
            return null;
        }


    }//search User

    public long addValueUser (String strUserID, String strUserName, String strPassword, String strOfficer){
        ContentValues objContentValues = new ContentValues();

        objContentValues.put(COLUMN_USER_ID, strUserID);
        objContentValues.put(COLUMN_USER_NAME, strUserName);
        objContentValues.put(COLUMN_USER_PASSWORD, strPassword);
        objContentValues.put(COLUMN_OFFICER, strOfficer);
        return writeSQLite.insert(TABLE_USER, null,objContentValues);
    } //addValueToUser
}
